package npcDialogue;

import npcDialogue.controller.DialogueLoader;
import npcDialogue.model.NpcDialogueData;

/**
 * Entrypoint of Application
 */
public class ConversationTest {
    public static void main(String[] args) {

        //later: needs to be done at game start one time per NPC
        DialogueLoader dialogueLoader = new DialogueLoader();
        NpcDialogueData merchant1DialogueData = dialogueLoader.load("data/merchant1Dialogue.yml");

        //later: needs to be executed when a player starts to interact with an npc, each time
        // (storing state and NPC development etc in npcDialogueData)
        merchant1DialogueData.start();
    }
}
