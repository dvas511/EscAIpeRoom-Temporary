package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.MissionManager.MISSION;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppPanel;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult.Choice;

/** Controller class for the chat view. */
public class ChatController {
  public static ChatMessage gptMessage;

  @FXML private TextArea chatTextArea;
  @FXML private TextField inputText;
  @FXML private Button sendButton;
  @FXML private Label counter;
  @FXML private Rectangle neutral;
  @FXML private Rectangle thinking1;
  @FXML private Rectangle thinking2;
  @FXML private Circle speaking;
  @FXML private Circle eye1;
  @FXML private Circle eye2;
  @FXML private Circle loadingCircle;
  @FXML private Label listeningLabel;

  @FXML private ProgressIndicator loading;
  @FXML private ImageView progressButton;

  @FXML private Rectangle fuel;
  @FXML private Label fuelCollected;

  // private ChatMessage thinkingMessage =
  //     new ChatMessage("Wise Mystical Tree", "Allow me to ponder...");
  private ChatCompletionRequest chatCompletionRequest;

  public static ChatMessage secondGuideMessage;

  private int firstMission;
  private int secondMission;

  /**
   * Initializes the chat view, loading the riddle.
   *
   * @throws ApiProxyException if there is an error communicating with the API proxy
   */
  @FXML
  public void initialize() throws ApiProxyException {

    chatTextArea.setEditable(false); // prevents user from editing the chat text area

    inputText.setDisable(true);

    eye1.setVisible(false);
    eye2.setVisible(false);
    neutral.setVisible(true);
    speaking.setVisible(false);
    thinking1.setVisible(true);
    thinking2.setVisible(true);

    loading.setVisible(true);
    loadingCircle.setFill(Color.LIGHTGRAY);

    // greets the user at the start.
    Task<Void> greetTask =
        new Task<Void>() {

          @Override
          protected Void call() throws Exception {

            System.out.println("greet task");

            setChatCompletionRequest(
                new ChatCompletionRequest()
                    .setN(1)
                    .setTemperature(0.7)
                    .setTopP(0.7)
                    .setMaxTokens(100));

            gptMessage = runGpt(new ChatMessage("user", GptPromptEngineering.introCall()));
            appendChatMessage(gptMessage);

            updateProgress(1, 1);
            return null;
          }
        };

    loading.progressProperty().bind(greetTask.progressProperty());

    greetTask.setOnSucceeded(
        e -> {
          loading.progressProperty().unbind();
          eye1.setVisible(true);
          eye2.setVisible(true);
          thinking1.setVisible(false);
          thinking2.setVisible(false);
          neutral.setVisible(false);
          speaking.setVisible(true);
          loading.setVisible(false);
          loadingCircle.setFill(Color.valueOf("264f31"));
          inputText.setDisable(false);
        });

    Thread mainRiddleThread = new Thread(greetTask);
    mainRiddleThread.start();

    Task<Void> guideTask =
        new Task<Void>() {

          @Override
          protected Void call() throws Exception {

            System.out.println("greet task");

            setChatCompletionRequest(
                new ChatCompletionRequest()
                    .setN(1)
                    .setTemperature(0.7)
                    .setTopP(0.7)
                    .setMaxTokens(100));

            if (true) { // controller
              secondGuideMessage =
                  runGpt(
                      new ChatMessage(
                          "user",
                          GptPromptEngineering.getGuideToSecondMission("Fix the Controller")));
              System.out.println("second guide message");
            }

            updateProgress(1, 1);
            return null;
          }
        };

    loading.progressProperty().bind(guideTask.progressProperty());

    guideTask.setOnSucceeded(
        e -> {
          loading.progressProperty().unbind();
          eye1.setVisible(true);
          eye2.setVisible(true);
          thinking1.setVisible(false);
          thinking2.setVisible(false);
          neutral.setVisible(false);
          speaking.setVisible(true);
          loading.setVisible(false);
          loadingCircle.setFill(Color.valueOf("264f31"));
          inputText.setDisable(false);
        });

    Thread guideThread = new Thread(guideTask);
    guideThread.start();
  }

  public ChatCompletionRequest getChatCompletionRequest() {
    return chatCompletionRequest;
  }

  public void setChatCompletionRequest(ChatCompletionRequest chatCompletionRequest) {
    this.chatCompletionRequest = chatCompletionRequest;
  }

  public void goProgress() {
    App.setUi(AppPanel.PROGRESS);
  }

  /**
   * Appends a chat message to the chat text area.
   *
   * @param msg the chat message to append
   */
  private void appendChatMessage(ChatMessage msg) {
    chatTextArea.appendText(msg.getRole() + ": " + msg.getContent() + "\n\n");
  }

  /**
   * Runs the GPT model with a given chat message.
   *
   * @param msg the chat message to process
   * @return the response chat message
   * @throws ApiProxyException if there is an error communicating with the API proxy
   */
  private ChatMessage runGpt(ChatMessage msg) throws ApiProxyException {
    getChatCompletionRequest().addMessage(msg);
    try {
      ChatCompletionResult chatCompletionResult = getChatCompletionRequest().execute();
      Choice result = chatCompletionResult.getChoices().iterator().next();
      getChatCompletionRequest().addMessage(result.getChatMessage());
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

  /**
   * Sends a message to the GPT model.
   *
   * @param event the action event triggered by the send button
   * @throws ApiProxyException if there is an error communicating with the API proxy
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onSendMessage(ActionEvent event) throws ApiProxyException, IOException {

    inputText.setDisable(true);

    loading.setProgress(0);
    loading.setVisible(true);

    loadingCircle.setFill(Color.LIGHTGRAY);

    String message = inputText.getText();
    if (message.trim().isEmpty()) {
      return;
    }
    inputText.clear();

    eye1.setVisible(false);
    eye2.setVisible(false);
    neutral.setVisible(true);
    speaking.setVisible(false);
    thinking1.setVisible(true);
    thinking2.setVisible(true);
    listeningLabel.setVisible(false);

    if (!GameState.isGreetingShown && !GameState.isFirstMissionCompleted) {
      generateFirstRiddle(message);
      GameState.isGreetingShown = true;
      listeningLabel.setVisible(false);
      return;
    } else if (GameState.isFirstMissionCompleted && !GameState.isSecondGuideShown) {
      generatePuzzle(message);
      GameState.isSecondGuideShown = true;
      listeningLabel.setVisible(false);
      return;
    }

    Task<Void> typeCall =
        new Task<Void>() {

          @Override
          protected Void call() throws Exception {

            System.out.println("type call");

            ChatMessage msg = new ChatMessage("user", message);
            appendChatMessage(msg);
            ChatMessage lastMsg = runGpt(msg);
            appendChatMessage(lastMsg);

            System.out.println("lastMsg");

            // if riddle was solved correctly, then -1 is added to the inventory; -2 is determined
            // from the launch panel and checks whether or not text to speech will be active

            if (!GameState.firstRiddleSolved) {
              System.out.println("first riddle not solved");
              if (lastMsg.getRole().equals("assistant")
                  && lastMsg.getContent().startsWith("Correct")) {
                if (!GameState.firstRiddleSolved && GameState.missionList.contains(2)) {
                  GameState.missionManager.getMission(MISSION.FUEL).increaseStage();
                  GameState.progressBarGroup.updateProgressOne(MISSION.FUEL);
                  System.out.println("Fuel Mission 1 Complete");
                }
                GameState.firstRiddleSolved = true;
                fuel.setDisable(false);
                fuel.setVisible(true);
                System.out.println("first riddle solved");
              }
            } else if (GameState.firstRiddleSolved && !GameState.secondRiddleSolved) {
              if (lastMsg.getRole().equals("assistant")
                  && lastMsg.getContent().startsWith("Correct")) {
                GameState.secondRiddleSolved = true;
              }
            }
            updateProgress(1, 1);
            return null;
          }
        };

    loading.progressProperty().bind(typeCall.progressProperty());

    typeCall.setOnSucceeded(
        e -> {
          loading.progressProperty().unbind();
          eye1.setVisible(true);
          eye2.setVisible(true);
          thinking1.setVisible(false);
          thinking2.setVisible(false);
          neutral.setVisible(false);
          speaking.setVisible(true);
          loading.setVisible(false);
          loadingCircle.setFill(Color.valueOf("264f31"));
          inputText.setDisable(false);
        });

    Thread typeInThread = new Thread(typeCall);
    typeInThread.start();
  }

  /**
   * Navigates back to the previous view.
   *
   * @param event the action event triggered by the go back button
   * @throws ApiProxyException if there is an error communicating with the API proxy
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onGoBack(ActionEvent event) throws ApiProxyException, IOException {
    speaking.setVisible(false);
    neutral.setVisible(true);
    App.setUi(SceneManager.getPrevious());
  }

  /**
   * Handles the key pressed event.
   *
   * @param event the key event
   */
  @FXML
  public void onKeyPressed(KeyEvent event) {
    eye1.setVisible(false);
    eye2.setVisible(false);
    thinking1.setVisible(true);
    thinking2.setVisible(true);
    listeningLabel.setVisible(true);
    speaking.setVisible(false);
    neutral.setVisible(true);
  }

  /**
   * Handles the key released event.
   *
   * @param event the key event
   */
  @FXML
  public void onKeyReleased(KeyEvent event) {
    System.out.println("key " + event.getCode() + " released");
    if (inputText.getText().trim().isEmpty()) {
      eye1.setVisible(true);
      eye2.setVisible(true);
      thinking1.setVisible(false);
      thinking2.setVisible(false);
      listeningLabel.setVisible(false);
    }
  }

  public void activateProgressGlow() {
    progressButton.setEffect(GameState.glowBright);
  }

  public void deactivateProgressGlow() {
    progressButton.setEffect(GameState.glowDim);
  }

  private void generateFirstRiddle(String message) {

    inputText.setDisable(true);

    eye1.setVisible(false);
    eye2.setVisible(false);
    neutral.setVisible(true);
    speaking.setVisible(false);
    thinking1.setVisible(true);
    thinking2.setVisible(true);

    loading.setVisible(true);
    loadingCircle.setFill(Color.LIGHTGRAY);

    System.out.println("generate riddle");

    for (int i = 0; i < 2; i++) {
      System.out.println("----");
      if (GameState.missionList.get(i) == 1 || GameState.missionList.get(i) == 2) {
        System.out.println("first mission");
        firstMission = GameState.missionList.get(i);
        System.out.println(firstMission);
      } else {
        System.out.println("second mission");
        secondMission = GameState.missionList.get(i);
        System.out.println(secondMission);
      }
    }

    Task<Void> firstRiddleTask =
        new Task<Void>() {

          @Override
          protected Void call() throws Exception {

            ChatMessage msg = new ChatMessage("user", message);
            appendChatMessage(msg);

            setChatCompletionRequest(
                new ChatCompletionRequest()
                    .setN(1)
                    .setTemperature(0.5)
                    .setTopP(0.2)
                    .setMaxTokens(100));

            System.out.println("first mission riddle");
            if (firstMission == 1) { // if the first mission is the window
              gptMessage =
                  runGpt(
                      new ChatMessage(
                          "user", GptPromptEngineering.getRiddleWithGivenWordWindow("sand")));
              appendChatMessage(gptMessage);
            } else if (firstMission == 2) { // if it is the fuel
              gptMessage =
                  runGpt(
                      new ChatMessage(
                          "user", GptPromptEngineering.getRiddleWithGivenWordFuel("sky", "lake")));
              appendChatMessage(gptMessage);
            }

            updateProgress(1, 1);
            return null;
          }
        };

    loading.progressProperty().bind(firstRiddleTask.progressProperty());

    firstRiddleTask.setOnSucceeded(
        e2 -> {
          loading.progressProperty().unbind();
          eye1.setVisible(true);
          eye2.setVisible(true);
          thinking1.setVisible(false);
          thinking2.setVisible(false);
          neutral.setVisible(false);
          speaking.setVisible(true);
          loading.setVisible(false);
          loadingCircle.setFill(Color.valueOf("264f31"));
          inputText.setDisable(false);
        });

    Thread firstRiddleThread = new Thread(firstRiddleTask);
    firstRiddleThread.start();
  }

  public void fuelLight() {
    fuel.setFill(Color.valueOf("d0615f"));
  }

  public void fuelNeutral() {
    fuel.setFill(Color.valueOf("b51412"));
  }

  public void collectFuel() {
    GameState.inventory.add(8); // fuel collected
    GameState.missionManager.getMission(MISSION.FUEL).increaseStage();
    GameState.progressBarGroup.updateProgressOne(MISSION.FUEL);
    System.out.println("Fuel Mission 2 Complete");
    fuel.setVisible(false);
    fuel.setDisable(true);
    fuelCollected.setVisible(true);
  }

  private void generatePuzzle(String message) {
    inputText.setDisable(true);

    eye1.setVisible(false);
    eye2.setVisible(false);
    neutral.setVisible(true);
    speaking.setVisible(false);
    thinking1.setVisible(true);
    thinking2.setVisible(true);

    loading.setVisible(true);
    loadingCircle.setFill(Color.LIGHTGRAY);

    System.out.println("generate puzzle");

    Task<Void> secondPuzzleTask =
        new Task<Void>() {

          @Override
          protected Void call() throws Exception {

            ChatMessage msg = new ChatMessage("user", message);
            appendChatMessage(msg);

            setChatCompletionRequest(
                new ChatCompletionRequest()
                    .setN(1)
                    .setTemperature(0.5)
                    .setTopP(0.2)
                    .setMaxTokens(100));

            System.out.println("first mission riddle");
            if (firstMission == 3) { // if the first mission is the controller
              gptMessage =
                  runGpt(new ChatMessage("user", GptPromptEngineering.getControllerPuzzle()));
              appendChatMessage(gptMessage);
            } else if (firstMission == 4) { // if it is the thruster
              if (GameState.randomColorNumber == 1) { // red
                gptMessage =
                    runGpt(new ChatMessage("user", GptPromptEngineering.getThrusterPuzzle("red")));
                appendChatMessage(gptMessage);
              } else if (GameState.randomColorNumber == 2) { // blue
                gptMessage =
                    runGpt(new ChatMessage("user", GptPromptEngineering.getThrusterPuzzle("blue")));
                appendChatMessage(gptMessage);
              } else if (GameState.randomColorNumber == 3) { // green
                gptMessage =
                    runGpt(
                        new ChatMessage("user", GptPromptEngineering.getThrusterPuzzle("green")));
                appendChatMessage(gptMessage);
              } else if (GameState.randomColorNumber == 4) { // purple
                gptMessage =
                    runGpt(
                        new ChatMessage("user", GptPromptEngineering.getThrusterPuzzle("purple")));
                appendChatMessage(gptMessage);
              }
            }

            updateProgress(1, 1);
            return null;
          }
        };

    loading.progressProperty().bind(secondPuzzleTask.progressProperty());

    secondPuzzleTask.setOnSucceeded(
        e2 -> {
          loading.progressProperty().unbind();
          eye1.setVisible(true);
          eye2.setVisible(true);
          thinking1.setVisible(false);
          thinking2.setVisible(false);
          neutral.setVisible(false);
          speaking.setVisible(true);
          loading.setVisible(false);
          loadingCircle.setFill(Color.valueOf("264f31"));
          inputText.setDisable(false);
        });

    Thread secondPuzzleThread = new Thread(secondPuzzleTask);
    secondPuzzleThread.start();
  }
}
