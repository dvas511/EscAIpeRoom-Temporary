package nz.ac.auckland.se206.missions;

import nz.ac.auckland.se206.controllers.ChatController;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;

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
  public void askGpt() {
    // TODO ask gpt to generate riddle

    new ChatCompletionRequest().setN(1).setTemperature(0.7).setTopP(0.7).setMaxTokens(100);

    try {
      chatController.invokeRunGpt(
          new ChatMessage("user", GptPromptEngineering.getRiddleWithGivenWord("red")));
    } catch (ApiProxyException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
