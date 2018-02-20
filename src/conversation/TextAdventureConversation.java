package conversation;


public class TextAdventureConversation {

    private final DialogueState rootState;

    public TextAdventureConversation(DialogueState rootState) {
        this.rootState = rootState;
    }


    public void start() {
        InputOutputProcessor inputOutputProcessor = new InputOutputProcessor();

        DialogueState currentDialogueState = rootState;

        while (!currentDialogueState.isFinalState()) {
            PlayerOption selectedDialogueOption = currentDialogueState.process(inputOutputProcessor);
            currentDialogueState = selectedDialogueOption.getDestinationDialogueState();
        }
        if (currentDialogueState.isFinalState()) {
            System.out.format("\nNPC: \"%s\"\n\n", currentDialogueState.getNpcText());
        }
    }


}
