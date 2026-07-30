package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import model.Game;
import model.GameLibrary;
import model.GameStatus;
import util.GameImages;
import util.SceneNavigator;

import java.io.File;
import java.util.UUID;

/**
 * Controls the Add Game screen: collects the details for a new game and
 * saves it into the shared GameLibrary.
 */
public class AddGameController {

    @FXML private TextField titleField;
    @FXML private ComboBox<String> platformCombo;
    @FXML private ComboBox<String> genreCombo;
    @FXML private TextField hoursField;
    @FXML private ImageView previewImage;
    @FXML private Label imageFileLabel;
    @FXML private Button chooseImageButton;
    @FXML private Button doneButton;
    @FXML private Button cancelTopButton;
    @FXML private Label errorLabel;

    private final GameLibrary library = GameLibrary.getInstance();

    private String selectedImageFile;

    @FXML
    public void initialize() {
        platformCombo.setItems(FXCollections.observableArrayList(
                "PC", "PlayStation", "Xbox", "Switch", "Mobile"));

        genreCombo.setItems(FXCollections.observableArrayList(
                "Action", "Adventure", "RPG", "FPS", "Battle Royale", "Sports", "Puzzle", "Strategy"));

        previewImage.setImage(GameImages.resolve(GameImages.PLACEHOLDER_IMAGE));
        imageFileLabel.setText("No image selected");
    }

    /**
     * Lets the user pick a cover image, defaulting to the shared data folder
     * so they can reuse one of the existing cover images already there.
     */
    @FXML
    private void onChooseImage() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Choose Cover Image");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif"));

        File dataDir = GameImages.dataDirectory();
        if (dataDir.isDirectory()) {
            chooser.setInitialDirectory(dataDir);
        }

        File chosen = chooser.showOpenDialog(chooseImageButton.getScene().getWindow());
        if (chosen == null) {
            return;
        }

        selectedImageFile = GameImages.importImage(chosen);
        imageFileLabel.setText(selectedImageFile);
        previewImage.setImage(GameImages.resolve(selectedImageFile));
    }

    @FXML
    private void onDone() {
        String title = titleField.getText() == null ? "" : titleField.getText().trim();

        if (title.isEmpty()) {
            showError("Please enter a title.");
            return;
        }

        double hours = parseHours(hoursField.getText());

        String platform = comboValue(platformCombo);
        String genre = comboValue(genreCombo);

        GameStatus status = hours > 0 ? GameStatus.PLAYING : GameStatus.NOT_STARTED;

        String imagePath = selectedImageFile == null ? GameImages.PLACEHOLDER_IMAGE : selectedImageFile;

        Game game = new Game(UUID.randomUUID().toString(), title, platform, genre,
                hours, status, imagePath, "");

        library.addGame(game);

        SceneNavigator.switchScene(doneButton, "/resources/layouts/game-library.fxml");
    }

    @FXML
    private void onCancel() {
        SceneNavigator.switchScene(cancelTopButton, "/resources/layouts/game-library.fxml");
    }

    private double parseHours(String text) {
        if (text == null || text.isBlank()) {
            return 0.0;
        }
        try {
            return Double.parseDouble(text.trim());
        } catch (NumberFormatException exception) {
            return 0.0;
        }
    }

    private String comboValue(ComboBox<String> combo) {
        String value = combo.getEditor().getText();
        return value == null ? "" : value.trim();
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }
}
