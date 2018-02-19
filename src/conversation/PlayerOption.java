package conversation;

public class PlayerOption implements DialogueOption {

    private final String label;
    private final String playerText;
    private final DialogueState sourceDialogueState;


    private final DialogueState destinationDialogueState;
    private boolean picked;
    private final boolean visible;
    private final boolean isStepBack;

    public PlayerOption(String label, String playerText, DialogueState sourceDialogueState, DialogueState destinationDialogueState,
                        boolean picked, boolean visible, boolean isStepBack) { //TODO implement better strategy for cycles
        this.label = label;
        this.playerText = playerText;
        this.sourceDialogueState = sourceDialogueState;
        this.destinationDialogueState = destinationDialogueState;
        this.picked = picked;
        this.visible = visible;
        this.isStepBack = isStepBack;
    }

    public void onPick() {
        picked = true;
    }


    public boolean hasPickableOption() {
        return !this.picked && destinationDialogueState.hasPickableOption();
    }


    public boolean isVisible() {
        return visible;
    }


    public boolean isStepBack() {
        return isStepBack;
    }

    public DialogueState getDestinationDialogueState() {
        return destinationDialogueState;
    }

    @Override
    public String getLabel() {
        return label;
    }

    public String getPlayerText() {
        return playerText;
    }

    @Override
    public boolean isAvailable() {
        return isVisible() && hasPickableOption();
    }
}
