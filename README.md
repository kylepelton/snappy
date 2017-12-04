# Photo Schema Application

This is the repository for Team 7102's Junior Design project, the **Photo Schema Application** (a.k.a. "Snappy"). Snappy is a photo organization application which streamlines the photo tagging process by incorporating voice control so that users can tag in a hands-free manner.

## Release Notes
### Version
* Snappy 1.0
### New Features
* Added ability to view photos and their tag connections as a graph
  * Added tab view at top of screen
  * Clicking "View Related Photos" on a photo opens a new tab with a graph of connected photos
* Added functionality to search bar on main screen so that users can look up photos based on tag
* Added logging to a text file in the case of an exception
* Added multi-select functionality to the grid view
  * Added the "Tag Selected Photos" and "Tag Selected Photos With" options to the Start Tagging button
  * Added the ability to delete multiple photos at once using multi-select
### Bug Fixes
* Photos can no longer have a blank tag
### Known Bugs and Defects
* #10: Application screen does not resize based on size of the user's screen
* The graph view sometimes may display images on top of other images.

## Install Guide
### Prerequisites
1. Ensure that [Java](http://www.oracle.com/technetwork/java/javase/downloads/index.html) is installed on your machine. Note that you need at least Java 8 to run this application.
### Dependent Libraries
&nbsp;&nbsp;&nbsp;&nbsp;N/A
### Download Instructions
1. Click [here](https://github.com/kylepelton/snappy/archive/master.zip) to download Snappy.
### Build Instructions
*NOTE*: Only users who intend to develop Snappy need to complete the following instructions. Any other users can proceed to the next section.
1. If you haven't already, unzip the file you just downloaded to whichever directory you desire.
2. Install [Ant](http://ant.apache.org/), the build system Snappy uses.
3. Open your terminal, navigate to the top level of the Snappy directory, and type `ant` to compile, build, and run Snappy.
### Installing/Running Snappy
1. If you haven't already, unzip the file you just downloaded to whichever directory you desire.
2. Open the directory using your native File Explorer. Find the `snappy.jar` file.
3. Double click this file to run Snappy. If double clicking doesn't work, then you can alternatively open your terminal/command prompt, navigate to the snappy folder, and type `java -jar snappy.jar`
