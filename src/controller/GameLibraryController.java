package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Button;
import util.SceneNavigator;
import model.GameStatus;
import util.GameImages;

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
            if (is == null) {
                System.out.println("CSV file not found: " + CSV_PATH);
                return games;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line = br.readLine();
            // Skip the header row.

            while ((line = br.readLine()) != null) {
                // Split the row into columns.
                String[] data = line.split(",", -1);
                if (data.length < 8) {
                    continue;
                }

                String id = data[0];
                String title = data[1];
                String platform = data[2];
                String genre = data[3];

                double hoursPlayed;
                try {
                    hoursPlayed = Double.parseDouble(data[4]);
                }
                catch (NumberFormatException exception) {
                    hoursPlayed = 0.0;
                }

                GameStatus status;
                try {
                    status = GameStatus.valueOf(data[5]);
                }
                catch (IllegalArgumentException exception) {
                    status = null;
                }
                String imageFile = data[6];
                String notes = data[7];

                model.Game fullGame = new model.Game(id, title, platform, genre, hoursPlayed,
                        status, imageFile, notes);

                games.add(new Game(fullGame));
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

                if(cover != null) {
                    cover.setImage(GameImages.resolve(game.getImageFile()));
                }

                cell.setStyle("-fx-cursor: hand;");

                cell.setOnMouseClicked(event -> {

                    GameDetailsController controller = SceneNavigator.switchScene(cell,
                            "/resources/layouts/game-details.fxml");
                    if (controller != null) {
                        controller.setSelectedGame(game.getFullGame());
                    }
                });


                // Add the completed cell to the FlowPane.
                gameFlow.getChildren().add(cell);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Simple data class representing a game entry.
    public static class Game {

        private final model.Game fullGame;

        public Game(model.Game fullGame) {
            this.fullGame = fullGame;
        }

        public String getImageFile() {
            return fullGame.getImagePath();
        }

        public model.Game getFullGame() {
            return fullGame;
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
