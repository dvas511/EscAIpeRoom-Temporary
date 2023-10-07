package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.Random;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.MissionInitialise;
import nz.ac.auckland.se206.RootBorder;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppPanel;
import nz.ac.auckland.se206.TimeCounter;

public class LaunchController {

  private String mission1;
  private String mission2;

  private static Parent loadFxml(final String fxml) throws IOException {
    return new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml")).load();
  }

  @FXML private Button launchButton;
  @FXML private Button diffButton;
  @FXML private Button timerButton;
  @FXML private Button speechButton;
  @FXML private Button quitButton;

  public static TimeCounter timer;

  MissionInitialise missionInitialise = new MissionInitialise();

  // clears all instances of existing rooms, wipes out the inventory and resets the timeline
  public void launchGame(MouseEvent ev) throws IOException {
    Random rand = new Random();
    int task1 = rand.nextInt(2) + 1;
    int task2 = rand.nextInt(2) + 3;
    GameState.missionListA.add(task1);
    GameState.missionListA.add(task2);

    SceneManager.clearMap();
    SceneManager.addPanel(AppPanel.MAIN_ROOM, loadFxml("mainRoom"));
    SceneManager.addPanel(AppPanel.OUTSIDE, loadFxml("outsideRoom"));
    SceneManager.addPanel(AppPanel.LOSE, loadFxml("loseRoom"));
    SceneManager.addPanel(AppPanel.WIN, loadFxml("winRoom"));
    SceneManager.addPanel(AppPanel.LAUNCH, loadFxml("startRoom"));
    SceneManager.addPanel(AppPanel.THRUSTER, loadFxml("thrusterRoom"));
    SceneManager.addPanel(AppPanel.CHEST, loadFxml("chest"));
    SceneManager.addPanel(AppPanel.PROGRESS, loadFxml("progressBars"));
    SceneManager.addPanel(AppPanel.STORAGE, loadFxml("storage"));
    SceneManager.addPanel(AppPanel.CHAT, loadFxml("chat"));

    createTimer();

    GameState.createRandomColorNumber();

    GameState.missionManager.addMission(task1);
    GameState.missionManager.addMission(task2);
    GameState.missionList.add(task1);
    GameState.missionList.add(task2);
    GameState.progressBarGroup.setMissionOne(task1);
    GameState.progressBarGroup.setMissionTwo(task2);
    missionInitialise.initialiseFirstMission(task1);
    missionInitialise.initialiseSecondMission(task2);

    if (GameState.missionList.contains(1)) {
      mission1 = "Fix the Window";
    } else if (GameState.missionList.contains(2)) {
      mission1 = "Charge the Fuel";
    }

    if (GameState.missionList.contains(3)) {
      mission2 = "Fix the Controller";
    } else if (GameState.missionList.contains(4)) {
      mission2 = "Fix the Thruster";
    }

    String text =
        "Hey there, Seems like you have crush-landed \non this planet! "
            + "\nWonder who I am?\n"
            + "I am the mysterious tree of this planet.\n"
            + "To leave, you should have to fix this ship.\nFirst, "
            + mission1
            + " and then "
            + mission2
            + ".\nFind me outside for the clue to the first step!";

    ((Label) SceneManager.getPanel(AppPanel.MAIN_ROOM).lookup("#guideLabel")).setText(text);

    String mission1;

    if (GameState.missionList.contains(1)) {
      mission1 =
          "Know how to fix the window? I shall give you a riddle and the answer should guide you"
              + " to the next step. You ready?\n";
      ((TextArea) SceneManager.getPanel(AppPanel.CHAT).lookup("#chatTextArea"))
          .appendText(mission1);

    } else if (GameState.missionList.contains(2)) {
      mission1 =
          "Know how to charge the fuel? I shall give you a riddle and the answer should guide"
              + " you to the next step. You ready?\n";
      ((TextArea) SceneManager.getPanel(AppPanel.CHAT).lookup("#chatTextArea"))
          .appendText(mission1);
    }

    SceneManager.setPrevious(AppPanel.MAIN_ROOM);

    App.setUi(AppPanel.MAIN_ROOM);
  }

  public void changeDiff() {
    // switch case for difficulty in Gamestate class for numbers between 0-2
    int difficulty = GameState.getDifficulty();

    switch (difficulty) {
      case 0:
        GameState.setDifficulty(1);
        GameState.setHintNumber(5);
        diffButton.setText("Difficulty: Medium");
        break;
      case 1:
        GameState.setDifficulty(2);
        GameState.setHintNumber(0);
        diffButton.setText("Difficulty: Hard");
        break;
      case 2:
        GameState.setDifficulty(0);
        GameState.setHintNumber(1000);
        diffButton.setText("Difficulty: Easy");
        break;
    }
  }

  public void changeTimer() {

    int timer = GameState.getTimer();

    switch (timer) {
      case 0:
        GameState.setTimer(1);
        timerButton.setText("Timer: Four Minutes");
        break;
      case 1:
        GameState.setTimer(2);
        timerButton.setText("Timer: Six Minutes");
        break;
      case 2:
        GameState.setTimer(0);
        timerButton.setText("Timer: Two Minutes");
        break;
    }
  }

  public void changeSpeech() {
    if (GameState.textToSpeechSetting) {
      GameState.textToSpeechSetting = false;
      speechButton.setText("Text to Speech: Off");
    } else {
      GameState.textToSpeechSetting = true;
      speechButton.setText("Text to Speech: On");
    }
  }

  public void quitGame() {
    Platform.exit();
    System.exit(0);
  }

  public void createTimer() {
    Task<Void> timerTask =
        new Task<Void>() {
          @Override
          protected Void call() {

            if (GameState.timer == 0) {
              timer = new TimeCounter(2, 0);
            } else if (GameState.timer == 1) {
              timer = new TimeCounter(4, 0);
            } else {
              timer = new TimeCounter(6, 0);
            }
            while (true) {
              if (timer.isEnd()) {
                return null;
              }

              // Every 1s, update the clock
              if (!timer.isEnd()) {
                Platform.runLater(
                    () -> {
                      updateClock(timer.getTime());
                    });
              }

              // Implement methods for 25%, 50%, 75% progress of game
              if (timer.getRemainingPercentage() == 75) {
                System.out.println("25% passed");
                RootBorder.rootGrow();
              } else if (timer.getRemainingPercentage() == 50) {
                System.out.println("50% passed");
                RootBorder.rootGrow();

              } else if (timer.getRemainingPercentage() == 25) {
                System.out.println("75% passed");
                RootBorder.rootGrow();
              }

              // Decrease the counter by 1 unit every 1 second
              try {
                Thread.sleep(1000);
                timer.decrease();
              } catch (InterruptedException e) {
                System.out.println("Interrupted Exception in timer thread");
              }
            }
          }
        };
    Thread timerThread = new Thread(timerTask);
    timerThread.start();
  }

  public void updateClock(String time) {
    ((Label) SceneManager.getPanel(AppPanel.MAIN_ROOM).lookup("#counter")).setText(time);
    ((Label) SceneManager.getPanel(AppPanel.CHAT).lookup("#counter")).setText(time);
    ((Label) SceneManager.getPanel(AppPanel.OUTSIDE).lookup("#counter")).setText(time);
    ((Label) SceneManager.getPanel(AppPanel.CHEST).lookup("#counter")).setText(time);
    ((Label) SceneManager.getPanel(AppPanel.STORAGE).lookup("#counter")).setText(time);
    ((Label) SceneManager.getPanel(AppPanel.PROGRESS).lookup("#counter")).setText(time);
    ((Label) SceneManager.getPanel(AppPanel.THRUSTER).lookup("#counter")).setText(time);
  }
}
