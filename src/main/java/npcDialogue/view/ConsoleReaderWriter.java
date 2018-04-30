package npcDialogue.view;

import npcDialogue.model.Action;
import npcDialogue.model.Role;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * Responsible for reading the players input from the console and printing ActionTexts to the console.
 */
public class ConsoleReaderWriter {
    private static final Scanner SCANNER = new Scanner(System.in);

    /**
     * Returns the action which was chosen by the player.
     *
     * @param availableActions a list of all the actions the player may choose from.
     * @return the chosen action by the player.
     */
    public Action chooseByPlayerInput(List<Action> availableActions) {
        if (availableActions == null) {
            throw new IllegalArgumentException("AvailableActions is null.");
        }

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
    private int awaitIntegerInput(int min, int max) {
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
     * Prints one single actions actionText to the console and inserts the role of the action before it.
     *
     * @param action the action of which the actionText should be printed.
     */
    public void printSingleActionText(Action action) {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException ex) {
            printErrorMessage(ex.getMessage());
        }
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

    public void printErrorMessage(String message) {
        System.err.print(message);
    }
}
