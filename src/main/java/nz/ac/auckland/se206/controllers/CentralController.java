package nz.ac.auckland.se206.controllers;

import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.MissionManager.MISSION;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppPanel;
import nz.ac.auckland.se206.TreeAvatar;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.WinGame;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Polygon;

/**
 * The CentralController class is responsible for controlling the main room of
 * the game, including the
 * progress button, inventory, and various assets such as the window, fuel tank,
 * and control panel.
 * It also handles the repair of the window and the addition of fuel to the
 * ship.
 */
public class CentralController {

  @FXML
  private Label counter;
  @FXML
  private Label guideLabel;
  @FXML
  private ImageView guideImage;
  @FXML
  private ImageView outside;
  @FXML
  private ImageView storage;
  @FXML
  private ImageView progressButton;
  @FXML
  private ImageView root1;
  @FXML
  private ImageView root2;
  @FXML
  private ImageView root3;
  @FXML
  private ImageView root4;
  @FXML
  private ImageView chest;
  @FXML
  private ImageView miniTree;
  @FXML
  private ImageView completeGame;

  @FXML
  private Polygon outsideDoor;
  @FXML
  private Polygon storageDoor;
  @FXML
  private Polygon window;
  @FXML
  private Polygon fuelTank;
  @FXML
  private Polygon controllerBroken1;
  @FXML
  private Polygon controllerBroken2;
  @FXML
  private Polygon rootOneCollisionBox1;
  @FXML
  private Polygon rootOneCollisionBox2;
  @FXML
  private Polygon rootOneCollisionBox3;
  @FXML
  private Polygon rootOneCollisionBox4;
  @FXML
  private Polygon rootTwoCollisionBox1;
  @FXML
  private Polygon rootTwoCollisionBox2;
  @FXML
  private Polygon rootTwoCollisionBox3;
  @FXML
  private Polygon rootTwoCollisionBox4;
  @FXML
  private Polygon rootThreeCollisionBox1;
  @FXML
  private Polygon rootThreeCollisionBox2;
  @FXML
  private Polygon rootThreeCollisionBox3;
  @FXML
  private Polygon rootThreeCollisionBox4;
  @FXML
  private Polygon rootThreeCollisionBox5;
  @FXML
  private Polygon rootFourCollisionBox1;
  @FXML
  private Polygon rootFourCollisionBox2;
  @FXML
  private Polygon rootFourCollisionBox3;
  @FXML
  private Polygon rootFourCollisionBox4;
  @FXML
  private Polygon rootFourCollisionBox5;
  @FXML
  private Polygon rootFourCollisionBox6;
  @FXML
  private Polygon rootFourCollisionBox7;
  @FXML
  private Polygon rootFourCollisionBox8;
  @FXML
  private Polygon rootFourCollisionBox9;

  @FXML
  private ImageView windowController1;
  @FXML
  private ImageView windowController2;
  @FXML
  private ImageView fuelController1;
  @FXML
  private ImageView fuelController2;
  @FXML
  private ImageView windowThruster1;
  @FXML
  private ImageView windowThruster2;
  @FXML
  private ImageView fuelThruster1;
  @FXML
  private ImageView fuelThruster2;
  @FXML
  private ImageView completeImage;

  @FXML
  private ImageView windowInfo;
  @FXML
  private ImageView fuelInfo;
  @FXML
  private ImageView controllerInfo;
  @FXML
  private Label infoLabel;
  @FXML
  private Label infoTitle;

  @FXML
  private Polygon winGameCollisionBox;

  public void goOutside() {
    SceneManager.setPrevious(AppPanel.MAIN_ROOM);
    App.setUi(AppPanel.OUTSIDE);
  }

  public void goProgress() {
    SceneManager.setPrevious(AppPanel.MAIN_ROOM);
    App.setUi(AppPanel.PROGRESS);
  }

  public void goStorage() {
    App.setUi(AppPanel.STORAGE);
  }

  /**
   * This method is called when the player wins the game. It sets the timer to
   * finish, plays the win sound effect,
   * and sets the UI to the win panel.
   */
  public void goWin() {
    LaunchController.timer.setFinish();
    WinController.playMedia();

    App.setUi(AppPanel.WIN);
  }

  /**
   * Repairs the window if it is in the inventory. Increases the stage of the
   * window mission and updates the progress bar. Records that the first mission
   * is completed. Starts flashing the tree. Removes the window collision box.
   * Initializes the second mission. Disables the outside and storage doors. If
   * the inventory does not contain a window, shows a broken message.
   */
  public void repairWindow() {
    // if inventory contains the necessary items, fixes the window and control panel
    // and changes
    // visibility of assets
    // If the inventory contains window
    if (GameState.inventory.contains(3)) {
      // Increase the stage of the window mission and update progress bar
      GameState.missionManager.getMission(MISSION.WINDOW).increaseStage();
      GameState.progressBarGroup.updateProgressOne(MISSION.WINDOW);
      // Record that the first mission is completed
      GameState.isFirstMissionCompleted = true;
      // Tree start flashing
      TreeAvatar.startFlashTree();
      // Remove the window collision box
      window.setOpacity(0);
      window.setDisable(true);
      // Initialise the second mission
      activateBlueprint();
      // activateChest();
      activateSecondMissionImage();
      // Show the fix window message
      outsideDoor.setDisable(true);
      storageDoor.setDisable(true);
    } else {
      // If the inventory does not contain a window, show broken message
      infoTitle.setText("Broken Window");
      infoLabel.setText("A large crack is inside the window");
      infoTitle.setVisible(true);
      infoLabel.setVisible(true);
      windowInfo.setVisible(true);
    }
  }

  /**
   * Adds fuel to the ship. If the inventory contains fuel, increase missing stage
   * and fill up the ship.
   * Record that the first mission is completed. Remove fuel from inventory. Tree
   * start flashing.
   * Hide the fuel warning. Initialise the second mission. Show success message.
   * If the inventory does not contain fuel, show error message.
   */
  public void addFuel() {
    // This method adds fuel to the ship
    if (GameState.inventory.contains(8)) {
      // If the inventory contains fuel, increase missing stage and fill up the ship
      GameState.missionManager.getMission(MISSION.FUEL).increaseStage();
      GameState.progressBarGroup.updateProgressOne(MISSION.FUEL);
      // Record that the first mission is completed
      GameState.isFirstMissionCompleted = true;
      // Remove fuel from inventory
      GameState.inventory.remove(GameState.inventory.indexOf(8));
      // Tree start flashing
      TreeAvatar.startFlashTree();
      // Hide the fuel warning
      fuelTank.setOpacity(0);
      fuelTank.setDisable(true);
      // Initialise the second mission
      activateBlueprint();
      // activateChest();
      activateSecondMissionImage();
      // Show success message
      // SceneManager.showDialog("Info", "Fuel added", "Mission accomplished");
      outsideDoor.setDisable(true);
      storageDoor.setDisable(true);
    } else {
      // If the inventory does not contain fuel, show error message
      infoTitle.setText("No Fuel");
      infoLabel.setText("Internal fuel tank is empty");
      infoTitle.setVisible(true);
      infoLabel.setVisible(true);
      fuelInfo.setVisible(true);
    }
  }

  /**
   * This method attempts to fix the controller. If the controller mission is less
   * than stage 1 (not having spare part on hand),
   * it shows a broken controller message. If the controller mission is at stage
   * 1, indicating the panel can be fixed, it shows a
   * message indicating the controller is fixed, increases the stage, updates the
   * progress bar, sets the end game button visible,
   * starts flashing the win game message, sets the second mission as completed,
   * shows the complete image, and hides the controller.
   */
  public void fixController() {
    // This method trys to fix the controller
    // If the controller mission less than stage 0 (not having spare part on hand),
    // show broken
    // controller message
    if (GameState.missionManager.getMission(MISSION.CONTROLLER).getStage() != 1) {
      infoTitle.setText("Broken Control Panel");
      infoLabel.setText("The control panel for the ship is broken");
      infoTitle.setVisible(true);
      infoLabel.setVisible(true);
      controllerInfo.setVisible(true);
    } else if (GameState.missionManager.getMission(MISSION.CONTROLLER).getStage() == 1) {
      // If the controller mission is at stage 2, indicating panel can be fixed, show
      // message

      // Increase the stage, update progress bar
      GameState.missionManager.getMission(MISSION.CONTROLLER).increaseStage();
      GameState.progressBarGroup.updateProgressTwo(MISSION.CONTROLLER);
      // Set the end game button visible
      winGameCollisionBox.setVisible(true);
      WinGame.startFlashWin();
      GameState.isSecondMissionCompleted = true;
      // Show the complete image
      completeImage.setVisible(true);
      // Hide the controller
      controllerBroken1.setVisible(false);
      controllerBroken2.setVisible(false);
    }
  }

  /**
   * Sets the UI to the win panel and plays the win media.
   */
  public void goHome() {
    // if window and control panel are fixed, then game can be completed by pressing
    // red button
    if (GameState.inventory.contains(6) && GameState.inventory.contains(7)) {
      App.setUi(AppPanel.WIN);

      WinController.playMedia();
    }
  }

  private void activateBlueprint() {
    if (!GameState.firstRiddleSolved || GameState.missionList.contains(3)) {
      return;
    }
    SceneManager.getPanel(AppPanel.STORAGE).lookup("#blueprint").setVisible(true);
    SceneManager.getPanel(AppPanel.STORAGE).lookup("#blueprintCollisionBox").setVisible(true);
  }

  /** Activate the background image for second mission. */
  private void activateSecondMissionImage() {
    if (GameState.missionList.contains(1) && GameState.missionList.contains(3)) {
      // Activate background image for window & controller mission
      windowController2.setVisible(true);
    } else if (GameState.missionList.contains(1) && GameState.missionList.contains(4)) {
      // Activate background image for window & thruster mission
      windowThruster2.setVisible(true);
    } else if (GameState.missionList.contains(2) && GameState.missionList.contains(3)) {
      // Activate background image for fuel & controller mission
      fuelController2.setVisible(true);
    } else {
      // Activate background image for fuel & thruster mission
      fuelThruster2.setVisible(true);
    }
  }

  public void activateProgressGlow() {
    progressButton.setEffect(GameState.glowBright);
  }

  public void deactivateProgressGlow() {
    progressButton.setEffect(GameState.glowDim);
  }

  /**
   * Activates the glowing effect on the outside of the door and sets the cursor
   * to a hand icon.
   * Also sets the opacity of the outside door to 1 and sets its cursor to a hand
   * icon.
   */
  public void activateDoorGlow() {
    outside.setEffect(GameState.glowBright);
    outside.setCursor(Cursor.HAND);
    outsideDoor.setOpacity(1);
    outsideDoor.setCursor(Cursor.HAND);
  }

  public void deactivateDoorGlow() {
    outside.setEffect(GameState.glowDim);
    outsideDoor.setOpacity(0);
  }

  public void activateFuelTankGlow() {
    fuelTank.setOpacity(1);
    fuelTank.setCursor(Cursor.HAND);
  }

  public void deactivateFuelTankGlow() {
    fuelTank.setOpacity(0);
  }

  /**
   * Activates the storage glow effect and sets the cursor to hand for the storage
   * and storage door.
   */
  public void activateStorageGlow() {
    storage.setEffect(GameState.glowBright);
    storageDoor.setOpacity(1);
    storage.setCursor(Cursor.HAND);
    storageDoor.setCursor(Cursor.HAND);
  }

  public void deactivateStorageGlow() {
    storage.setEffect(GameState.glowDim);
    storageDoor.setOpacity(0);
  }

  public void activateWindowGlow() {
    window.setOpacity(1);
    window.setCursor(Cursor.HAND);
  }

  public void deactivateWindowGlow() {
    window.setOpacity(0);
  }

  /**
   * Activates the glow effect for the broken controllers.
   */
  public void activateControllerGlow() {
    controllerBroken1.setOpacity(1);
    controllerBroken1.setCursor(Cursor.HAND);

    controllerBroken2.setOpacity(1);
    controllerBroken2.setCursor(Cursor.HAND);
  }

  public void deactivateControllerGlow() {
    controllerBroken1.setOpacity(0);
    controllerBroken2.setOpacity(0);
  }

  /**
   * This method sets the panel to chat and displays a message from the Wise
   * Ancient Tree depending on the player's progress in the game.
   * If the first mission is completed, the second guide message is shown.
   * The method also stops the tree flashing and deactivates the tree glow.
   * Finally, it sets the previous panel to Main room then goes to chat room.
   */
  public void goChat() {
    // This method set the panel to chat
    // Stop the tree flashing
    TreeAvatar.treeFlash.pause();
    TreeAvatar.deactivateTreeGlow();

    // If the first mission is completed, show the second guide message
    if (GameState.isFirstMissionCompleted && !GameState.isSecondGuideShown) {
      if (GameState.missionList.contains(1) && GameState.missionList.contains(4)) {
        String appendString = "You have repaired the window... Well done. You still cannot leave however, as the"
            + " thrusters are still damaged. In amongst your ship, I have hidden a"
            + " blueprint that should help you fix the thrusters";
        ((TextArea) SceneManager.getPanel(AppPanel.CHAT).lookup("#chatTextArea"))
            .appendText("Wise Ancient Tree: " + appendString);
        ((Label) SceneManager.getPanel(AppPanel.CHAT).lookup("#chatLabel")).setText(appendString);

      } else if (GameState.missionList.contains(1) && GameState.missionList.contains(3)) {

        String appendString = "You have repaired the window... Well done. You still cannot leave however, as the"
            + " control panel is still damaged. In amongst your ship, I have hidden a"
            + " chest containing spare parts that should help you fix the control panel";

        ((TextArea) SceneManager.getPanel(AppPanel.CHAT).lookup("#chatTextArea"))
            .appendText("Wise Ancient Tree: " + appendString);
        ((Label) SceneManager.getPanel(AppPanel.CHAT).lookup("#chatLabel")).setText(appendString);
      } else if (GameState.missionList.contains(2) && GameState.missionList.contains(4)) {

        String appendString = "You have refueled the ship... Well done. You still cannot leave however, as the"
            + " thrusters are still damaged. In amongst your ship, I have hidden a"
            + " blueprint that should help you fix the thrusters";

        ((TextArea) SceneManager.getPanel(AppPanel.CHAT).lookup("#chatTextArea"))
            .appendText("Wise Ancient Tree: " + appendString);
        ((Label) SceneManager.getPanel(AppPanel.CHAT).lookup("#chatLabel")).setText(appendString);
      } else {

        String appendString = "You have refueled the ship... Well done. You still cannot leave however, as the"
            + " control panel is still damaged. In amongst your ship, I have hidden a"
            + " chest containing spare parts that should help you fix the control panel";

        ((TextArea) SceneManager.getPanel(AppPanel.CHAT).lookup("#chatTextArea"))
            .appendText("Wise Ancient Tree: " + appendString);
        ((Label) SceneManager.getPanel(AppPanel.CHAT).lookup("#chatLabel")).setText(appendString);
      }
      outsideDoor.setDisable(false);
      storageDoor.setDisable(false);
    }
    GameState.isSecondGuideShown = true;

    // Set the previous panel to Main room then go to chat room
    SceneManager.setPrevious(AppPanel.MAIN_ROOM);
    App.setUi(AppPanel.CHAT);
  }

  public void miniTreeGlow() {
    miniTree.setEffect(GameState.glowBright);
  }

  public void miniTreeDim() {
    miniTree.setEffect(GameState.glowDim);
  }

  public void hideGuide() {
    guideImage.setVisible(false);
    guideLabel.setVisible(false);
  }

  /**
   * Handles the key pressed event for the "OK" button.
   * If the ENTER or ESCAPE key is pressed, the guide is hidden.
   *
   * @param event The KeyEvent that triggered the method call.
   * @throws ApiProxyException If an error occurs while communicating with the
   *                           API.
   * @throws IOException       If an I/O error occurs.
   */
  @FXML
  public void okKeyPressed(KeyEvent event) throws ApiProxyException, IOException {
    if (event.getCode().toString().equals("ENTER") || event.getCode().toString().equals("ESCAPE")) {
      hideGuide();
    }
  }

  public void activateWinGlow() {
    completeGame.setEffect(GameState.glowBright);
  }

  public void deactivateWinGlow() {
    completeGame.setEffect(GameState.glowDim);
  }

  /**
   * Hides the information labels and windows when the user exits the information
   * screen.
   */
  public void exitInfo() {
    infoLabel.setVisible(false);
    infoTitle.setVisible(false);
    windowInfo.setVisible(false);
    fuelInfo.setVisible(false);
    controllerInfo.setVisible(false);
  }

  /**
   * Activates the glow effect for the collision boxes in root one.
   */
  public void activateRootOneGlow() {
    // uses set effect method to change the cursor visual to an open hand when the
    // mouse is over the

    rootOneCollisionBox1.setOpacity(1);
    rootOneCollisionBox1.setCursor(Cursor.HAND);
    rootOneCollisionBox2.setOpacity(1);
    rootOneCollisionBox2.setCursor(Cursor.HAND);
    rootOneCollisionBox3.setOpacity(1);
    rootOneCollisionBox3.setCursor(Cursor.HAND);
    rootOneCollisionBox4.setOpacity(1);
    rootOneCollisionBox4.setCursor(Cursor.HAND);
  }

  /**
   * Sets the opacity of the collision boxes in root one to 0, effectively
   * deactivating the glow effect.
   */
  public void deactivateRootOneGlow() {
    rootOneCollisionBox1.setOpacity(0);
    rootOneCollisionBox2.setOpacity(0);
    rootOneCollisionBox3.setOpacity(0);
    rootOneCollisionBox4.setOpacity(0);
  }

  /**
   * Activates the glow effect for the collision boxes in root two.
   */
  public void activateRootTwoGlow() {
    // box 1
    rootTwoCollisionBox1.setOpacity(1); // set opacity to 1
    rootTwoCollisionBox1.setCursor(Cursor.HAND); // set cursor to hand
    // box 2
    rootTwoCollisionBox2.setOpacity(1);
    rootTwoCollisionBox2.setCursor(Cursor.HAND);
    // box 3
    rootTwoCollisionBox3.setOpacity(1);
    rootTwoCollisionBox3.setCursor(Cursor.HAND);
    // box 4
    rootTwoCollisionBox4.setOpacity(1);
    rootTwoCollisionBox4.setCursor(Cursor.HAND);
  }

  /**
   * Deactivates the glow effect on the collision boxes of the second root in the
   * game.
   */
  public void deactivateRootTwoGlow() {
    rootTwoCollisionBox1.setOpacity(0);
    rootTwoCollisionBox2.setOpacity(0);
    rootTwoCollisionBox3.setOpacity(0);
    rootTwoCollisionBox4.setOpacity(0);
  }

  /**
   * Activates the glow effect for the collision boxes in root three and sets the
   * cursor to hand.
   */
  public void activateRootThreeGlow() {
    // box 1
    rootThreeCollisionBox1.setOpacity(1); // set opacity to 1
    rootThreeCollisionBox1.setCursor(Cursor.HAND); // set cursor to hand
    // box 2
    rootThreeCollisionBox2.setOpacity(1);
    rootThreeCollisionBox2.setCursor(Cursor.HAND);
    // box 3
    rootThreeCollisionBox3.setOpacity(1);
    rootThreeCollisionBox3.setCursor(Cursor.HAND);
    // box 4
    rootThreeCollisionBox4.setOpacity(1);
    rootThreeCollisionBox4.setCursor(Cursor.HAND);
    // box 5
    rootThreeCollisionBox5.setOpacity(1);
    rootThreeCollisionBox5.setCursor(Cursor.HAND);
  }

  /**
   * Deactivates the glow effect on the collision boxes for root three.
   */
  public void deactivateRootThreeGlow() {
    rootThreeCollisionBox1.setOpacity(0);
    rootThreeCollisionBox2.setOpacity(0);
    rootThreeCollisionBox3.setOpacity(0);
    rootThreeCollisionBox4.setOpacity(0);
    rootThreeCollisionBox5.setOpacity(0);
  }

  /**
   * Activates the glow effect for all nine collision boxes in the root four area,
   * setting their opacity to 1 and cursor to hand.
   */
  public void activateRootFourGlow() {
    // box 1
    rootFourCollisionBox1.setOpacity(1); // set opacity to 1
    rootFourCollisionBox1.setCursor(Cursor.HAND); // set cursor to hand
    // box 2
    rootFourCollisionBox2.setOpacity(1);
    rootFourCollisionBox2.setCursor(Cursor.HAND);
    // box 3
    rootFourCollisionBox3.setOpacity(1);
    rootFourCollisionBox3.setCursor(Cursor.HAND);
    // box 4
    rootFourCollisionBox4.setOpacity(1);
    rootFourCollisionBox4.setCursor(Cursor.HAND);
    // box 5
    rootFourCollisionBox5.setOpacity(1);
    rootFourCollisionBox5.setCursor(Cursor.HAND);
    // box 6
    rootFourCollisionBox6.setOpacity(1);
    rootFourCollisionBox6.setCursor(Cursor.HAND);
    // box 7
    rootFourCollisionBox7.setOpacity(1);
    rootFourCollisionBox7.setCursor(Cursor.HAND);
    // box 8
    rootFourCollisionBox8.setOpacity(1);
    rootFourCollisionBox8.setCursor(Cursor.HAND);
    // box 9
    rootFourCollisionBox9.setOpacity(1);
    rootFourCollisionBox9.setCursor(Cursor.HAND);
  }

  /**
   * Deactivates the glow effect on all collision boxes in root four.
   */
  public void deactivateRootFourGlow() {
    // deactivate all the glow
    rootFourCollisionBox1.setOpacity(0); // set opacity to 0
    rootFourCollisionBox2.setOpacity(0);
    rootFourCollisionBox3.setOpacity(0); // set opacity to 0
    rootFourCollisionBox4.setOpacity(0);
    rootFourCollisionBox5.setOpacity(0);
    rootFourCollisionBox6.setOpacity(0); // set opacity to 0
    rootFourCollisionBox7.setOpacity(0);
    rootFourCollisionBox8.setOpacity(0);
    rootFourCollisionBox9.setOpacity(0); // set opacity to 0
  }
}
