package npcDialogue.view;

import npcDialogue.model.Action;
import npcDialogue.model.ActorType;

import java.util.Scanner;

/**
 * Responsible for reading the Players input from the console and printing ActionTexts to the console
 */
public class ConsoleInputOutput { //TODO rename
    private static final Scanner SCANNER = new Scanner(System.in);


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


    public void printNumberedOptions(Action currentAction) {
        int i = 0;
        for (Action targetAction : currentAction.getTargetActions()) {
            System.out.println("[" + i + "] " + targetAction.getActionText());
            i++;
        }
    }

    public void printSingleAction(Action currentAction) {
        if (currentAction.getActorType() == ActorType.PLAYER) {
            System.out.println("You: " + currentAction.getActionText());
        } else {
            System.out.println("NPC: " + currentAction.getActionText());
        }
    }

}
