package npcDialogue.view;

import conversation_deprecated.PlayerOption;

import java.util.Map;
import java.util.Scanner;

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


    public void printNumberedOptions(Map<Integer, PlayerOption> options) {
        options.forEach(
                (number, option) -> System.out.format("   [%s] - %s", number, option.getLabel())
        );
    }

}
