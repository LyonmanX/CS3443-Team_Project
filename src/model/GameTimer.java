package model;

/**
 * Represents a play session timer for a game.
 * Handles starting, pausing, resetting, and tracking elapsed play time.
 */
public class GameTimer {

    private long startTime;
    private long elapsedTime;
    private boolean running;

    /**
     * Starts or resumes the timer.
     */
    public void start() {
        if (!running) {
            startTime = System.nanoTime();
            running = true;
        }
    }

    /**
     * Pauses the timer.
     */
    public void pause() {
        if (running) {
            elapsedTime += System.nanoTime() - startTime;
            running = false;
        }
    }

    /**
     * Resets the timer to zero.
     */
    public void reset() {
        elapsedTime = 0;
        startTime = 0;
        running = false;
    }

    /**
     * Returns the elapsed time in nanoseconds.
     *
     * @return elapsed time
     */
    public long getElapsedNanoseconds() {
        if (running) {
            return elapsedTime + (System.nanoTime() - startTime);
        }
        return elapsedTime;
    }

    /**
     * Returns the elapsed time in seconds.
     *
     * @return elapsed seconds
     */
    public double getElapsedSeconds() {
        return getElapsedNanoseconds() / 1_000_000_000.0;
    }

    /**
     * Returns the elapsed time in hours.
     *
     * @return elapsed hours
     */
    public double getElapsedHours() {
        return getElapsedSeconds() / 3600.0;
    }

    /**
     * Returns whether the timer is currently running.
     *
     * @return true if running
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Returns the formatted timer string.
     *
     * Format: MM:SS.hh
     * Example: 01:23.45
     *
     * @return formatted timer
     */
    public String getFormattedTime() {

        long totalHundredths = getElapsedNanoseconds() / 10_000_000;

        long hundredths = totalHundredths % 100;
        long totalSeconds = totalHundredths / 100;

        long seconds = totalSeconds % 60;
        long minutes = totalSeconds / 60;

        return String.format("%02d:%02d.%02d",
                minutes,
                seconds,
                hundredths);
    }
}
