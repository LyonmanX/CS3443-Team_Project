package util;

import javafx.scene.image.Image;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * Shared logic for loading and storing game cover images.
 *
 * All cover images live in src/resources/data alongside VideoGameLibrary.csv.
 * A Game only ever stores the file name (e.g. "Halo4.jpg") in imagePath, so
 * this class is the one place that knows how to turn that file name into an
 * actual Image, and how to bring a new image into that folder.
 */
public class GameImages {

    public static final String DATA_DIR = "src/resources/data/";
    private static final String CLASSPATH_DIR = "/resources/data/";

    private GameImages() {
    }

    /**
     * Finds the real, on-disk data folder, regardless of what the app's
     * current working directory happens to be.
     * <p>
     *
     * @return the best folder to browse/read/write cover images in
     */
    public static File dataDirectory() {
        File relative = new File(DATA_DIR).getAbsoluteFile();
        if (relative.isDirectory()) {
            return relative;
        }

        URL marker = GameImages.class.getResource(CLASSPATH_DIR + "VideoGameLibrary.csv");
        if (marker != null) {
            try {
                File classpathData = new File(marker.toURI()).getParentFile();
                if (classpathData != null && classpathData.isDirectory()) {
                    return classpathData;
                }
            } catch (URISyntaxException ignored) {
                // fall through to the best-guess relative path below
            }
        }

        return relative;
    }

    /**
     * Resolves a stored image file name (or full path) into a loadable Image.
     * Returns null if there is nothing to show.
     */
    public static Image resolve(String imagePath) {
        if (imagePath == null || imagePath.isBlank()) {
            return null;
        }

        try {
            URL resource = GameImages.class.getResource(CLASSPATH_DIR + imagePath);
            if (resource != null) {
                return new Image(resource.toExternalForm());
            }

            File file = new File(imagePath);
            if (file.exists()) {
                return new Image(file.toURI().toString());
            }

            File dataFile = new File(dataDirectory(), imagePath);
            if (dataFile.exists()) {
                return new Image(dataFile.toURI().toString());
            }
        } catch (Exception exception) {
            System.out.println("Could not load image: " + imagePath);
        }

        return null;
    }

    /**
     * Makes sure the given source image file lives inside the data folder,
     * copying it in if needed (avoiding a name collision), and returns the
     * file name that should be stored on the Game.
     */
    public static String importImage(File source) {
        if (source == null) {
            return null;
        }

        try {
            Path dataDir = dataDirectory().toPath();
            Files.createDirectories(dataDir);

            Path sourcePath = source.toPath().toAbsolutePath();
            Path dataDirAbs = dataDir.toAbsolutePath();

            if (sourcePath.startsWith(dataDirAbs)) {
                // Already inside the data folder, just use its file name.
                return source.getName();
            }

            String fileName = uniqueFileName(dataDir, source.getName());
            Path destination = dataDir.resolve(fileName);
            Files.copy(sourcePath, destination, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException exception) {
            exception.printStackTrace();
            return source.getName();
        }
    }

    private static String uniqueFileName(Path dataDir, String desiredName) {
        Path candidate = dataDir.resolve(desiredName);
        if (!Files.exists(candidate)) {
            return desiredName;
        }

        String base = desiredName;
        String extension = "";
        int dot = desiredName.lastIndexOf('.');
        if (dot > 0) {
            base = desiredName.substring(0, dot);
            extension = desiredName.substring(dot);
        }

        int counter = 1;
        String newName;
        do {
            newName = base + "_" + counter + extension;
            counter++;
        } while (Files.exists(dataDir.resolve(newName)));

        return newName;
    }
}

