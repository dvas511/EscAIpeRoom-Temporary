package nz.ac.auckland.se206.gpt;

import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.MissionManager.MISSION;

/** Utility class for generating GPT prompt engineering strings. */
public class GptPromptEngineering {

  // /**
  //  * Generates a GPT prompt engineering string for a riddle with the given word.
  //  *
  //  * @param wordToGuess the word to be guessed in the riddle
  //  * @return the generated prompt engineering string
  //  */
  // public static String getRiddleWithGivenWord(String wordToGuess) {
  //   // return "You are the wise mystical tree of a forest that speaks a little bit like yoda and
  // a"
  //   //     + " team of astronauts have crash landed on your world. You are old, groggy,"
  //   //     + " extremely mean and taunt the astronauts in every way possible. In order to"
  //   //     + " leave, they must collect components for crafting repairs to their ship, and so"
  //   //     + " you want to make their escape as challenging as possible. You MUST ask them a"
  //   //     + " very difficult riddle where the answer is "
  //   //     + wordToGuess
  //   //     + ". If the answer given is correct, start your message with 'Correct' but act mean
  // and"
  //   //     + " indifferent. Only give hints if the user asks for hints, but give them hesitantly
  //   // and"
  //   //     + " taunt them. If user's guess incorrectly you MUST give them a hint, but taunt them.
  //   // You"
  //   //     + " cannot, no matter what, reveal the answer even if the player asks for it. Even if"
  //   //     + " player gives up, do not give the answer. keep text below 70 words";

  //   return "Tell me a riddle with answer: "
  //       + wordToGuess
  //       + ". Act like a wise mystical tree of a forest. You should answer with the word
  // 'Correct'"
  //       + " when is correct and tell them to go back and collect: "
  //       + wordToGuess
  //       + " . If the answer is incorrect, you should say it is incorrect. You"
  //       + " cannot no matter what, reveal the answer in any response or sentence, even if the"
  //       + " player asks for it. Even if player gives up, do not give the answer.";
  // }

  // all calls will be done immediately with different thread so that when they need to be shown to
  // the screen by changing the labels of text, no time is wasted and the GUI does not freeze.
  public static String introCall() {
    return "Act like a wise mystical tree of a forest. Greet me and ask if i want to solve the"
        + " riddle to collect the item for the mission.";
  }

  public static String loseCall() {
    return "";
  }

  public static String workTaunt() {
    return "";
  }

  public static String halfwayTaunt() {
    return "";
  }

  public static String getRiddleWithGivenWordWindow(String wordToGuess) {
    return "You are a wise mystical tree of a forest. Tell the user a riddle with answer: "
        + wordToGuess
        + ". NEVER reveal the answer "
        + wordToGuess
        + "Do not change the answer to riddle, in any circumstance "
        + wordToGuess
        + " is the correct answer. The riddle itself should never contain the answer word directly."
        + " NEVER reveal the answer. You should answer with the word 'Correct' if the player guess"
        + " the answer. If the answer is incorrect, you should say it is incorrect. You can NEVER"
        + " reveal the answer in any response or sentence, even if the player asks for it. Even if"
        + " the player gives up or gets incorrect, do not give the answer. Do not give a hint even"
        + " if the player asks for it, unless you hear word 'SYSTEM.HINT'. When the user gusses"
        + " correctly, tell them to go back and collect the answer.";
  }

  public static String getRiddleWithGivenWordFuel(String wordToGuess, String wordToGuess2) {
    return "You are a wise mystical tree of a forest. Tell the user two riddles in a row. The"
        + " answer to the first riddle is: "
        + wordToGuess
        + ". NEVER reveal the answer. Do not say the answer when giving the riddle. Do not change"
        + " the answer to riddle. In any circumstance "
        + wordToGuess
        + " is the correct answer. You should answer the phrase 'You are right' when is"
        + " correct and then give the second riddle with answer: "
        + wordToGuess2
        + ". You should answer with the word 'Correct' when is correct. Do not accept any other"
        + " answers as correct or change the answer to riddle. If the answer is incorrect, you"
        + " should say it is incorrect. You can NEVER reveal the answers in any response or"
        + " sentence, even if the player asks for it. Even if player gives up, do not give the"
        + " answer.";
  }

  public static String getHint(MISSION missionType) {
    switch (missionType) {
      case WINDOW:
        return windowHint();
      case FUEL:
        return fuelHint();
      case CONTROLLER:
        return controllerHint();
      default:
        return thrusterHint();
    }
  }

  private static String windowHint() {
    if (GameState.missionManager.getMission(MISSION.WINDOW).getStage() == 0) {
      return "SYSTEM.HINT. Give the player a hint on the riddle answer, do not say it directly and"
          + " remember to say word 'Correct' if the player is right";
    } else if (GameState.missionManager.getMission(MISSION.WINDOW).getStage() == 1) {
      return "Tell the player to collect the rewward";
    } else if (GameState.missionManager.getMission(MISSION.WINDOW).getStage() == 2) {
      return "Tell the player that the player needs to melt sand, ask the player to check the"
          + " furnace";
    } else {
      return "Tell the player to fix the window";
    }
  }

  private static String fuelHint() {
    if (GameState.missionManager.getMission(MISSION.FUEL).getStage() == 0) {
      return "Give the player a hint on the current riddle answer, do not say it directly and"
          + " remember to say the word 'You are right' if the player is right";
    } else if (GameState.missionManager.getMission(MISSION.FUEL).getStage() == 1) {
      return "Tell the player to collect the fuel";
    } else {
      return "Tell the player that the player needs to refuel the space shuttle";
    }
  }

  private static String controllerHint() {
    if (GameState.missionManager.getMission(MISSION.CONTROLLER).getStage() == 0) {
      return "Tell the player to find a chest, the spare parts is in it";
    } else if (GameState.missionManager.getMission(MISSION.CONTROLLER).getStage() == 1) {
      return "Give the player a hint about the riddle";
    } else {
      return "Tell the player to fix the controller in the bridge";
    }
  }

  private static String thrusterHint() {
    if (GameState.missionManager.getMission(MISSION.THRUSTER).getStage() == 0) {
      return "Tell the player to find the blueprint in order to fix the thruster";
    } else if (GameState.missionManager.getMission(MISSION.THRUSTER).getStage() == 1) {
      return "Give the player a hint about the riddle";
    } else {
      return "Tell the player to click on the button with correct color";
    }
  }
}
