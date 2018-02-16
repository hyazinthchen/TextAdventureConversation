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

    public void addPlayerOption(PlayerOption playerOption) {
        this.playerOptions.add(playerOption);
    }

    public boolean hasUnpickedChildren() {
        for (PlayerOption playerOption :
                this.playerOptions) {
            if(!playerOption.isStepBack){
                if(playerOption.hasUnpickedChildren()){
                    return true;
                }
            }
        }
        return false;
    }

    public void onExit(PlayerOption playerOption){
        playerOption.picked = true;
    }

    public void onEnter(){
        int i = 1;
        for(PlayerOption playerOption : this.playerOptions){
            if(!playerOption.invisible && playerOption.hasUnpickedChildren()){
                System.out.println("[" + i + "] " + playerOption.playerText);
                i++;
            }
        }
    }
}
