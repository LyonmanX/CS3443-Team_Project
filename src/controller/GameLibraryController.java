package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Button;
import util.SceneNavigator;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class GameLibraryController {

    @FXML
    private FlowPane gameFlow;
    // FlowPane that holds all game cells.

    @FXML
    private Button addGameButton;

    @FXML
    private Button backButton;

    private static final String CSV_PATH = "/resources/data/VideoGameLibrary.csv";
    // Path to the CSV file inside the resources/data folder.

    private static final String DATA_FOLDER = "/resources/data/";
    // Folder containing both the CSV file and all game images.

    public void initialize() {
        // Runs when the FXML view is loaded.
        // Loads game data from the CSV and displays each game.
        List<Game> games = loadGamesFromCSV();
        displayGames(games);
    }

    private List<Game> loadGamesFromCSV() {
        // Reads the CSV file and converts each row into a Game object.
        List<Game> games = new ArrayList<>();

        try {
            InputStream is = getClass().getResourceAsStream(CSV_PATH);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line = br.readLine();
            // Skip the header row.

            while ((line = br.readLine()) != null) {
                // Split the row into columns.
                String[] data = line.split(",");

                // Column 0 = GameTitle
                String title = data[1];

                // Column 7 = ImageFile
                String imageFile = data[6];

                // Store the game entry.
                games.add(new Game(title, imageFile));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return games;
    }

    private void displayGames(List<Game> games) {
        // Creates a game-cell.fxml for each game and adds it to the FlowPane.
        try {
            for (Game game : games) {

                // Load the FXML template for a single game cell.
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/layouts/game-cell.fxml"));
                StackPane cell = loader.load();

                // Locate the ImageView inside the cell.
                ImageView cover = (ImageView) cell.lookup("#coverImage");

                // Load the game's image from resources/data.
                Image img = new Image(getClass().getResourceAsStream(DATA_FOLDER + game.getImageFile()));

                // Assign the image to the ImageView.
                cover.setImage(img);

                // Add the completed cell to the FlowPane.
                gameFlow.getChildren().add(cell);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Simple data class representing a game entry.
    public static class Game {
        private final String title;
        private final String imageFile;

        public Game(String title, String imageFile) {
            this.title = title;
            this.imageFile = imageFile;
        }

        public String getImageFile() {
            return imageFile;
        }
    }

    @FXML
    private void onAddGame() {
        SceneNavigator.switchScene(addGameButton,"/layouts/addgame.fxml");
    }

    @FXML private void onBack() {
        SceneNavigator.switchScene(backButton,"/layouts/home-screen.fxml");
    }

}
