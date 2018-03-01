package npcDialogue.controller;

import npcDialogue.model.InvalidStateException;
import npcDialogue.model.NpcDialogueData;
import org.junit.Test;

import java.io.FileNotFoundException;

import static junit.framework.TestCase.assertEquals;

public class DialogueNavigatorTest {
    @Test
    public void testStartByLastCurrentAction() throws FileNotFoundException, InvalidStateException {
        DialogueLoader loader = new DialogueLoader();
        NpcDialogueData dialogueData = loader.load(loader.getFileFromClassPath("merchant2Dialogue.yml"));
        dialogueData.start();
        DialogueNavigator navigator = dialogueData.getDialogueNavigator();

        assertEquals("Here you go. See you!", navigator.getCurrentAction().getActionText());
    }

}