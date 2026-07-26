/**
 * this class is going to represent one game in a users library
 */

public class Game {

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
    public Game(String title, String platform, String genre,
                double hoursPlayed, GameStatus status, String imagePath, String notes) {

        this.title = title;
        this.platform = platform;
        this.genre = genre;
        this.hoursPlayed = hoursPlayed;
        this.status = status;
        this.imagePath = imagePath;
        this.notes = notes;
    }

    // Getters ..

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
     * Returns game title
     * @return the game title
     */
    @Override
    public String toString() {
        return title;
    }
}
