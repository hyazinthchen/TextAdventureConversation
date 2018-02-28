package npcDialogue.view;

import com.queomedia.commons.checks.Check;
import npcDialogue.model.Action;
import npcDialogue.model.ActorType;

import java.util.List;
import java.util.Scanner;

/**
 * Responsible for reading the players input from the console and printing ActionTexts to the console.
 */
public class ConsoleInputOutput { //TODO rename
    private static final Scanner SCANNER = new Scanner(System.in);

    public Action choose(List<Action> availableActions) {
        Check.notNullArgument(availableActions, "availableActions");
        if (availableActions.isEmpty()) {
            throw new IllegalArgumentException("availableActions list must have at least one element");
        }
        int i = 0;
        for (Action availableAction : availableActions) {
            System.out.println("[" + i + "] " + availableAction.getActionText());
            i++;
        }
        return availableActions.get(awaitIntegerInput(0, availableActions.size() - 1));

    }

    /**
     * Waits for the players input and reads it.
     *
     * @param min
     * @param max
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
     * Prints the actionTexts of players choices to the console and numbers these choices.
     *
     * @param action
     */
    public void printNumberedOptions(Action action) {
        int i = 0;
        for (Action targetAction : action.getTargetActions()) {
            System.out.println("[" + i + "] " + targetAction.getActionText());
            i++;
        }
    }

    /**
     * Prints the actionText of one Action to the console.
     *
     * @param action
     */
    public void printSingleAction(Action action) {
        if (action.getActorType() == ActorType.PLAYER) {
            System.out.println("You: " + action.getActionText());
        } else {
            System.out.println("NPC: " + action.getActionText());
        }
    }

}
