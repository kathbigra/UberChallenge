package com.example.faisal.uberchallenge.utilities;

import android.graphics.Bitmap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtility {

    public static  HttpURLConnection getUrlConnection(URL searchUrl) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) searchUrl.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setReadTimeout(10000 /* milliseconds */);
        urlConnection.setConnectTimeout(15000 /* milliseconds */);
        urlConnection.setDoOutput(true);
        return urlConnection;
    }

    public static String getJsonString(URL url) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        char[] buffer = new char[1024];
        String jsonString = new String();
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }
        br.close();
        jsonString = sb.toString();
        return jsonString;
    }

    public static Bitmap downloadImage(URL downloadUrl, File f) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) downloadUrl.openConnection();
        Bitmap bitmap = null;
        conn.setConnectTimeout(30000);
        conn.setReadTimeout(30000);
        conn.setInstanceFollowRedirects(true);
        InputStream is = conn.getInputStream();
        OutputStream os = new FileOutputStream(f);
        copyStream(is, os);
        os.close();
        conn.disconnect();
        bitmap = FileUtility.decodeFile(f);
        return bitmap;

    }

    private static void copyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            while (true) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
        }
    }

}
