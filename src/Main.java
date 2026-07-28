import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/layouts/home-screen.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        primaryStage.setTitle("Screen Report");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

//OLD MAIN FILE
/*
import java.util.Scanner; // Built-in Java class for reading user input.

public class Main {

    // One Scanner object for reading user input from the terminal.
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        // Create the GameLibrary object.
        // This object will manage all games, editing, adding, selecting, etc.
        GameLibrary library = new GameLibrary();

        boolean running = true; // Controls the main loop.

        // MAIN LOOP — this is your "Home Screen"
        while (running) {
            showHomeScreen(library);

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    // Go to the Game Library screen.
                    // All logic for adding/editing/selecting games happens inside GameLibrary.
                    library.openLibraryMenu();
                    break;

                case "2":
                    System.out.println("Exiting Screen Report...");
                    running = false;
                    break;

                default:
                    System.out.println("Invalid option. Please choose again.");
            }
        }
    }

    // ---------------- HOME SCREEN ----------------
    private static void showHomeScreen(GameLibrary library) {
        System.out.println("\n=== HOME SCREEN ===");

        // These methods will be implemented inside GameLibrary.
        System.out.println("Total Games: " + library.getTotalGames());
        System.out.println("Total Hours Played: " + library.getTotalHoursPlayed());

        System.out.println("\n1. Game Library");
        System.out.println("2. Exit");
        System.out.print("Choose an option: ");
    }
}
 */