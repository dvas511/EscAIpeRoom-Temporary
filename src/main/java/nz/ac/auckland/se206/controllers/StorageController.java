package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.SceneManager.AppPanel;

public class StorageController {

  @FXML private Rectangle storageDoor;
  @FXML private Rectangle hiddenChest;
  @FXML private ImageView storageDoorImage;
  @FXML private ImageView hiddenChestImage;
  @FXML private ImageView chest;
  @FXML private Label counter;

  public void goInside() {
    App.setUi(AppPanel.MAIN_ROOM);
  }

  public void findChest() {
    System.out.println("Find chest");
    chest.setDisable(false);
    chest.setOpacity(1);
  }

  public void goToChest() {
    App.setUi(AppPanel.CHEST);
  }

  public void activateDoorGlow() {
    storageDoorImage.setEffect(GameState.glowBright);
  }

  public void deactivateDoorGlow() {
    storageDoorImage.setEffect(GameState.glowDim);
  }

  public void activateHiddenChestGlow() {
    hiddenChestImage.setEffect(GameState.glowBright);
  }

  public void deactivateHiddenChestGlow() {
    hiddenChestImage.setEffect(GameState.glowDim);
  }

  public void activateChestGlow() {
    chest.setEffect(GameState.glowBright);
  }

  public void deactivateChestGlow() {
    chest.setEffect(GameState.glowDim);
  }
}
