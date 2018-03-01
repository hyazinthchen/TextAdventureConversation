package npcDialogue.view;

import com.queomedia.commons.checks.Check;
import npcDialogue.model.Action;
import npcDialogue.model.Role;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Responsible for reading the players input from the console and printing ActionTexts to the console.
 */
public class ConsoleInputOutput { //TODO rename
    private static final Scanner SCANNER = new Scanner(System.in);

    /**
     * Returns the action which was chosen by the player.
     *
     * @param availableActions
     * @return the chosen action.
     */
    public Action chooseByPlayerInput(List<Action> availableActions) {
        Check.notNullArgument(availableActions, "availableActions");
        if (availableActions.isEmpty()) {
            throw new IllegalArgumentException("AvailableActions list must have at least one element.");
        }
        printPlayersOptions(availableActions);
        return availableActions.get(awaitIntegerInput(0, availableActions.size() - 1));
    }

    /**
     * Waits for the players input into the console and reads it.
     *
     * @param min the lowest number the player may enter.
     * @param max the highest number the player may enter.
     * @return the input of the player as an int.
     */
    public int awaitIntegerInput(int min, int max) {
        while (true) {
            try {
                int input = SCANNER.nextInt();
                if (input >= min && input <= max) return input;
                else System.err.println("\n -> number out of range !\n");
            } catch (Exception e) {
                System.err.println("\n -> please enter a valid number !\n");
            }
        }
    }

    /**
     * Chooses an action randomly from a list of actions and returns it.
     *
     * @param availableActions the list of available actions.
     * @return the chosen action.
     */
    public Action chooseRandomly(List<Action> availableActions) {
        Random random = new Random();
        int randomNumber = random.nextInt(availableActions.size());
        Action action = availableActions.get(randomNumber);
        printSingleActionText(action);
        return action;
    }

    /**
     * Prints one single actions actionText to the console and inserts the actorType of the action before it.
     *
     * @param action the action of which the actionText should be printed.
     */
    public void printSingleActionText(Action action) {
        if (action.getRole() == Role.PLAYER) {
            System.out.println("You: " + action.getActionText());
        } else {
            System.out.println("NPC: " + action.getActionText());
        }
    }

    /**
     * Prints the actionTexts of multiple actions in a list to the console.
     *
     * @param actions the list of actions to print.
     */
    private void printPlayersOptions(List<Action> actions) {
        int i = 0;
        for (Action availableAction : actions) {
            System.out.println("[" + i + "] " + availableAction.getActionText());
            i++;
        }
    }

}
