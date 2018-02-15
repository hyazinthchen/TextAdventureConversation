package conversation;

public class PlayerOption {

    public String playerText;

    public DialogueState previousDialogueState;

    public DialogueState nextDialogueState;

    public Boolean traversed;

    public Boolean invisible;

    public PlayerOption(String playerText, DialogueState previousDialogueState, DialogueState nextDialogueState) {
        this.playerText = playerText;
        this.previousDialogueState = previousDialogueState;
        this.nextDialogueState = nextDialogueState;
    }

    public PlayerOption(String playerText, DialogueState previousDialogueState, DialogueState nextDialogueState,
            Boolean traversed) {
        this.playerText = playerText;
        this.previousDialogueState = previousDialogueState;
        this.nextDialogueState = nextDialogueState;
        this.traversed = traversed;
    }

    public PlayerOption(String playerText, DialogueState previousDialogueState, DialogueState nextDialogueState,
            Boolean traversed, Boolean invisible) {
        this.playerText = playerText;
        this.previousDialogueState = previousDialogueState;
        this.nextDialogueState = nextDialogueState;
        this.traversed = traversed;
        this.invisible = invisible;
    }
}
