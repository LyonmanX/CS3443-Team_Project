package model;
import java.util.UUID;

/**
 * this class is going to represent one game in a users library
 */

public class Game {

    private String id;
    private String title;
    private String platform;
    private String genre;
    private double hoursPlayed;
    private GameStatus status;
    private String imagePath;
    private String notes;

    /**
     * Construct for a Game Object
     * @param title games title
     * @param platform is the platform a game is played on
     * @param genre the games genre
     * @param hoursPlayed total hours played on a game
     * @param status users status on a game
     * @param imagePath file path of the game cover image
     * @param notes user notes about a game
     */
    public Game(String id, String title, String platform, String genre,
                double hoursPlayed, GameStatus status, String imagePath, String notes) {

        this.id = id;
        this.title = title;
        this.platform = platform;
        this.genre = genre;
        this.hoursPlayed = hoursPlayed;
        this.status = status;
        this.imagePath = imagePath;
        this.notes = notes;
    }

    // Getters ..

    public String getId() { return id; }

    public String getTitle() {
        return title;
    }

    public String getPlatform() {
        return platform;
    }

    public String getGenre() {
        return genre;
    }

    public double getHoursPlayed() {
        return hoursPlayed;
    }

    public GameStatus getStatus() {
        return status;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getNotes() {
        return notes;
    }

    // Setters..

    public void setId(String id) { this.id = id; }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setHoursPlayed(double hoursPlayed) {
        this.hoursPlayed = hoursPlayed;
    }

    public void setStatus(GameStatus status){
        this.status = status;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Adds total playtime to the hours played
     * @param hours number of hours to add
     */
    public void addHoursPlayed(double hours) {
        hoursPlayed += hours;
    }

    /**
     * Converts this game into a single CSV row. Commas inside any field are
     * replaced with semicolons so the row always has exactly 8 columns:
     * id,title,platform,genre,hoursPlayed,status,imageFile,notes
     * @return a comma separated CSV line representing this game
     */
    public String toCSv() {
        return String.join(",",
                sanitize(id),
                sanitize(title),
                sanitize(platform),
                sanitize(genre),
                String.valueOf(hoursPlayed),
                status == null ? "" : status.name(),
                sanitize(imagePath),
                sanitize(notes));
    }

    /**
     * !!!!NOTE!!!!!
     * Probably want a way to convert CSV back to object
     */
    //public static Game fromCSV(String csvLine) {}


    /**
     * Replaces commas to prevent colomun breaking in the CSV
     */
    private static String sanitize(String line) {
        return line == null ? "" : line.replace(",", ";");
    }

    /**
     * Returns game title
     * @return the game title
     */
    @Override
    public String toString() {
        return title;
    }
}
