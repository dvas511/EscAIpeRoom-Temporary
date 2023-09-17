package nz.ac.auckland.se206.missions;

public class FuelMission implements Mission {
  private int currentStage;
  private int totalStage;

  public FuelMission() {
    currentStage = 0;
    totalStage = 3;
  }

  @Override
  public void initialize() {
    // TODO Set the correct image to visible
  }

  @Override
  public String getName() {
    return "Collect fuel for ship";
  }

  @Override
  public void increaseStage() {
    currentStage++;
    if (currentStage > totalStage) {
      System.out.println("Mission already finished, can't increase stage");
      currentStage--;
    }
  }

  @Override
  public int getStage() {
    return currentStage;
  }

  @Override
  public int getPercentage() {
    return currentStage * 100 / totalStage;
  }

  @Override
  public void askGpt() {
    // TODO ask gpt to generate riddle
    throw new UnsupportedOperationException("Unimplemented method 'askGpt'");
  }
}
