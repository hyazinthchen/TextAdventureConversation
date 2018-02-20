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

    public String getNpcText() {
        return npcText;
    }

    public void addPlayerOption(String label, String playerText, DialogueState targetDialogueState, boolean visible) {
        this.playerOptions.add(new PlayerOption(label, playerText, this, targetDialogueState, false, visible));
    }


    public PlayerOption process(InputOutputProcessor inputOutputProcessor) {
        System.out.format("\nNPC: \"%s\"\n\n", npcText);

        Map<Integer, PlayerOption> availableDialogueOptions = getAvailableOptions();
        inputOutputProcessor.prettyPrintNumberedOptions(availableDialogueOptions);

        Integer selectedIndex = inputOutputProcessor.waitForIntegerInput(1, availableDialogueOptions.size()); //blocks and waits
        PlayerOption selectedPlayerOption = availableDialogueOptions.get(selectedIndex);
        selectedPlayerOption.onPick();
        System.out.format("\nYou: \"%s\"\n\n", selectedPlayerOption.getPlayerText());

        return selectedPlayerOption;
    }


    public boolean hasPickableOption() {
        for (PlayerOption playerOption : this.playerOptions) {
            ConversationEngine.traversedPlayerOptions.add(playerOption);
            if (!playerOption.isStepBack() && playerOption.hasPickableOption()) {
                return true;
            }
        }
        return false;
    }


    public boolean isFinalState() {
        return availableDialogueOptionCount() == 0;
    }

    public int availableDialogueOptionCount() {
        return getAvailableOptions().size();
    }

    private Map<Integer, PlayerOption> getAvailableOptions() {

        /*ArrayList<PlayerOption> availablePlayerOptions = new ArrayList<>();
        for (PlayerOption playerOption : this.playerOptions){
            if(playerOption.isAvailable()){
                availablePlayerOptions.add(playerOption);
            }
        }
        return availablePlayerOptions;*/


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

}
