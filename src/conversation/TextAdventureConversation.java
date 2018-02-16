package conversation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TextAdventureConversation {

    DialogueState rootState;

    public TextAdventureConversation(DialogueState rootState) {
        this.rootState = rootState;
    }

    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public void start() throws IOException {
        System.out.println("NPC: " + rootState.npcText);
        while (rootState.playerOptions != null) {
            rootState.onEnter();
            Integer playerInput = Integer.parseInt(reader.readLine());
            printPlayerChoiceAndNpcAnswer(playerInput);
        }
    }

    private void printPlayerChoiceAndNpcAnswer(Integer playerInput) {
        for (int i = 0; i < rootState.playerOptions.size(); i++) {
            if (playerInput == i + 1) {
                System.out.println("You: " + rootState.playerOptions.get(i).playerText);
                rootState.onExit(rootState.playerOptions.get(i));
                rootState = rootState.playerOptions.get(i).targetDialogueState;
                System.out.println("NPC: " + rootState.npcText);
                break;
            }
        }
    }
}
