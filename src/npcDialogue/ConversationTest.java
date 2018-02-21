package npcDialogue;

import npcDialogue.controller.DialogueLoader;
import npcDialogue.model.NpcDialogue;

/**
 * Entrypoint of Application
 */
public class ConversationTest {
    public static void main(String[] args) {
        DialogueLoader dialogueLoader = new DialogueLoader();
        NpcDialogue npcDialogue = dialogueLoader.load("data/merchant1Dialogue.yml"); //later: needs to be done at game start
        npcDialogue.start(); //later: needs to be executed when a player starts to interact with an npc
        //later: storing state of dialogue in db
    }
}
