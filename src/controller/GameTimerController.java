package controller;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import model.Game;
import model.GameLibrary;
import model.GameStatus;
import util.SceneNavigator;

/**
 * Controls the game timer screen.
 * Tracks a play session and adds the elapsed time to the selected game.
 */
public class GameTimerController {

    @FXML private Label gameTitleLabel;
    @FXML private Label timerLabel;

    @FXML private StackPane timerCircle;

    @FXML private Button startPauseButton;
    @FXML private Button resetButton;
    @FXML private Button gameLibraryButton;

    private final GameLibrary library = GameLibrary.getInstance();

    private Game selectedGame;

    private AnimationTimer timer;

    private boolean running;

    /*
     * Total time accumulated before the timer was most recently started.
     */
    private long accumulatedNanoseconds;

    /*
     * The nanoTime value recorded when the timer was started.
     */
    private long startNanoseconds;

    @FXML
    public void initialize() {
        timerLabel.setText("00:00.00");
        startPauseButton.setText("Start");

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (running) {
                    long elapsed = accumulatedNanoseconds
                            + (now - startNanoseconds);

                    displayTime(elapsed);
                }
            }
        };

        timer.start();
    }

    /**
     * Receives the selected game from the Game Details screen.
     *
     * @param game game being timed
     */
    public void setGame(Game game) {
        selectedGame = game;

        if (game == null) {
            gameTitleLabel.setText("No Game Selected");
        } else {
            gameTitleLabel.setText(game.getTitle());
        }
    }

    /**
     * Starts or pauses the timer.
     */
    @FXML
    private void onStartPause() {
        if (selectedGame == null) {
            return;
        }

        if (running) {
            pauseTimer();
        } else {
            startTimer();
        }
    }

    /**
     * Allows the circular timer area to also start or pause the timer.
     */
    @FXML
    private void onTimerCircleClicked() {
        onStartPause();
    }

    /**
     * Starts or resumes the timer.
     */
    private void startTimer() {
        startNanoseconds = System.nanoTime();
        running = true;
        startPauseButton.setText("Pause");
    }

    /**
     * Pauses the timer and stores the current elapsed time.
     */
    private void pauseTimer() {
        accumulatedNanoseconds += System.nanoTime() - startNanoseconds;
        running = false;

        displayTime(accumulatedNanoseconds);
        startPauseButton.setText("Resume");
    }

    /**
     * Resets the current session without changing the game's saved hours.
     */
    @FXML
    private void onReset() {
        running = false;
        accumulatedNanoseconds = 0;
        startNanoseconds = 0;

        timerLabel.setText("00:00.00");
        startPauseButton.setText("Start");
    }

    /**
     * Saves the session time and returns to the game details screen.
     */
    @FXML
    private void onFinishSession() {
        saveSession();

        GameDetailsController controller = SceneNavigator.switchScene(
                startPauseButton,
                "/resources/layouts/game-details.fxml");

        if (controller != null) {
            controller.setSelectedGame(selectedGame);
        }
    }

    /**
     * Saves the current timer value into the selected game's hours played.
     */
    private void saveSession() {
        if (selectedGame == null) {
            return;
        }

        if (running) {
            accumulatedNanoseconds += System.nanoTime() - startNanoseconds;
            running = false;
        }

        if (accumulatedNanoseconds <= 0) {
            return;
        }

        double sessionHours =
                accumulatedNanoseconds / 1_000_000_000.0 / 60.0 / 60.0;

        selectedGame.addHoursPlayed(sessionHours);
        selectedGame.setStatus(GameStatus.PLAYING);

        library.updateGame(selectedGame);

        accumulatedNanoseconds = 0;
        startNanoseconds = 0;
    }

    /**
     * Saves the session and opens the game library.
     */
    @FXML
    private void onGameLibrary() {
        saveSession();

        SceneNavigator.switchScene(
                gameLibraryButton,
                "/resources/layouts/game-library.fxml");
    }

    /**
     * Displays time using MM:SS.hh.
     *
     * Example: 01:23.34 means 1 minute, 23 seconds,
     * and 34 hundredths of a second.
     *
     * @param nanoseconds elapsed timer time
     */
    private void displayTime(long nanoseconds) {
        long totalHundredths = nanoseconds / 10_000_000;

        long hundredths = totalHundredths % 100;
        long totalSeconds = totalHundredths / 100;

        long seconds = totalSeconds % 60;
        long totalMinutes = totalSeconds / 60;

        timerLabel.setText(String.format(
                "%02d:%02d.%02d",
                totalMinutes,
                seconds,
                hundredths));
    }

    /**
     * Returns the currently selected game.
     *
     * @return selected game
     */
    public Game getSelectedGame() {
        return selectedGame;
    }
}
