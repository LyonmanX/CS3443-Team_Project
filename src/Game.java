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

    public void addHoursPlayed(double hours) {
        hoursPlayed += hours;
    }

    @Override
    public String toString() {
        return title;
    }
}
