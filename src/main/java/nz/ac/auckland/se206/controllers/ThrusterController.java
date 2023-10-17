package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Polygon;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.MissionManager.MissionType;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppPanel;
import nz.ac.auckland.se206.TreeAvatar;
import nz.ac.auckland.se206.WinGame;
import nz.ac.auckland.se206.buttons.BottomLeftButton;
import nz.ac.auckland.se206.buttons.BottomRightButton;
import nz.ac.auckland.se206.buttons.TopLeftButton;
import nz.ac.auckland.se206.buttons.TopRightButton;

/**
 * The ThrusterController class controls the thruster mini-game in the Escape Room game. 
 * It handles the visibility and functionality of the four directional buttons, 
 * the repair button, and the complete button. It also manages the visibility of the 
 * blueprint and missing parts, as well as the progress button that takes the player 
 * back to the progress screen.
 */
public class ThrusterController {

  public static int buttonActivationCounter = 0;
  public static int isGameActive = 0;

  @FXML private ImageView bottomLeftUnclicked;
  @FXML private ImageView bottomLeftClicked;
  @FXML private ImageView bottomRightUnclicked;
  @FXML private ImageView bottomRightClicked;
  @FXML private ImageView topLeftUnclicked;
  @FXML private ImageView topLeftClicked;
  @FXML private ImageView topRightUnclicked;
  @FXML private ImageView topRightClicked;
  @FXML private Button returnOutside;
  @FXML private ImageView progressButton;
  @FXML private ImageView miniTree;
  @FXML private Button repairButton;
  @FXML private Button completeButton;

  @FXML private ImageView blueprintBackground;
  @FXML private ImageView complete;

  @FXML private ImageView thrusterRoot;
  @FXML private Polygon rootCollisionBox1;
  @FXML private Polygon rootCollisionBox2;
  @FXML private Polygon rootCollisionBox3;

  @FXML private ImageView missingBlueprint;
  @FXML private ImageView missingColor;
  @FXML private Label infoTitle;

  public void initialize() {}

  /**
   * Sets the bottom left button to visible if the game is active and the button's 
   * cycle number is not equal to the random color number generated by the game. 
   * If the button's cycle number is equal to the random color number, 
   * the button's timeline is paused and the button activation counter is incremented. 
   * Finally, the method checks if the game has been completed.
   */
  public void setBottomLeftVisible() { // sets the bottom left button to visible
    if (isGameActive == 1) { // checks if the game is active
      if (BottomLeftButton.getCycleNumber() != GameState.getRandomColorNumber()) {
        BottomLeftButton.timeline.play();
        bottomLeftUnclicked.setVisible(true); // sets the button to visible
      } else {
        BottomLeftButton.timeline.pause();
        buttonActivationCounter++; // increments the button activation counter
      }
    }
    checkCompletion();
  }

  public void goProgress() {
    SceneManager.setPrevious(AppPanel.THRUSTER);
    App.setUi(AppPanel.PROGRESS);
  }

  /**
   * Sets the BottomLeftButton to be invisible if the game is active.
   */
  public void setBottomLeftInvisible() {
    if (isGameActive == 1) {
      BottomLeftButton.timeline.pause();
      bottomLeftUnclicked.setVisible(false);
    }
  }

  /**
   * Sets the bottom right button to be invisible if the game is active.
   */
  public void setBottomRightInvisible() {
    if (isGameActive == 1) {
      BottomRightButton.timeline.pause();
      bottomRightUnclicked.setVisible(false);
    }
  }

  /** 
   * Sets the bottom right button to visible if the game is active and 
   * the button's cycle number is not equal to the random color number generated 
   * by the game state. If the button's cycle number is equal to the random color number, 
   * the button's timeline is paused and the button activation counter is incremented. 
   * Finally, the completion of the game is checked.
   */
  public void setBottomRightVisible() { // sets the bottom right button to visible
    if (isGameActive == 1) { // checks if the game is active
      if (BottomRightButton.getCycleNumber() != GameState.getRandomColorNumber()) {
        BottomRightButton.timeline.play();
        bottomRightUnclicked.setVisible(true); // sets the button to visible
      } else {
        BottomRightButton.timeline.pause();
        buttonActivationCounter++; // increments the button activation counter
      }
    }
    checkCompletion();
  }

  /**
   * Sets the top left button to be invisible if the game is active.
   */
  public void setTopLeftInvisible() {
    if (isGameActive == 1) {
      TopLeftButton.timeline.pause();
      topLeftUnclicked.setVisible(false);
    }
  }


  /**
   * Sets the top left button to visible if the game is active and the button's cycle number 
   * is not equal to the random color number generated by the game. 
   * If the button's cycle number is equal to the random color number, 
   * the button's timeline is paused and the button activation counter is incremented.
   * Finally, the method checks for game completion.
   */
  public void setTopLeftVisible() { // sets the top left button to visible
    if (isGameActive == 1) { // checks if the game is active
      if (TopLeftButton.getCycleNumber() != GameState.getRandomColorNumber()) {
        topLeftUnclicked.setVisible(true); // sets the button to visible
        TopLeftButton.timeline.play();
      } else {
        TopLeftButton.timeline.pause();
        buttonActivationCounter++; // increments the button activation counter
      }
    }
    checkCompletion();
  }

  /**
   * Sets the top right button to be invisible if the game is active.
   */
  public void setTopRightInvisible() {
    if (isGameActive == 1) {
      TopRightButton.timeline.pause();
      topRightUnclicked.setVisible(false);
    }
  }

  /**
   * Sets the top right button to visible if the game is active and the button's 
   * cycle number is not equal to the random color number generated by the game state. 
   * Otherwise, the button's timeline is paused and the button activation counter 
   * is incremented. Finally, the completion of the game is checked.
   */
  public void setTopRightVisible() { // sets the top right button to visible
    if (isGameActive == 1) { // checks if the game is active
      if (TopRightButton.getCycleNumber() != GameState.getRandomColorNumber()) {
        TopRightButton.timeline.play();
        topRightUnclicked.setVisible(true); // sets the button to visible
      } else {
        TopRightButton.timeline.pause();
        buttonActivationCounter++; // increments the button activation counter
      }
    }
    checkCompletion();
  }

  /**
   * Begins the thruster mini-game if the player has collected the blueprint 
   * and solved the color puzzle. If the player is missing either the blueprint 
   * or color key, an error message is displayed. Once the mini-game is activated, 
   * the repair button is hidden.
   */
  public void beginRepairs() {

    // Mission stage is set to 2 when the player has collected the blueprint and has solved the
    // color puzzle. Only once both steps have been completed can the player begin the thruster
    // mini-game

    if (GameState.missionManager.getMission(MissionType.THRUSTER).getStage() == 0) {
      infoTitle.setText("Missing blueprint");
      infoTitle.setVisible(true);
      missingBlueprint.setVisible(true);
    } else if (GameState.missionManager.getMission(MissionType.THRUSTER).getStage() == 1) {
      infoTitle.setText("Missing color key");
      infoTitle.setVisible(true);
      missingColor.setVisible(true);
    }

    if (buttonActivationCounter == 0
        && GameState.missionManager.getMission(MissionType.THRUSTER).getStage() == 2) {

      isGameActive = 1;
      BottomRightButton.timeline.setCycleCount(360);
      BottomRightButton.timeline.play();
      BottomLeftButton.timeline.setCycleCount(360);
      BottomLeftButton.timeline.play();
      TopLeftButton.timeline.setCycleCount(360);
      TopLeftButton.timeline.play();
      TopRightButton.timeline.setCycleCount(360);
      TopRightButton.timeline.play();
      repairButton.setVisible(false);
    }
  }

  public void goOutside() {
    App.setUi(AppPanel.OUTSIDE);
  }

  /**
   * Pauses the tree flash animation, deactivates the tree glow, 
   * sets the UI to the chat panel, and sets the previous scene 
   * to the thruster panel.
   */
  public void goChat() {
    TreeAvatar.treeFlash.pause();
    TreeAvatar.deactivateTreeGlow();
    App.setUi(AppPanel.CHAT);
    SceneManager.setPrevious(AppPanel.THRUSTER);
  }

  public void miniTreeGlow() {
    miniTree.setEffect(GameState.glowBright);
  }

  public void miniTreeDim() {
    miniTree.setEffect(GameState.glowDim);
  }

  public void activateProgressGlow() {
    progressButton.setEffect(GameState.glowBright);
  }

  public void deactivateProgressGlow() {
    progressButton.setEffect(GameState.glowDim);
  }

  /** 
   * Activates the root glow effect by setting the opacity of the root collision boxes to 1 
   * and changing the cursor to a hand cursor.
   */
  public void activateRootGlow() {
    rootCollisionBox1.setOpacity(1);
    rootCollisionBox1.setCursor(Cursor.HAND);
    rootCollisionBox2.setOpacity(1);
    rootCollisionBox2.setCursor(Cursor.HAND);
    rootCollisionBox3.setOpacity(1);
    rootCollisionBox3.setCursor(Cursor.HAND);
  }

  /**
   * Deactivates the root glow by setting the opacity of the root collision boxes to 0.
   */
  public void deactivateRootGlow() {
    rootCollisionBox1.setOpacity(0);
    rootCollisionBox2.setOpacity(0);
    rootCollisionBox3.setOpacity(0);
  }

  /**
   * Checks if the buttonActivationCounter is equal to 4 and calls completeRepair() if true.
   */
  public void checkCompletion() {
    if (buttonActivationCounter == 4) {
      completeRepair();
    }
  }

  /**
   * Sets the complete image to visible, increases the stage of the thruster mission, 
   * updates the progress bar, sets the win game collision box to visible, 
   * starts the win game flash, sets the thrusters to invisible and disabled, 
   * sets the outside image to visible, sets the outside broken image to invisible, 
   * and sets the complete image to visible.
   */
  public void completeRepair() {
    complete.setVisible(true); // sets the complete image to visible
    // increases the stage
    GameState.missionManager.getMission(MissionType.THRUSTER).increaseStage();
    GameState.progressBarGroup.updateProgressTwo(MissionType.THRUSTER); // updates the progress bar
    System.out.println("Thruster Mission Complete");
    SceneManager.getPanel(AppPanel.MAIN_ROOM).lookup("#winGameCollisionBox").setVisible(true);
    WinGame.startFlashWin(); // starts the win game flash
    // sets the thruster to visible and disabled
    SceneManager.getPanel(AppPanel.OUTSIDE).lookup("#thruster1").setVisible(false);
    SceneManager.getPanel(AppPanel.OUTSIDE).lookup("#thruster1").setDisable(true);
    SceneManager.getPanel(AppPanel.OUTSIDE).lookup("#thruster2").setVisible(false);
    SceneManager.getPanel(AppPanel.OUTSIDE).lookup("#thruster2").setDisable(true);
    // sets the outside image to invisible
    SceneManager.getPanel(AppPanel.OUTSIDE).lookup("#outsideImage").setVisible(true);
    // sets the outside broken image to invisible
    SceneManager.getPanel(AppPanel.OUTSIDE).lookup("#outsideBrokenImage").setVisible(false);
    // sets the complete image to visible
    SceneManager.getPanel(AppPanel.MAIN_ROOM).lookup("#completeImage").setVisible(true);
  }

  /**
   * Hides the missingBlueprint, missingColor, and infoTitle elements.
   */
  public void exitInfo() {
    missingBlueprint.setVisible(false);
    missingColor.setVisible(false);
    infoTitle.setVisible(false);
  }
}
