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
controllers/
HomeController.java
GameLibraryController.java
AddGameController.java
EditGameController.java
GameDetailsController.java
GameTimerController.java

model/
Game.java
GameLibrary.java
GameStatus.java
GameTimer.java

util/
SceneNavigator.java
GameImages.java

resources/
layouts/ (FXML files)
data/ (CSV file and cover images)

Data Storage

All game data is stored in the file:
src/resources/data/VideoGameLibrary.csv

Cover images are stored in the same folder. A placeholder image is used when no cover is available.

How to Run

Install Java 17 or later.

Install JavaFX and configure it in your IDE.

Clone the repository.

Open the project in your IDE.

Run the main application file.

The application will load the CSV file and open the Home Screen.

Team Members

Bryan Larison
Ethan Renaud
Matthew Dolotina
Jack Grimmer 

License

This project is intended for academic use as part of CS3443 – Application Programming.
