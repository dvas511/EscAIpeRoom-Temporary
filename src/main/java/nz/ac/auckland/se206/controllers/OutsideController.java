package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.SceneManager.AppPanel;

public class OutsideController {
  @FXML private ImageView returnShip;
  @FXML private Circle sand;
  @FXML private Circle tech;
  @FXML private Circle wiseTree;
  @FXML private Label counter;
  @FXML private Label sandLabel;
  @FXML private Label treeLabel;
  @FXML private Label shipLabel;
  @FXML private Label techLabel;
  @FXML private ImageView rootInitial;
  @FXML private ImageView rootOne;
  @FXML private ImageView rootTwo;
  @FXML private ImageView rootThree;
  @FXML private ImageView thruster;

  public void initialize() {}

  // displays counter on panel and constantly checks if the riddle has been solved. If riddle was
  // solved correctly, and sand is currently NOT in the inventory, then the sand appears inside the
  // panel.
  public void outsideReturn() {
    App.setUi(AppPanel.MAIN_ROOM);
  }

  public void goThruster() {
    App.setUi(AppPanel.THRUSTER);
  }

  public void collectTech() {
    GameState.inventory.add(1);
    tech.setVisible(false);
  }

  // techLabel methods make the label appear and disappear depending on whether or not the mouse is
  // hovering over the object
  public void techLabelOn() {
    techLabel.setVisible(true);
  }

  public void techLabelOff() {
    techLabel.setVisible(false);
  }

  public void collectSand() {

    if (GameState.inventory.contains(-1)) {
      GameState.inventory.add(2);
      sand.setVisible(false);
    }
  }

  // there are two types of methods below: Light and Dark/Normal. On hover over with mouse, Light
  // method is invoked: the color of the selected object becomes lighter and a label becomes
  // visible, indicating it is
  // clickble. Once mouse is moved from object, color returns to original and the label is made
  // invisible with Dark/Normal method
  // invokation.

  public void treeLight() {
    wiseTree.setFill(Color.valueOf("864310"));
    treeLabel.setVisible(true);
  }

  public void treeNormal() {
    wiseTree.setFill(Color.valueOf("6f3506"));
    treeLabel.setVisible(false);
  }

  public void sandLight() {
    sand.setFill(Color.valueOf("fffccc"));
    sandLabel.setVisible(true);
  }

  public void sandNormal() {
    sand.setFill(Color.valueOf("fffba5"));
    sandLabel.setVisible(false);
  }

  public void openRiddle() {
    App.setUi(AppPanel.CHAT);
  }
}
