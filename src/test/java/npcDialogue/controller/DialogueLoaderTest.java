package npcDialogue.controller;

import npcDialogue.model.Action;
import npcDialogue.model.NpcDialogueData;
import npcDialogue.model.ParsingException;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;

import static java.util.Arrays.asList;
import static junit.framework.TestCase.assertEquals;

public class DialogueLoaderTest {

    @Test
    public void testLoadNpcAttributes() throws FileNotFoundException, ParsingException {
        DialogueLoader loader = new DialogueLoader();
        NpcDialogueData dialogueData = loader.load(loader.getFileFromClassPath("merchant1Dialogue.yml"));

        assertEquals(0, dialogueData.getNpcAttributes().getNpcAttributes().get("bribePaid"));
        assertEquals(50, dialogueData.getNpcAttributes().getNpcAttributes().get("reputation"));
    }

    @Test
    public void testLoadingStartAction() throws FileNotFoundException, ParsingException {
        DialogueLoader loader = new DialogueLoader();
        NpcDialogueData dialogueData = loader.load(loader.getFileFromClassPath("merchant1Dialogue.yml"));

        assertEquals("Welcome!", dialogueData.getStartAction().getActionText());
    }


    @Test
    public void testLoadingActionTexts() throws FileNotFoundException, ParsingException {
        DialogueLoader loader = new DialogueLoader();
        NpcDialogueData dialogueData = loader.load(loader.getFileFromClassPath("merchant1Dialogue.yml"));

        Action smallTalkPlayer1 = dialogueData.getStartAction().getTargetActionByName("smallTalkPlayer1");
        Action smallTalkPlayer2 = dialogueData.getStartAction().getTargetActionByName("smallTalkPlayer2");
        Action buySpecialPotion = smallTalkPlayer2.getTargetActionByName("smallTalkNpc").getTargetActionByName("buySpecialPotion");
        Action buyPotion = smallTalkPlayer2.getTargetActionByName("smallTalkNpc").getTargetActionByName("buyPotion");
        Action bye = buyPotion.getTargetActionByName("bye");

        Assert.assertTrue(dialogueData.getStartAction().getTargetActions().containsAll(asList(smallTalkPlayer1, smallTalkPlayer2)));

        Assert.assertTrue(smallTalkPlayer1.getTargetActions().contains(smallTalkPlayer2.getTargetActionByName("smallTalkNpc")));
        Assert.assertTrue(smallTalkPlayer2.getTargetActions().contains(smallTalkPlayer2.getTargetActionByName("smallTalkNpc")));

        Assert.assertTrue(smallTalkPlayer2.getTargetActionByName("smallTalkNpc").getTargetActions().containsAll(asList(buyPotion, buySpecialPotion)));

        Assert.assertTrue(buyPotion.getTargetActions().contains(bye));
    }


    @Test
    public void testLoadingActionConditions() throws FileNotFoundException, ParsingException {
        DialogueLoader loader = new DialogueLoader();
        NpcDialogueData dialogueData = new DialogueLoader().load(loader.getFileFromClassPath("merchant1Dialogue.yml"));

        assertEquals(0, dialogueData.getStartAction().getActionConditions().size());
        assertEquals(0, dialogueData.getStartAction().getTargetActionByName("smallTalkPlayer1").getActionConditions().size());
        assertEquals(0, dialogueData.getStartAction().getTargetActionByName("smallTalkPlayer2").getActionConditions().size());
        assertEquals(0, dialogueData.getStartAction().getTargetActionByName("smallTalkPlayer1").getTargetActionByName("smallTalkNpc").getActionConditions().size());
        assertEquals(0, dialogueData.getStartAction().getTargetActionByName("smallTalkPlayer2").getTargetActionByName("smallTalkNpc").getTargetActionByName("buyPotion").getActionConditions().size());
        assertEquals(1, dialogueData.getStartAction().getTargetActionByName("smallTalkPlayer2").getTargetActionByName("smallTalkNpc").getTargetActionByName("buySpecialPotion").getActionConditions().size());

        assertEquals(60, dialogueData.getStartAction().getTargetActionByName("smallTalkPlayer2").getTargetActionByName("smallTalkNpc").getTargetActionByName("buySpecialPotion").getActionConditions().get("reputation"));
    }

    @Test
    public void testLoadingNpcAttributeModifications() throws FileNotFoundException, ParsingException {
        DialogueLoader loader = new DialogueLoader();
        NpcDialogueData dialogueData = loader.load(loader.getFileFromClassPath("merchant1Dialogue.yml"));

        assertEquals(60, dialogueData.getStartAction().getTargetActionByName("smallTalkPlayer1").getNpcAttributeModifications().get("reputation"));
    }
}