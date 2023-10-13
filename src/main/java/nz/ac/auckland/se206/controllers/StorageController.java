package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.MissionManager.MISSION;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppPanel;
import nz.ac.auckland.se206.TreeAvatar;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult.Choice;

public class StorageController {

  @FXML private ImageView progressButton;
  @FXML private ImageView storageDoor;
  @FXML private ImageView hiddenChestImage;
  // @FXML private ImageView chest;
  @FXML private ImageView blueprint;
  @FXML private ImageView miniTree;
  @FXML private ImageView root1;
  @FXML private ImageView root2;
  @FXML private ImageView root3;
  @FXML private ImageView root4;
  @FXML private Label counter;

  @FXML private ImageView controller;
  @FXML private ImageView windowController;
  @FXML private ImageView controller2;
  @FXML private ImageView thruster;
  @FXML private ImageView windowThruster;

  @FXML private Polygon processMachine;
  @FXML private Polygon bridgeDoor;
  @FXML private Polygon chest;
  @FXML private Polygon glass;

  @FXML private Rectangle collectedRectangle;
  @FXML private Label collectedLabel;
  @FXML private ImageView collectedImgBluePrint;
  @FXML private ImageView collectedImgWindow;

  private ChatMessage gptMessage;
  private boolean passwordGenerate = false;

  public void goInside() {
    App.setUi(AppPanel.MAIN_ROOM);
  }

  public void goProgress() {
    SceneManager.setPrevious(AppPanel.STORAGE);
    App.setUi(AppPanel.PROGRESS);
  }

  public void goToChest() {
    // if (passwordGenerate || !GameState.firstRiddleSolved) {
    //   App.setUi(AppPanel.CHEST);
    //   return;
    // }
    if (GameState.isFirstMissionCompleted == true) {
      GameState.generatePassWord();
      System.out.println(GameState.passWord);
      // SceneManager.showDialog("Info", "+", "What does this mean?");
      // when the user goes to the chest for the first time, the user sees the tree begin flashing
      // BRIGHTLY. At this time, a new gpt prompt will be created with a numerical puzzle and the
      // user
      // will be prompted with intro text while they wait for the tree to stop flashing.

      Task<Void> riddleSecondCall =
          new Task<Void>() {

            @Override
            protected Void call() throws Exception {

              System.out.println("this code is working");
              gptMessage =
                  runGpt(
                      new ChatMessage(
                          "user", GptPromptEngineering.getControllerPuzzle(GameState.passWord)));
              Platform.runLater(() -> appendChatMessage(gptMessage));

              System.out.println(gptMessage.getContent());

              return null;
            }
          };

      Thread secondRiddleThread = new Thread(riddleSecondCall);
      secondRiddleThread.start();
      passwordGenerate = true;
      TreeAvatar.treeFlash.play();
      App.setUi(AppPanel.CHEST);
    }
  }

  public void collectBlueprint() {
    collectedLabel.setText("BluePint Collected");
    activateCollectedInfoBluePrint();
    blueprint.setVisible(false);
    SceneManager.getPanel(AppPanel.THRUSTER).lookup("#blueprint").setVisible(true);
    // 1: purple    2: blue     3: red    4: green
    GameState.missionManager.getMission(MISSION.THRUSTER).increaseStage();
    GameState.missionManager.getMission(MISSION.THRUSTER).increaseStage();
    System.out.println(GameState.missionManager.getMission(MISSION.THRUSTER).getStage());
    GameState.progressBarGroup.updateProgressTwo(MISSION.THRUSTER);
  }

  public void meltSand() { // furnace button when the mission is the window.
    if (GameState.inventory.contains(2)) { // checks if the inventory contains sand
      GameState.missionManager.getMission(MISSION.WINDOW).increaseStage();
      GameState.progressBarGroup.updateProgressOne(MISSION.WINDOW);
      processMachine.setVisible(false);
      processMachine.setDisable(true);
      showGlass();
      SceneManager.showDialog("Info", "Glass collected", "A well-made window");
    } else if (!GameState.inventory.contains(2) && GameState.missionList.contains(1)) { // if the
      // inventory
      // does not
      // contain
      // sand and
      // the mission
      // is the
      // window
      SceneManager.showDialog("Info", "Furnace", "You do not need to use the furnace yet!");
    } else {
      SceneManager.showDialog("Info", "Furnace", "You do not need to use the furnace!");
    }
  }

  /** Show the glass image according to the mission selected */
  private void showGlass() {
    if (GameState.missionList.contains(3)) {
      // If the second mission is controller mission
      windowController.setVisible(true);
    } else {
      // If the second mission is thruster mission
      windowThruster.setVisible(true);
    }
    // Activate the collision box for glass
    glass.setVisible(true);
    glass.setDisable(false);
  }

  public void collectGlass() {
    collectedLabel.setText("Glass Collected");
    activateCollectedInfoWindow();
    GameState.inventory.add(3);
    glass.setVisible(false);
    glass.setDisable(true);
    // Show the glass collected image
    if (GameState.missionList.contains(3)) {
      // If the second mission is controller mission
      windowController.setVisible(false);
    } else {
      // If the second mission is thruster mission
      windowThruster.setVisible(false);
    }
  }

  public void activateProgressGlow() {
    progressButton.setEffect(GameState.glowBright);
  }

  public void deactivateProgressGlow() {
    progressButton.setEffect(GameState.glowDim);
  }

  public void activateDoorGlow() {
    storageDoor.setEffect(GameState.glowBright);
    bridgeDoor.setOpacity(1);
  }

  public void deactivateDoorGlow() {
    storageDoor.setEffect(GameState.glowDim);
    bridgeDoor.setOpacity(0);
  }

  public void activateHiddenChestGlow() {
    hiddenChestImage.setEffect(GameState.glowBright);
  }

  public void deactivateHiddenChestGlow() {
    hiddenChestImage.setEffect(GameState.glowDim);
  }

  public void activateChestGlow() {
    chest.setOpacity(1);
  }

  public void deactivateChestGlow() {
    chest.setOpacity(0);
  }

  public void activateProcessMachineGlow() {
    processMachine.setOpacity(1);
  }

  public void deactivateProcessMachineGlow() {
    processMachine.setOpacity(0);
  }

  public void activateGlassGlow() {
    glass.setOpacity(1);
  }

  public void deactivateGlassGlow() {
    glass.setOpacity(0);
  }

  public void goChat() {
    TreeAvatar.treeFlash.pause();
    TreeAvatar.deactivateTreeGlow();
    SceneManager.setPrevious(AppPanel.STORAGE);

    App.setUi(AppPanel.CHAT);
  }

  public void miniTreeGlow() {
    miniTree.setEffect(GameState.glowBright);
  }

  public void miniTreeDim() {
    miniTree.setEffect(GameState.glowDim);
  }

  private void activateCollectedInfoBluePrint() {
    collectedLabel.setVisible(true);
    collectedImgBluePrint.setVisible(true);
    collectedRectangle.setVisible(true);
  }

  private void activateCollectedInfoWindow() {
    collectedLabel.setVisible(true);
    collectedImgWindow.setVisible(true);
    collectedRectangle.setVisible(true);
  }

  public void exitInfo() {
    collectedRectangle.setVisible(false);
    collectedLabel.setVisible(false);
    collectedImgBluePrint.setVisible(false);
    collectedImgWindow.setVisible(false);
  }

  /* ======================================= GPT Helper Methods ======================================= */
  private ChatMessage runGpt(ChatMessage msg) throws ApiProxyException {
    ChatController.chatCompletionRequest.addMessage(msg);
    try {
      ChatCompletionResult chatCompletionResult = ChatController.chatCompletionRequest.execute();
      Choice result = chatCompletionResult.getChoices().iterator().next();
      ChatController.chatCompletionRequest.addMessage(result.getChatMessage());
      result.getChatMessage().setRole("Wise Mystical Tree");
      result.getChatMessage().setRole("assistant");
      return result.getChatMessage();
    } catch (ApiProxyException e) {
      ChatMessage error = new ChatMessage(null, null);

      error.setRole("Wise Mystical Tree");

      error.setContent(
          "Sorry, there was a problem generating a response. Please try restarting the"
              + " application.");
      appendChatMessage(error);
      e.printStackTrace();
      return null;
    }
  }

  private void appendChatMessage(ChatMessage msg) {
    // chatTextArea.appendText(msg.getRole() + ": " + msg.getContent() + "\n\n");
    ((TextArea) SceneManager.getPanel(AppPanel.CHAT).lookup("#chatTextArea"))
        .appendText("\n\n" + "Wise Ancient Tree: " + msg.getContent() + "\n\n");
    ((Label) SceneManager.getPanel(AppPanel.CHAT).lookup("#chatLabel")).setText(msg.getContent());
  }
}
