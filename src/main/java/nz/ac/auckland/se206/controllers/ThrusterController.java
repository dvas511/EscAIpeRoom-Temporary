package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.SceneManager.AppPanel;
import nz.ac.auckland.se206.ThrusterButtons.BottomLeftButton;
import nz.ac.auckland.se206.ThrusterButtons.BottomRightButton;
import nz.ac.auckland.se206.ThrusterButtons.TopLeftButton;
import nz.ac.auckland.se206.ThrusterButtons.TopRightButton;

public class ThrusterController {

  @FXML private ImageView bottomLeftUnclicked;
  @FXML private ImageView bottomLeftClicked;
  @FXML private ImageView bottomRightUnclicked;
  @FXML private ImageView bottomRightClicked;
  @FXML private ImageView topLeftUnclicked;
  @FXML private ImageView topLeftClicked;
  @FXML private ImageView topRightUnclicked;
  @FXML private ImageView topRightClicked;
  @FXML private Button returnOutside;
  private int buttonActivationCounter = 0;

  public void initialize() {}

  public void setBottomLeftVisible() {
    if (BottomLeftButton.getCycleNumber() != GameState.getRandomColorNumber()) {
      BottomLeftButton.timeline.play();
      bottomLeftUnclicked.setVisible(true);
    } else {
      BottomLeftButton.timeline.pause();
      buttonActivationCounter++;
    }
  }

  public void setBottomLeftInvisible() {
    BottomLeftButton.timeline.pause();
    bottomLeftUnclicked.setVisible(false);
  }

  public void setBottomRightInvisible() {
    BottomRightButton.timeline.pause();
    bottomRightUnclicked.setVisible(false);
  }

  public void setBottomRightVisible() {
    if (BottomRightButton.getCycleNumber() != GameState.getRandomColorNumber()) {
      BottomRightButton.timeline.play();
      bottomRightUnclicked.setVisible(true);
    } else {
      BottomRightButton.timeline.pause();
      buttonActivationCounter++;
    }
  }

  public void setTopLeftInvisible() {
    TopLeftButton.timeline.pause();
    topLeftUnclicked.setVisible(false);
  }

  public void setTopLeftVisible() {
    if (TopLeftButton.getCycleNumber() != GameState.getRandomColorNumber()) {
      topLeftUnclicked.setVisible(true);
      TopLeftButton.timeline.play();
    } else {
      TopLeftButton.timeline.pause();
      buttonActivationCounter++;
    }
  }

  public void setTopRightInvisible() {
    TopRightButton.timeline.pause();
    topRightUnclicked.setVisible(false);
  }

  public void setTopRightVisible() {
    if (TopRightButton.getCycleNumber() != GameState.getRandomColorNumber()) {
      TopRightButton.timeline.play();
      topRightUnclicked.setVisible(true);
    } else {
      TopRightButton.timeline.pause();
      buttonActivationCounter++;
    }
  }

  public void beginRepairs() {

    // add logic so that once the game is over, the button can no longer be pressed.

    if (buttonActivationCounter == 0) {
      BottomRightButton.timeline.setCycleCount(360);
      BottomRightButton.timeline.play();
      BottomLeftButton.timeline.setCycleCount(360);
      BottomLeftButton.timeline.play();
      TopLeftButton.timeline.setCycleCount(360);
      TopLeftButton.timeline.play();
      TopRightButton.timeline.setCycleCount(360);
      TopRightButton.timeline.play();
    }
  }

  public void goOutside() {
    App.setUi(AppPanel.OUTSIDE);
  }
}
