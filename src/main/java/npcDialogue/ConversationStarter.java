package npcDialogue;

import npcDialogue.controller.DialogueLoader;
import npcDialogue.model.NpcDialogueData;

/**
 * Represents the entry point of the application.
 */
public class ConversationStarter {
    public static void main(String[] args){

        DialogueLoader dialogueLoader = new DialogueLoader();
        NpcDialogueData dialogueData = dialogueLoader.load("merchant1Dialogue.yml");

        //TODO: do something with dialogueData
    }
}
