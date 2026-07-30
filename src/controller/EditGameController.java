package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import model.Game;
import model.GameLibrary;
import model.GameStatus;
import util.GameImages;
import util.SceneNavigator;

import java.io.File;
import java.util.Optional;

/**
 * Controls the Edit Game screen: pre-fills the form with an existing game's
 * details and lets the user update or delete that game.
 */
public class EditGameController {

    @FXML private TextField titleField;
    @FXML private ComboBox<String> platformCombo;
    @FXML private ComboBox<String> genreCombo;
    @FXML private TextField hoursField;
    @FXML private ComboBox<GameStatus> statusCombo;
    @FXML private TextArea descriptionField;
    @FXML private ImageView previewImage;
    @FXML private Label imageFileLabel;
    @FXML private Button chooseImageButton;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;
    @FXML private Button gameLibraryButton;
    @FXML private Button cancelTopButton;
    @FXML private Label errorLabel;

    private final GameLibrary library = GameLibrary.getInstance();

    private Game game;
    private String selectedImageFile;

    @FXML
    public void initialize() {
        platformCombo.setItems(FXCollections.observableArrayList(
                "PC", "PlayStation", "Xbox", "Switch", "Mobile"));

        genreCombo.setItems(FXCollections.observableArrayList(
                "Action", "Adventure", "RPG", "FPS", "Battle Royale", "Sports", "Puzzle", "Strategy"));

        statusCombo.setItems(FXCollections.observableArrayList(GameStatus.values()));
    }

    /**
     * Loads an existing game's details into the form. Must be called right
     * after this screen is loaded.
     *
     * @param game the game to edit
     */
    public void setGame(Game game) {
        this.game = game;

        if (game == null) {
            return;
        }

        titleField.setText(game.getTitle());
        platformCombo.getEditor().setText(game.getPlatform() == null ? "" : game.getPlatform());
        genreCombo.getEditor().setText(game.getGenre() == null ? "" : game.getGenre());
        hoursField.setText(formatHours(game.getHoursPlayed()));
        statusCombo.setValue(game.getStatus());
        descriptionField.setText(game.getNotes() == null ? "" : game.getNotes());

        selectedImageFile = game.getImagePath();
        imageFileLabel.setText(selectedImageFile == null || selectedImageFile.isBlank()
                ? "No image selected" : selectedImageFile);
        previewImage.setImage(GameImages.resolve(selectedImageFile));
    }

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
    private void onUpdate() {
        if (game == null) {
            return;
        }

        String title = titleField.getText() == null ? "" : titleField.getText().trim();
        if (title.isEmpty()) {
            showError("Please enter a title.");
            return;
        }

        game.setTitle(title);
        game.setPlatform(comboValue(platformCombo));
        game.setGenre(comboValue(genreCombo));
        game.setHoursPlayed(parseHours(hoursField.getText()));
        game.setStatus(statusCombo.getValue());
        game.setImagePath(selectedImageFile);
        game.setNotes(descriptionField.getText() == null ? "" : descriptionField.getText().trim());

        library.updateGame(game);

        GameDetailsController controller = SceneNavigator.switchScene(
                updateButton, "/resources/layouts/game-details.fxml");
        if (controller != null) {
            controller.setSelectedGame(game);
        }
    }

    @FXML
    private void onDelete() {
        if (game == null) {
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete \"" + game.getTitle() + "\" from your library?", ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText(null);
        confirm.setTitle("Delete Game");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            library.removeGame(game.getId());
            SceneNavigator.switchScene(deleteButton, "/resources/layouts/game-library.fxml");
        }
    }

    @FXML
    private void onCancel() {
        GameDetailsController controller = SceneNavigator.switchScene(
                cancelTopButton, "/resources/layouts/game-details.fxml");
        if (controller != null) {
            controller.setSelectedGame(game);
        }
    }

    @FXML
    private void onGameLibrary() {
        SceneNavigator.switchScene(gameLibraryButton, "/resources/layouts/game-library.fxml");
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

    private String formatHours(double hours) {
        if (hours == Math.floor(hours)) {
            return String.valueOf((int) hours);
        }
        return String.format("%.1f", hours);
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
