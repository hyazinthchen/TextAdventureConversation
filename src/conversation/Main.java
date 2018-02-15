package conversation;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        DialogueLoader dialogueLoader = new DialogueLoader();

        DialogueState rootState = dialogueLoader.load();

        TextAdventureConversation textAdventureConversation = new TextAdventureConversation(rootState);
        textAdventureConversation.start();

    }
}
