/**
*This class will represent the current status of a particular game
*/
public enum GameStatus {
  PLAYING("Playing"),
  NOT_STARTED("Not Started"),
  COMPLETED("Completed"),
  PAUSED ("Paused"),
  DROPPED("Dropped");

  private final String displayName;

  GameStatus(String displayName) {
    this.displayName = displayName;
  }

  @Override
  public String toString() {
    return displayName;
  }
}