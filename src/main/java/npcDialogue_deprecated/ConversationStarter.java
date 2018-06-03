package npcDialogue_deprecated;

import npcDialogue_deprecated.controller.DialogueLoader;
import npcDialogue_deprecated.model.NpcDialogueData;
import npcDialogue_deprecated.model.ParsingException;

import java.io.IOException;

/**
 * Represents the entry point of the application.
 */
public class ConversationStarter {
    public static void main(String[] args) throws IOException, ParsingException{

        //later: needs to be done at game start one time per NPC
        DialogueLoader dialogueLoader = new DialogueLoader();
        NpcDialogueData dialogueData = dialogueLoader.load(dialogueLoader.getFileFromClassPath("merchant1Dialogue.yml"));

        //later: needs to be executed when a player starts to interact with an npc each time
        dialogueData.start();
    }


}
