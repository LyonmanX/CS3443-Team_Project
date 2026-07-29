package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import model.GameLibrary;
import util.SceneNavigator;

import java.io.IOException;

/**
 * This class will be the controller for the Home Screen
 */

public class HomeController {

    @FXML private Label totalGamesLabel;

    @FXML private Label totalHoursLabel;

    @FXML private Button gameLibraryButton;

    private final GameLibrary library = GameLibrary.getInstance();

    /**
     * initializes home screen
     */

    @FXML public void initialize() {

        try {
            library.loadFromCSV();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        updateInformation(library.getTotalGames(), library.getTotalHours());

    }

    /**
     * Opens Game Library
     */

    @FXML private void openGameLibrary(){
        SceneNavigator.switchScene(gameLibraryButton, "/resources/layouts/game-library.fxml");
    }

    /**
     *  Update home screen information
     */

    public void updateInformation(int totalGames, double totalHours) {

        totalGamesLabel.setText(String.valueOf(totalGames));

        if(totalHours == (int) totalHours) {
            totalHoursLabel.setText(String.valueOf((int) totalHours));
        }
        else {
            totalHoursLabel.setText(String.format("%.1f", totalHours));
        }
    }
}
