package com.example.faisal.uberchallenge.components;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Handler;
import android.widget.ImageView;

import com.example.faisal.uberchallenge.R;
import com.example.faisal.uberchallenge.cache.FileCache;
import com.example.faisal.uberchallenge.cache.MemoryCache;
import com.example.faisal.uberchallenge.utilities.NetworkUtility;

import java.io.File;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.example.faisal.uberchallenge.utilities.FileUtility.decodeFile;


public class ImageLoader {
    private MemoryCache memoryCache;
    private FileCache fileCache;
    //Default number of threads for simultaneous downloads of images.
    private static final int DEFAULT_NO_OF_THREADS = 5;
    private Map<ImageView, String> recyclerMap =
            Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
    private ExecutorService executorService;
    private Resources resources;
    private Map<String, Boolean> serverRequestMap = Collections.synchronizedMap(new HashMap<String, Boolean>());


    public ImageLoader(Context context) {
        fileCache = new FileCache(context);
        executorService = Executors.newFixedThreadPool(DEFAULT_NO_OF_THREADS);
        memoryCache = new MemoryCache();
        resources = context.getResources();
    }

    public void displayImage(String url, ImageView imageView) {
        recyclerMap.put(imageView, url);
        //Check if image exists in memory cache.
        Bitmap bitmap = memoryCache.get(url);
        if (bitmap != null)
            //If yes set the image.
            imageView.setImageBitmap(bitmap);
        else {
            //If not queue the request for download image.
            queuePhoto(url, imageView);
            //Set dummy image while the actual image is being downloaded.
            imageView.setImageDrawable(resources.getDrawable(R.drawable.stub));
        }
    }

    private void queuePhoto(String url, ImageView imageView) {
        //Create a new thread for downloading the request and put in the executor service.
        Photo p = new Photo(url, imageView);
        executorService.submit(new PhotoLoader(p));
    }


    private Bitmap getBitmap(String url) {
        //Try to get a file for cached image.
        File f = fileCache.getFile(url);
        //from SD cache
        Bitmap b = decodeFile(f);
        //If file exists then return the bitmap image.
        if (b != null)
            return b;

        //If file is not available, fetch from web.
        try {
            URL imageUrl = new URL(url);
            return NetworkUtility.downloadImage(imageUrl,f);
        } catch (Throwable ex) {
            ex.printStackTrace();
            if (ex instanceof OutOfMemoryError)
                memoryCache.clear();
            return null;
        }
    }




    //Task for the queue
    public class Photo {
        public String url;
        public ImageView imageView;

        public Photo(String u, ImageView i) {
            url = u;
            imageView = i;
        }
    }

    class PhotoLoader implements Runnable {
        Photo photo;

        PhotoLoader(Photo photo) {
            this.photo = photo;
        }

        @Override
        public void run() {
            //We have come to this code only in memory cache does not exists.
            try {
                //If reusing an existing Image View
                if (imageViewReused(photo))
                    return;
                //Is download request already going on.
                Boolean isServerRequestExists = serverRequestMap.get(photo.url);
                isServerRequestExists = (isServerRequestExists == null ? false : isServerRequestExists);
                Bitmap bmp = null;
                if (!isServerRequestExists) {
                    //Put the request is request map.
                    serverRequestMap.put(photo.url, true);
                    try {
                        //Try to fetch the bitmap from File cache, if not there then download the bitmap/
                        bmp = getBitmap(photo.url);
                    } catch (Exception e) {
                        //In case of any error, remove the request from the map
                        serverRequestMap.put(photo.url, false);
                        if (imageViewReused(photo))
                            return;
                        bmp = getBitmap(photo.url);
                        serverRequestMap.put(photo.url, true);
                    }
                } else return; //return if request already going on.
                if (bmp != null) {
                    //If image download is successful, add the file in file cache and remove from ongoing request map.
                    serverRequestMap.remove(photo.url);
                    memoryCache.put(photo.url, bmp);
                }
                if (imageViewReused(photo)) {
                    return;
                }
                BitmapDisplayer bd = new BitmapDisplayer(bmp, photo);
                Activity a=(Activity)photo.imageView.getContext();
                a.runOnUiThread(bd);
                //handler.post(bd);

            } catch (Throwable th) {
                th.printStackTrace();
            }

        }
    }

    //The image is no longer needed. In case returns true. the request for this image will be terminated.
    //Probably user scrolled away.
    private boolean imageViewReused(Photo photo) {
        String tag = recyclerMap.get(photo.imageView);
        return  (tag == null || !tag.equals(photo.url));
           // return true;
        //return false;
    }

    //Used to display bitmap in the UI thread
    class BitmapDisplayer implements Runnable {
        Bitmap bitmap;
        Photo photo;

        public BitmapDisplayer(Bitmap b, Photo p) {
            bitmap = b;
            photo = p;
        }

        public void run() {
            if (imageViewReused(photo))
                return;
            //Set bitmap if image is downloaded. If not set the dummy image.
            if (bitmap != null)
                photo.imageView.setImageBitmap(bitmap);
            else
                photo.imageView.setImageResource(R.drawable.stub);
        }
    }

    public void clearCache() {
        memoryCache.clear();
        fileCache.clear();
    }

}