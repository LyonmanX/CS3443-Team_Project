package util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Small helper used by controllers to swap the root of the current window for
 * another FXML screen, without every controller re-writing the same
 * FXMLLoader boilerplate.
 */
public class SceneNavigator {

    private SceneNavigator() {
    }

    /**
     * Loads the FXML at the given classpath location and puts it in the
     * current window (replacing whatever screen is showing right now).
     *
     * @param source          any node that is currently on screen (used to find the Stage)
     * @param fxmlClasspath   classpath location of the FXML, e.g. "/resources/layouts/addgame.fxml"
     * @param <T>             the controller type declared by that FXML file
     * @return the controller instance for the newly loaded screen, or null if loading failed
     */
    public static <T> T switchScene(Node source, String fxmlClasspath) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneNavigator.class.getResource(fxmlClasspath));
            Parent root = loader.load();

            Stage stage = (Stage) source.getScene().getWindow();
            stage.getScene().setRoot(root);

            return loader.getController();
        } catch (IOException exception) {
            exception.printStackTrace();
            return null;
        }
    }
}
