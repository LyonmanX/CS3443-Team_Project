package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * This class will be the controller for the Home Screen
 */

public class HomeController {

    @FXML private Label totalGamesLabel;

    @FXML private Label totalHoursLabel;

    @FXML private Button gameLibraryButton;

    /**
     * initializes home screen
     */

    @FXML public void initialize() {

        totalGamesLabel.setText("0");
        totalHoursLabel.setText("0");

    }

    /**
     * Opens Game Library
     */

    @FXML private void openGameLibrary(){

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
