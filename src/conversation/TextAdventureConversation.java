package conversation;


public class TextAdventureConversation {

    private final DialogueState rootState;

    public TextAdventureConversation(DialogueState rootState) {
        this.rootState = rootState;
    }


    public void start() {
        //TODO print general intro text ? "once upon a time..."
        System.out.println("Once upon a time...\n\n");

        InputOutputProcessor inputOutputProcessor = new InputOutputProcessor();

        DialogueState currentDialogueState = rootState;

        while (!currentDialogueState.isFinalState()) {
            PlayerOption selectedDialogueOption = currentDialogueState.process(inputOutputProcessor);
            currentDialogueState = selectedDialogueOption.getDestinationDialogueState();
        }
    }


}
