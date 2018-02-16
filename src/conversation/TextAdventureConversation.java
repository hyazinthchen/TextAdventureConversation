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
        for (int j = 0; j < rootState.playerOptions.size(); j++) {
            if (playerInput == j + 1) {
                System.out.println("You: " + rootState.playerOptions.get(j).playerText);
                rootState.onExit(rootState.playerOptions.get(j));
                rootState = rootState.playerOptions.get(j).nextDialogueState;
                System.out.println("NPC: " + rootState.npcText);
                break;
            }
        }
    }
}
