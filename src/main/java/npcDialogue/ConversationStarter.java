package npcDialogue;

import npcDialogue.controller.DialogueLoader;
import npcDialogue.model.NpcDialogueData;
import npcDialogue.model.ParsingException;

/**
 * Represents the entry point of the application.
 */
public class ConversationStarter {
    public static void main(String[] args) throws ParsingException {

        DialogueLoader dialogueLoader = new DialogueLoader();
        NpcDialogueData dialogueData = dialogueLoader.load("merchant1Dialogue.yml");

        //TODO: do something with dialogueData
    }
}
