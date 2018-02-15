package conversation;

public class DialogueLoader {
    public DialogueState load() {
        DialogueGenerator dialogueGenerator = new DialogueGenerator();
        DialogueState rootState = dialogueGenerator.generateDialogue();
        return rootState;
    }
}
