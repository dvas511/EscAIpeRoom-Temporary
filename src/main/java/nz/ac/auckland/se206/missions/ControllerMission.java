package nz.ac.auckland.se206.missions;

import nz.ac.auckland.se206.controllers.ChatController;
import nz.ac.auckland.se206.gpt.ChatMessage;

public class ControllerMission extends Mission {

  ChatController chatController;

  public ControllerMission() {
    currentStage = 0;
    totalStage = 3;
  }

  @Override
  public void initialize() {
    // TODO Set the correct image to visible
  }

  @Override
  public String getName() {
    return "Fix the controller of the ship";
  }

  @Override
  public ChatMessage askGpt() {
    // TODO ask gpt to generate riddle
    return null;
  }

  @Override
  public void updateProgress() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'updateProgress'");
  }
  
}
