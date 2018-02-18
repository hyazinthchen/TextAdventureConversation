package conversation;

public class PlayerOption implements DialogueOption {

    private final String playerText;
    private final DialogueState parentDialogueState;


    private final DialogueState targetDialogueState;
    private boolean picked;
    private final boolean invisible;
    private final boolean isStepBack;

    public PlayerOption(String playerText, DialogueState parentDialogueState, DialogueState targetDialogueState,
                        boolean picked, boolean invisible, boolean isStepBack) { //TODO implement better strategy for cycles
        this.playerText = playerText;
        this.parentDialogueState = parentDialogueState;
        this.targetDialogueState = targetDialogueState;
        this.picked = picked;
        this.invisible = invisible;
        this.isStepBack = isStepBack;
    }

    public void onPick() {
        picked = true;
    }


    public boolean hasPickableOption() {
        return !this.picked && targetDialogueState.hasPickableOption();
    }


    public boolean isVisible() {
        return !invisible;
    }


    public boolean isStepBack() {
        return isStepBack;
    }

    public DialogueState getTargetDialogueState() {
        return targetDialogueState;
    }

    @Override
    public String getLabel() {
        return playerText;
    }

    @Override
    public boolean isAvailable() {
        return isVisible() && hasPickableOption();
    }
}
