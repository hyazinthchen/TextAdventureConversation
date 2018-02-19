package conversation;


public class TextAdventureConversation extends ConsoleDialogue {

    private final DialogueState rootState;

    public TextAdventureConversation(DialogueState rootState) {
        this.rootState = rootState;
    }


    public void start() {
        //TODO print general intro text ? "once upon a time..."
        System.out.println("Once upon a time...\n\n");

        DialogueState currentDialogueState = rootState;

        while (!currentDialogueState.isFinalState()) {
            PlayerOption selectedDialogueOption = currentDialogueState.process(this);
            currentDialogueState = selectedDialogueOption.getDestinationDialogueState();
        }
    }


}
