package conversation;

import java.util.ArrayList;

public class DialogueState {

    public String npcText;

    public ArrayList<PlayerOption> playerOptions;

    public DialogueState(String npcText) {
        this.npcText = npcText;
        this.playerOptions = new ArrayList<>();
    }

    public void addPlayerOption(String playerText, DialogueState nextDialogueState, boolean invisible, boolean isStepBack) {
        this.playerOptions.add(new PlayerOption(playerText, this, nextDialogueState, false, invisible, isStepBack));
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
