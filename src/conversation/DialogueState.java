package conversation;

import java.util.ArrayList;

public class DialogueState {

    public String npcText;

    public ArrayList<PlayerOption> playerOptions;

    public DialogueState(String npcText) {
        this.npcText = npcText;
    }

    public DialogueState(String npcText, ArrayList<PlayerOption> playerOptions) {
        this.npcText = npcText;
        this.playerOptions = playerOptions;
    }

    public void addPlayerOptions(ArrayList<PlayerOption> playerOptions) {
        this.playerOptions = playerOptions;
    }
}
