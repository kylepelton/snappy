# Photo Schema Application

This is the repository for Team 7102's Junior Design project, the **Photo Schema Application** (a.k.a. "Snappy"). Snappy is a photo organization application which streamlines the photo tagging process by incorporating voice control so that users can tag photos in a hands-free manner.

## Release Notes
### Version
* Snappy 1.0
### New Features
* Added ability to view photos and their tag connections as a graph
  * Added tab view at top of screen
  * Clicking "View Related Photos" on a photo screen opens a new tab with a graph of connected photos
* Added functionality to search bar on main screen so that users can look up photos based on tag
* Added logging to a text file in the case of an exception
* Added multi-select functionality to the grid view
  * Added the "Tag Selected Photos" and "Tag Selected Photos With" options to the Start Tagging button
    * "Tag Selected Photos" opens up the currently selected photos in the tagging screen
    * "Tag Selected Photos With" tags all selected photos with the same set of tags
  * Added the ability to delete multiple photos at once using multi-select
* Added help documentation to the grid view which can be accessed by clicking the "Help" button
### Bug Fixes
* Issue#113: Application no longer breaks if the snappy_photos directory is deleted after it has been initially created
* Photos can no longer have a blank tag
* The photo tagging screen no longer automatically adds a line break upon trying to edit the tagging text box
### Known Bugs and Defects
* Issue#10: Application screen does not resize based on size of the user's screen
* The graph view sometimes may display images on top of other images

## Install Guide
### Prerequisites
1. Ensure that [Java](http://www.oracle.com/technetwork/java/javase/downloads/index.html) is installed and configured on your machine. Note that you need to have at least Java JRE 1.8 to run this application. If you wish to develop the project, then it is recommended that you have Java JDK 1.8 installed as well.
### Dependent Libraries
&nbsp;&nbsp;&nbsp;&nbsp;N/A
### Download Instructions
1. Click [here](https://github.com/kylepelton/snappy/archive/master.zip) to download Snappy.
### Build Instructions
*NOTE*: Only users who intend to develop Snappy need to complete the following instructions. Any other users can proceed to the next section since the download zip already contains an executable jar file.
1. If you haven't already, unzip the file you just downloaded to whichever directory you desire.
2. Install [Ant](http://ant.apache.org/), the build system Snappy uses.
3. Open your terminal, navigate to the top level of the snappy directory, and type `ant` to compile, build, and run Snappy. The following commands are also available:
    * `ant build` Creates the build directory.
    * `ant compile` Creates the build directory and compiles the source code into the build directory.
    * `ant jar` Creates the executable jar file in the build directory.
    * `ant run` Compiles, builds, and runs Snappy. The command `ant` defaults to this one.
    * `ant clean` Deletes the build directory.
### Installing/Running Snappy
1. If you haven't already, unzip the file you just downloaded to whichever directory you desire.
2. Open the directory using your native File Explorer. Find the `snappy.jar` file.
3. Double click the `snappy.jar` file to run Snappy. If double clicking doesn't work, then you can alternatively open your terminal/command prompt, navigate to the top level of the snappy directory, and run the command `java -jar snappy.jar`. You can also try using the build commands to run the application if you have Ant installed.
### Troubleshooting
* If you have any questions or problems related to installing and configuring Java on your machine, please visit Oracle at [http://www.oracle.com/technetwork/java/javase/downloads/index.html](http://www.oracle.com/technetwork/java/javase/downloads/index.html).
* If you have any questions about installing or using Ant, you can find relevant information on the Apache Ant website at [http://ant.apache.org/](http://ant.apache.org/).
* More information related to running jar files can be found on Oracle's Java Tutorials at [https://docs.oracle.com/javase/tutorial/deployment/jar/run.html](https://docs.oracle.com/javase/tutorial/deployment/jar/run.html).
