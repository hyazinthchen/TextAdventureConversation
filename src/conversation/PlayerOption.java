package conversation;

public class PlayerOption {

    public String playerText;

    public DialogueState parentDialogueState;

    public DialogueState targetDialogueState;

    public boolean picked;

    public boolean invisible;

    public boolean isStepBack;

    public PlayerOption(String playerText, DialogueState parentDialogueState, DialogueState targetDialogueState,
                        boolean picked, boolean invisible, boolean isStepBack) {
        this.playerText = playerText;
        this.parentDialogueState = parentDialogueState;
        this.targetDialogueState = targetDialogueState;
        this.picked = picked;
        this.invisible = invisible;
        this.isStepBack = isStepBack;
    }

    public boolean hasUnpickedChildren(){
        return !this.picked && targetDialogueState.hasUnpickedChildren();
    }
}
