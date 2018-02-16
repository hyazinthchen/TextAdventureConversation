package conversation;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        DialogueState rootState = new DialogueGenerator().generateDialogue();

        TextAdventureConversation textAdventureConversation = new TextAdventureConversation(rootState);
        textAdventureConversation.start();

    }
}
