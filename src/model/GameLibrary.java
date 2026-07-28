package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * GameLibrary manages the collection of Game objects and CSV persistence.
 * It is implemented as a thread-safe singleton and uses a CSV file named
 * "VideoGameLibrary.csv" in the data folder by default.
 *
 * CSV schema expected (per row):
 * id,title,platform,genre,hoursPlayed,status,imageFile,notes
 */
public class GameLibrary {

    private static final String DEFAULT_CSV = "data/VideoGameLibrary.csv";
    private static volatile GameLibrary instance;

    private final List<Game> games;
    private final String csvPath;

    private GameLibrary(String csvPath) {
        this.csvPath = csvPath == null || csvPath.isEmpty() ? DEFAULT_CSV : csvPath;
        this.games = Collections.synchronizedList(new ArrayList<>());
    }

    /**
     * Returns the singleton instance. If not created, creates with default CSV path.
     * If csvPath is provided on first call, it will be used; subsequent calls ignore csvPath.
     *
     * @param csvPath optional CSV path to use for the singleton instance
     * @return singleton GameLibrary instance
     */
    public static GameLibrary getInstance(String csvPath) {
        if (instance == null) {
            synchronized (GameLibrary.class) {
                if (instance == null) {
                    instance = new GameLibrary(csvPath);
                }
            }
        }
        return instance;
    }

    /**
     * Convenience accessor for default instance.
     *
     * @return singleton GameLibrary instance using default CSV path
     */
    public static GameLibrary getInstance() {
        return getInstance(DEFAULT_CSV);
    }

    /**
     * Loads games from the CSV file. Existing in-memory list is cleared and replaced.
     * The loader tolerates an optional header line that starts with "id" or "title".
     *
     * @throws IOException if an I/O error occurs while reading the file
     */
    public void loadFromCSV() throws IOException {
        Path path = Path.of(csvPath);
        File file = path.toFile();

        if (!file.exists()) {
            // Ensure parent directories exist and create an empty file
            Files.createDirectories(path.getParent());
            file.createNewFile();
            synchronized (games) {
                games.clear();
            }
            return;
        }

        List<Game> loaded = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                // Skip header if present
                if (firstLine) {
                    String lower = line.toLowerCase();
                    if (lower.startsWith("id,") || lower.startsWith("title,")) {
                        firstLine = false;
                        continue;
                    }
                }
                firstLine = false;

                // Split into columns; sanitize in Game ensures commas inside fields were replaced
                String[] cols = line.split(",", -1);
                if (cols.length < 8) {
                    // skip malformed lines
                    continue;
                }

                String id = cols[0].trim();
                String title = cols[1].trim();
                String platform = cols[2].trim();
                String genre = cols[3].trim();

                double hours = 0.0;
                try {
                    hours = Double.parseDouble(cols[4].trim());
                } catch (NumberFormatException ignored) {
                }

                GameStatus status = null;
                String statusToken = cols[5].trim();
                if (!statusToken.isEmpty()) {
                    try {
                        status = GameStatus.valueOf(statusToken);
                    } catch (IllegalArgumentException ignored) {
                        // tolerate unknown status
                        status = null;
                    }
                }

                String imageFile = cols[6].trim();
                String notes = cols[7].trim();

                Game g = new Game(id, title, platform, genre, hours, status, imageFile, notes);
                if (id == null || id.isEmpty()) {
                    g.setId(UUID.randomUUID().toString());
                } else {
                    g.setId(id);
                }
                loaded.add(g);
            }
        }

        synchronized (games) {
            games.clear();
            games.addAll(loaded);
        }
    }

    /**
     * Saves the current games list to CSV. Overwrites the file.
     * Writes a header line first for readability.
     */
    public void saveToCSV() {
        Path path = Path.of(csvPath);
        File file = path.toFile();
        try {
            Files.createDirectories(path.getParent());
        } catch (IOException ignored) {
        }

        synchronized (games) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, false))) {
                // Header
                bw.write("id,title,platform,genre,hoursPlayed,status,imageFile,notes");
                bw.newLine();
                for (Game g : games) {
                    // Use Game.toCSv() as implemented in the Game class
                    bw.write(g.toCSv());
                    bw.newLine();
                }
            } catch (IOException e) {
                // Replace with proper logging in production
                e.printStackTrace();
            }
        }
    }

    /**
     * Adds a new game. If the game has no id, assigns a UUID. Persists after add.
     *
     * @param game the Game to add
     */
    public void addGame(Game game) {
        if (game == null) return;
        synchronized (games) {
            if (game.getId() == null || game.getId().isEmpty()) {
                game.setId(UUID.randomUUID().toString());
            }
            games.add(game);
        }
        saveToCSV();
    }

    /**
     * Removes a game by id. Returns true if removed. Persists after removal.
     *
     * @param id the id of the game to remove
     * @return true if a game was removed
     */
    public boolean removeGame(String id) {
        if (id == null) return false;
        boolean removed = false;
        synchronized (games) {
            Optional<Game> opt = games.stream().filter(g -> id.equals(g.getId())).findFirst();
            if (opt.isPresent()) {
                removed = games.remove(opt.get());
            }
        }
        if (removed) saveToCSV();
        return removed;
    }

    /**
     * Finds a game by id. Returns null if not found.
     *
     * @param id the game id
     * @return Game instance or null
     */
    public Game findGame(String id) {
        if (id == null) return null;
        synchronized (games) {
            return games.stream().filter(g -> id.equals(g.getId())).findFirst().orElse(null);
        }
    }

    /**
     * Replaces an existing game with the provided updated instance (matching by id).
     * If no existing game is found, the updated game is added. Persists after update.
     *
     * @param updated the updated Game instance
     */
    public void updateGame(Game updated) {
        if (updated == null) return;
        synchronized (games) {
            for (int i = 0; i < games.size(); i++) {
                Game current = games.get(i);
                if (updated.getId() != null && updated.getId().equals(current.getId())) {
                    games.set(i, updated);
                    saveToCSV();
                    return;
                }
            }
            // not found, add as new
            if (updated.getId() == null || updated.getId().isEmpty()) {
                updated.setId(UUID.randomUUID().toString());
            }
            games.add(updated);
        }
        saveToCSV();
    }

    /**
     * Returns an immutable snapshot of the current games list for read-only use by controllers.
     *
     * @return list of games
     */
    public List<Game> getAllGames() {
        synchronized (games) {
            return games.stream().collect(Collectors.toList());
        }
    }

    /**
     * Returns the number of games in the library.
     *
     * @return total games
     */
    public int getTotalGames() {
        synchronized (games) {
            return games.size();
        }
    }

    /**
     * Returns the sum of hoursPlayed across all games.
     *
     * @return total hours
     */
    public double getTotalHours() {
        synchronized (games) {
            return games.stream().mapToDouble(Game::getHoursPlayed).sum();
        }
    }

    /**
     * Validates that the referenced image file exists relative to a base folder.
     * Returns true if the file exists, false otherwise.
     *
     * @param imagePath path to image file
     * @return true if exists
     */
    public boolean imageExists(String imagePath) {
        if (imagePath == null || imagePath.isEmpty()) return false;
        File f = new File(imagePath);
        return f.exists() && f.isFile();
    }

    /**
     * Clears the in-memory library and deletes the CSV file on disk.
     * Use with caution.
     *
     * @throws IOException if deletion fails
     */
    public void clearAndDeleteStorage() throws IOException {
        synchronized (games) {
            games.clear();
        }
        Files.deleteIfExists(Path.of(csvPath));
    }
}