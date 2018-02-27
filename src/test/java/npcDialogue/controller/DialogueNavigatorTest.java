package npcDialogue.controller;

import npcDialogue.model.InvalidStateException;
import npcDialogue.model.NpcDialogueData;
import org.junit.Test;

import java.io.FileNotFoundException;

public class DialogueNavigatorTest {
    @Test
    public void testStart() throws FileNotFoundException, InvalidStateException {

        DialogueLoader dialogueLoader = new DialogueLoader();
        NpcDialogueData dialogueData = dialogueLoader.load("src/main/resources/merchant1Dialogue.yml");
        dialogueData.start();
        //TODO: how to test DialogueNavigator.start() ?
    }

}