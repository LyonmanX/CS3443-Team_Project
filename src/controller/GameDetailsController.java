package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import model.Game;
import util.GameImages;
import util.SceneNavigator;

/**
 * This class is going to controll the game details screen
 */
public class GameDetailsController {

    @FXML private Label titleLabel;

    @FXML private Label platformLabel;

    @FXML private Label genreLabel;

    @FXML private Label hoursPlayedLabel;

    @FXML private Label statusLabel;

    @FXML private Label descriptionLabel;

    @FXML private ImageView gameImageView;

    @FXML private Button gameLibraryButton;

    @FXML private Button timerButton;

    @FXML private Button editButton;

    private Game selectedGame;

    @FXML
    public void initialize() {
        titleLabel.setText("No game selected");
        platformLabel.setText("-");
        genreLabel.setText("-");
        hoursPlayedLabel.setText("0");
        statusLabel.setText("-");
        descriptionLabel.setText("");
        gameImageView.setImage(null);
    }

    /**
     * Displays the selected game from the game library
     *
     * @param game selected game
     */

    public void setSelectedGame(Game game) {

        selectedGame = game;

        if (game == null) {
            clearGameInformation();
            return;
        }

        titleLabel.setText(displayText(game.getTitle()));
        platformLabel.setText(displayText(game.getPlatform()));
        genreLabel.setText(displayText(game.getGenre()));
        displayHoursPlayed(game.getHoursPlayed());

        if (game.getStatus() == null) {
            statusLabel.setText("-");
        } else {
            statusLabel.setText(game.getStatus().toString());
        }

        if (game.getNotes() == null || game.getNotes().isBlank()) {
            descriptionLabel.setText("");
        } else {
            descriptionLabel.setText(game.getNotes());
        }

        displayGameImage(game.getImagePath());
    }

    /**
     * Displays the hours played on a game
     *
     * @param hoursPlayed total hours played
     */

    private void displayHoursPlayed(double hoursPlayed) {

        if (hoursPlayed == Math.floor(hoursPlayed)) {
            hoursPlayedLabel.setText(String.valueOf((int) hoursPlayed));
        }
        else {
            hoursPlayedLabel.setText(String.format("%.1f", hoursPlayed));
        }
}
    /**
     * Is going to display the games cover image
     *
     * @param imagePath image filename or path
     */

    private void displayGameImage(String imagePath) {
        gameImageView.setImage(GameImages.resolve(imagePath));
    }

    /**
     * Clears the displayed game inforamtion
     */

    private void clearGameInformation() {
        titleLabel.setText("No Game Selected");
        platformLabel.setText("-");
        genreLabel.setText("-");
        hoursPlayedLabel.setText("0");
        statusLabel.setText("-");
        descriptionLabel.setText("");
        gameImageView.setImage(null);
    }

    /**
     * Removes blank text from being displayed
     */

    private String displayText(String value) {

        if (value == null || value.isBlank()) {
            return "-";
        }
        return value;
    }

    /**
     *  opens the game library screen
     */

    @FXML
    private void openGameLibrary(){
        SceneNavigator.switchScene(gameLibraryButton, "/resources/layouts/game-library.fxml");
    }

    /**
     * Opens the Edit Game screen for the currently selected game
     */

    @FXML
    private void openEdit() {

        if (selectedGame == null) {
            return;
        }

        EditGameController controller = SceneNavigator.switchScene(
                editButton, "/resources/layouts/editgame.fxml");

        if (controller != null) {
            controller.setGame(selectedGame);
        }
    }

    /**
     * Opens Timer screen
     */

    @FXML
    private void openTimer() {

        if (selectedGame == null) {
            return;
        }
    }

    /**
     * Returns the selected game
     */

    public Game getSelectedGame() {
        return selectedGame;
    }
}
