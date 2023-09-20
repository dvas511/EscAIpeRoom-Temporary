package nz.ac.auckland.se206;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.effect.Glow;
import javafx.util.Duration;
import nz.ac.auckland.se206.SceneManager.AppPanel;

public class TreeAvatar {

  public static Timeline treeFlash =
      new Timeline(new KeyFrame(Duration.millis(750), e -> flashTree()));
  private static int treeState = 0;
  private static Glow glowDim = new Glow(0.0);
  private static Glow glowBright = new Glow(0.7);

  public static void startFlashTree() {
    treeFlash.setCycleCount(Timeline.INDEFINITE);
    treeFlash.play();
  }

  public static void flashTree() {
    if (treeState == 0) {
      SceneManager.getPanel(AppPanel.MAIN_ROOM).lookup("#miniTree").setEffect(glowBright);
      SceneManager.getPanel(AppPanel.WORK).lookup("#miniTree").setEffect(glowBright);
      SceneManager.getPanel(AppPanel.OUTSIDE).lookup("#miniTree").setEffect(glowBright);
      SceneManager.getPanel(AppPanel.THRUSTER).lookup("#miniTree").setEffect(glowBright);
      SceneManager.getPanel(AppPanel.CHEST).lookup("#miniTree").setEffect(glowBright);
      SceneManager.getPanel(AppPanel.PROGRESS).lookup("#miniTree").setEffect(glowBright);

      treeState = 1;
    } else {
      SceneManager.getPanel(AppPanel.MAIN_ROOM).lookup("#miniTree").setEffect(glowDim);
      SceneManager.getPanel(AppPanel.WORK).lookup("#miniTree").setEffect(glowDim);
      SceneManager.getPanel(AppPanel.OUTSIDE).lookup("#miniTree").setEffect(glowDim);
      SceneManager.getPanel(AppPanel.THRUSTER).lookup("#miniTree").setEffect(glowDim);
      SceneManager.getPanel(AppPanel.CHEST).lookup("#miniTree").setEffect(glowDim);
      SceneManager.getPanel(AppPanel.PROGRESS).lookup("#miniTree").setEffect(glowDim);
      treeState = 0;
    }
  }
}
