package npcDialogue;

import npcDialogue.controller.DialogueLoader;
import npcDialogue.model.InvalidStateException;
import npcDialogue.model.NpcDialogueData;

import java.io.IOException;

/**
 * Represents the entry point of the application.
 */
public class ConversationStarter {
    public static void main(String[] args) throws IOException, InvalidStateException {

        //later: needs to be done at game start one time per NPC
        DialogueLoader dialogueLoader = new DialogueLoader();
        NpcDialogueData dialogueData = dialogueLoader.load(dialogueLoader.getFileFromClassPath("merchant1Dialogue.yml"));

        //later: needs to be executed when a player starts to interact with an npc, each time
        // (storing state and NPC development etc. in npcDialogueData)
        dialogueData.start();
    }


}