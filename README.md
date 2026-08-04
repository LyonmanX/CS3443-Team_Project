# CS3443-Team_Project Screen Report App
For the team project

This project is a JavaFX application designed to help users manage a personal video game library. It allows users to add games, edit game details, track playtime with a built‑in timer, and view overall statistics about their collection. The application uses CSV storage for persistence and follows a controller‑based architecture using FXML layouts.

Project Overview

The goal of this project is to demonstrate practical application programming skills using Java, JavaFX, and structured program design. The application includes multiple screens, navigation between views, persistent data storage, and a clean separation between models, controllers, and utility classes.

Features

Add new games with title, platform, genre, hours played, status, cover image, and notes.

Edit existing games and update their information.

Delete games from the library.

View detailed information for each game.

Track play sessions using a built‑in timer that adds elapsed time to the selected game.

Import and store cover images in a shared data folder.

Display total games and total hours played on the home screen.

Automatically load and save game data using a CSV file.

Project Structure

src/

Main.java

/controller/
HomeController.java
GameLibraryController.java
AddGameController.java
EditGameController.java
GameDetailsController.java
GameTimerController.java

/model/
Game.java
GameLibrary.java
GameStatus.java

/util/
SceneNavigator.java
GameImages.java

/resources/
/layouts/ (FXML files)
/data/ (CSV file and cover images)

Data Storage

All game data is stored in the file:
src/resources/data/VideoGameLibrary.csv

Cover images are stored in the same folder. A placeholder image is used when no cover is available.

How to Run
1. Install a JDK

Install JDK 26. In IntelliJ, go to File → Project Structure → Project and confirm the Project SDK is set to 26 (or update it to match whatever JDK you have installed, then update the module's Module SDK to match under File → Project Structure → Modules → Dependencies).

2. Install and configure JavaFX

JavaFX is a separate SDK from the JDK and has to be added manually:

Download the JavaFX SDK (not the jmods) for your OS from https://gluonhq.com/products/javafx/, matching your JDK version where possible.
Extract it somewhere permanent (e.g. C:\javafx-sdk-26 or ~/javafx-sdk-26) — avoid nested folders you might delete later.
In IntelliJ: File → Project Structure → Libraries → + → Java → select the lib folder inside the extracted JavaFX SDK (the folder containing files like javafx.controls.jar, javafx.fxml.jar, javafx.base.jar, javafx.graphics.jar).
Attach the library to the project module under File → Project Structure → Modules → Dependencies if it isn't already listed there.

3. Add VM options for running the app

JavaFX modules aren't found automatically at runtime — you need to tell the JVM where they are:

Run → Edit Configurations... → select (or create) the Main configuration.
Under "Modify options," enable Add VM options.
Enter, using your actual JavaFX SDK path:

   --module-path "/path/to/javafx-sdk-26/lib" --add-modules javafx.controls,javafx.fxml

4. Clone and run
Clone the repository.
Open the project in your IDE.

Run Main.java.

For testing:

There is an additional image in /testimage, this is provided for adding a new game with an icon. Or you can use any custom png or jpg you'd like.

Team Members

Bryan Larison
Ethan Renaud
Matthew Dolotina
Jack Grimmer 

License

This project is intended for academic use as part of CS3443 – Application Programming.
