package conversation;

public class PlayerOption {

    public String playerText;

    public DialogueState previousDialogueState;

    public DialogueState nextDialogueState;

    public boolean picked;

    public boolean invisible;

    public boolean isStepBack;

    public PlayerOption(String playerText, DialogueState previousDialogueState, DialogueState nextDialogueState,
                        boolean picked, boolean invisible, boolean isStepBack) {
        this.playerText = playerText;
        this.previousDialogueState = previousDialogueState;
        this.nextDialogueState = nextDialogueState;
        this.picked = picked;
        this.invisible = invisible;
        this.isStepBack = isStepBack;
    }

    public boolean hasUnpickedChildren(){
        return !this.picked && nextDialogueState.hasUnpickedChildren();
    }
}
