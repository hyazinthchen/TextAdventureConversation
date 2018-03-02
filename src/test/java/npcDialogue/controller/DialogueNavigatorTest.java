package npcDialogue.controller;

import npcDialogue.model.NpcDialogueData;
import org.junit.Test;

import java.io.FileNotFoundException;

import static junit.framework.TestCase.assertEquals;

public class DialogueNavigatorTest {
    @Test
    public void testNavigateByLastCurrentAction() throws FileNotFoundException {
        DialogueLoader loader = new DialogueLoader();
        NpcDialogueData dialogueData = loader.load(loader.getFileFromClassPath("merchant2Dialogue.yml"));
        dialogueData.start();
        DialogueNavigator navigator = dialogueData.getDialogueNavigator();

        assertEquals("I don't want to sell anything to you!", navigator.getCurrentAction().getActionText());
    }
}