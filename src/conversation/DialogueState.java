package conversation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DialogueState {

    private final String npcText;
    private final ArrayList<PlayerOption> playerOptions;

    public DialogueState(String npcText) {
        this.npcText = npcText;
        this.playerOptions = new ArrayList<>();
    }

    public void addPlayerOption(String label, String playerText, DialogueState targetDialogueState, boolean visible, boolean isStepBack) {
        this.playerOptions.add(new PlayerOption(label, playerText, this, targetDialogueState, false, visible, isStepBack));
    }


    public PlayerOption process(ConsoleDialogue ioProcessor) {
        System.out.format("\nNPC: \"%s\"\n\n", npcText);

        Map<Integer, PlayerOption> availableDialogueOptions = getAvailableOptions();
        ioProcessor.prettyPrintNumberedOptions(availableDialogueOptions);

        Integer selectedIndex = ioProcessor.waitForIntegerInput(1, availableDialogueOptions.size()); //blocks and waits
        PlayerOption selectedPlayerOption = availableDialogueOptions.get(selectedIndex);
        selectedPlayerOption.onPick();

        return selectedPlayerOption;
    }


    public boolean hasPickableOption() {
        for (PlayerOption playerOption : this.playerOptions) {
            if (!playerOption.isStepBack() && playerOption.hasPickableOption()) {
                return true;
            }
        }
        return false;
    }


    public boolean isFinalState() {
        return availableDialogueOptionCount() == 0;
    }


    private Map<Integer, PlayerOption> getAvailableOptions() {
        Map<Integer, PlayerOption> availableOptions = new HashMap<>();
        int i = 1;
        for (PlayerOption playerOption : this.playerOptions) { //numbering only the visible options from 1...n
            if (playerOption.isAvailable()) {
                availableOptions.put(i, playerOption);
                i++;
            }
        }
        return availableOptions;
    }

    public int availableDialogueOptionCount() {
        return getAvailableOptions().size();
    }

}
