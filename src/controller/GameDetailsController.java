package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Game;

import java.io.File;
import java.net.URL;

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

        gameImageView.setImage(null);

        if(imagePath == null || imagePath.isBlank()) {
            return;
        }

        try {
            URL resourceImage = getClass().getResource("/images/" + imagePath);

            if (resourceImage != null) {
                gameImageView.setImage(new Image(resourceImage.toExternalForm()));
                return;
            }

            File imageFile = new File(imagePath);

            if (imageFile.exists()) {
                gameImageView.setImage(new Image(imageFile.toURI().toString()));
            }
        }
        catch (Exception execption) {
            System.out.println("Could not load image: " + imagePath);
        }
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
