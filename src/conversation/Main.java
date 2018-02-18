package conversation;


public class Main {
    public static void main(String[] args) {

        DialogueState rootState = new DialogueGenerator().generateDialogue();

        TextAdventureConversation textAdventureConversation = new TextAdventureConversation(rootState);
        textAdventureConversation.start();

    }
}
