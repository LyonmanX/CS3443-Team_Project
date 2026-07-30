package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import model.Game;
import model.GameLibrary;
import util.GameImages;
import util.SceneNavigator;

import java.io.IOException;
import java.util.List;

/**
 * Controls the Game Library screen: shows every saved game as a cover-art
 * cell and handles navigating to Add Game or a game's details.
 * <p>
 * Reads through the shared {@link GameLibrary} singleton so that games
 * added/edited/removed on other screens (which all go through the same
 * singleton) are always reflected here - there is only ever one source of
 * truth for the game list.
 */
public class GameLibraryController {

    @FXML
    private FlowPane gameFlow;

    @FXML
    private Button addGameButton;

    @FXML
    private Button backButton;

    private final GameLibrary library = GameLibrary.getInstance();

    @FXML
    public void initialize() {
        try {
            library.loadFromCSV();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        displayGames(library.getAllGames());
    }

    private void displayGames(List<Game> games) {
        gameFlow.getChildren().clear();

        for (Game game : games) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/layouts/game-cell.fxml"));
                StackPane cell = loader.load();

                ImageView cover = (ImageView) cell.lookup("#coverImage");
                if (cover != null) {
                    cover.setImage(GameImages.resolve(game.getImagePath()));
                }

                cell.setStyle("-fx-cursor: hand;");
                cell.setOnMouseClicked(event -> {
                    GameDetailsController controller = SceneNavigator.switchScene(
                            cell, "/resources/layouts/game-details.fxml");
                    if (controller != null) {
                        controller.setSelectedGame(game);
                    }
                });

                gameFlow.getChildren().add(cell);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    @FXML
    private void onAddGame() {
        SceneNavigator.switchScene(addGameButton, "/resources/layouts/addgame.fxml");
    }

    @FXML
    private void onBack() {
        SceneNavigator.switchScene(backButton, "/resources/layouts/home-screen.fxml");
    }
}
