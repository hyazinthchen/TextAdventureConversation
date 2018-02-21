package conversation_deprecated;

public class PlayerOption {

    private final String label;
    private final String playerText;
    private final DialogueState sourceDialogueState;


    private final DialogueState destinationDialogueState;
    private boolean picked;
    private final boolean visible;

    public PlayerOption(String label, String playerText, DialogueState sourceDialogueState, DialogueState destinationDialogueState,
                        boolean picked, boolean visible) { //TODO implement better strategy for cycles
        this.label = label;
        this.playerText = playerText;
        this.sourceDialogueState = sourceDialogueState;
        this.destinationDialogueState = destinationDialogueState;
        this.picked = picked;
        this.visible = visible;
    }

    public void onPick() {
        picked = true;
    }

    public boolean isStepBack() { //TODO implement correct logic
        if (ConversationEngine.TRAVERSED_PLAYER_ACTIONS.contains(this)) {
            return true;
        }
        return false;
    }

    public boolean isAvailable() {
        return isVisible() && hasPickableOption();
    }

    public boolean isVisible() {
        return visible;
    }

    public boolean hasPickableOption() {
        return !this.picked && destinationDialogueState.hasPickableOption();
    }

    public DialogueState getDestinationDialogueState() {
        return destinationDialogueState;
    }

    public String getLabel() {
        return label;
    }

    public String getPlayerText() {
        return playerText;
    }

}
