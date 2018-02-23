package npcDialogue;

import npcDialogue.controller.DialogueLoader;
import npcDialogue.model.NpcDialogueData;

import java.io.IOException;

/**
 * Entrypoint of Application
 */
public class ConversationStarter {
    public static void main(String[] args) throws IOException {

        //later: needs to be done at game start one time per NPC
        DialogueLoader dialogueLoader = new DialogueLoader();
        NpcDialogueData merchant1DialogueData = dialogueLoader.load("src/main/resources/merchant1Dialogue.yml");

        //later: needs to be executed when a player starts to interact with an npc, each time
        // (storing state and NPC development etc in npcDialogueData)
        merchant1DialogueData.start();
    }
}
