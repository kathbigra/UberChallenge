Uber Challenge App

This is a mobile coding challenge app from Uber. The app takes a input from the user and seraches flickr and return relevant results in JSON format. The app has to display the results in 3 columns with infinite scrolling without any external libraries. Also the app have some Image caching implemented to save network and time.

What's New (1.0):
First implementation.

Getting Started
Installation
Clone this repository and import into Android Studio
git clone https://github.com/kathbigra/UberChallenge.git

Prerequisites
A working android studio setup with Java 1.8+ and android sdk installed.

Code structure:
The code structure follows an standard Android setup for source, test and resources. In the code part classes are divided in various packages as per their classification.

Solution logic/approach:
The solution contains of 2 different Activity classes. One of them deals with accepting the search query from the user, while the second one deals with displaying of search results. The logic of actually fetching the data is written inside the second activity (ResultActivity). Once the UI is setup to display the image grid, an Asynch call is started to execute the search so that UI thread is not burdened. 
Once the data is fetched from the query, we process it to get the images from the result. These image details are the added to the adapter of the grid view. We are only storing image name and their download URL in the adapter. Once the adapter gets a call to display a particular image, it implements sort of a Lazy Loading to fetch Image bitmap from the web. Before doing that the adapter provides a dummy placeholder image for every images.
Before downloading actual image we try to look into Memory cache first and then the File cache so that we dont download same image again and again. We also cancel and download request if the user has scrolled away from that asset and that particular image is no longer needed. In this solution we are using 5 threads in threadpool to download multiple images simultaneously. After download is successful the dummy image is removed and the actual image is replaced.
In case the service is not returning any more asset we assume that all the assets are already consumed, Also if in first attempt no image are returned we assume the query string is wrong. 
File cache simple store all the bitmap on the local storage with file name as the hashcode of the image url. In case user goes back to the search page we clean all the cache and try to restore athe resource that is in use.
Deployment:
The generated apk is added in the code at “UberChallenge\app\build\outputs\apk\debug”. It can be directly installed to any Android device or any Android emulator.
One can also deploy on any device from the code setup. Create a run configuration for your app with starting point as SearchActivity(default launcher activity).

Running Tests:
One can run any Test File separately or run the Test Suite  “UnitTestSuiteInstrumented.java”. Currently the code coverage is at 78%. The coverage reports are also added in the code at “UberChallenge\app\build\reports\coverage\debug\index.html”.

Things to be implement further:
Unit Tests: Most of the test cases currently are only focused on positive test case and code coverage. Usually  half of my test cases focus on negative scenarios and conditions/checks. These are the area where Unit test cases are really helpful. 
Code refractor: I would have liked to refractor the code further so that the logic are really separated from the View part. Right now both are a bit mashed up.
UI improvement: I have provided a GridView and a progressbar inside a LinearView instead of a Relative View. Similarly a new page to display the clicked result image in full screen would have been nice. May be a few settings option to modify the number of columns of image bitmap size as per the user requirement.
More feedback to user: In case user reaches the end of their searches or the query string is wrong or any network error or storage error. Ideally I would like to notify users of any of these scenario as well.
Metadata Options: Maybe give user options to specify more metadatas to refine their search even further.










