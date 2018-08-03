package npcDialogue.controller;

import npcDialogue.model.ParsingException;
import org.junit.Assert;
import org.junit.Test;

import static java.util.Arrays.asList;
import static junit.framework.TestCase.assertEquals;

public class DialogueLoaderTest {

    @Test
    public void testLoadNpcAttributes() throws ParsingException {
        DialogueLoader loader = new DialogueLoader();
        npcDialogue.model.NpcDialogueData dialogueData = loader.load("merchant1Dialogue.yml");

        assertEquals(new Integer(50), dialogueData.getNpcAttributes().getNpcAttributes().get("reputation"));
    }

    @Test
    public void testLoadStartAction() throws ParsingException {
        DialogueLoader loader = new DialogueLoader();
        npcDialogue.model.NpcDialogueData dialogueData = loader.load("merchant1Dialogue.yml");

        assertEquals("Welcome!", dialogueData.getStartAction().getActionText());
    }

    @Test
    public void testLoadActionTexts() throws ParsingException {
        DialogueLoader loader = new DialogueLoader();
        npcDialogue.model.NpcDialogueData dialogueData = loader.load("merchant1Dialogue.yml");

        npcDialogue.model.Action smallTalkPlayer1 = dialogueData.getStartAction().getTargetActionById("smallTalkPlayer1");
        npcDialogue.model.Action smallTalkPlayer2 = dialogueData.getStartAction().getTargetActionById("smallTalkPlayer2");
        npcDialogue.model.Action buySpecialPotion = smallTalkPlayer2.getTargetActionById("smallTalkNpc").getTargetActionById("buySpecialPotion");
        npcDialogue.model.Action buyPotion = smallTalkPlayer2.getTargetActionById("smallTalkNpc").getTargetActionById("buyPotion");
        npcDialogue.model.Action bye = buyPotion.getTargetActionById("bye");

        Assert.assertTrue(dialogueData.getStartAction().getTargetActions().containsAll(asList(smallTalkPlayer1, smallTalkPlayer2)));

        Assert.assertTrue(smallTalkPlayer1.getTargetActions().contains(smallTalkPlayer2.getTargetActionById("smallTalkNpc")));
        Assert.assertTrue(smallTalkPlayer2.getTargetActions().contains(smallTalkPlayer2.getTargetActionById("smallTalkNpc")));

        Assert.assertTrue(smallTalkPlayer2.getTargetActionById("smallTalkNpc").getTargetActions().containsAll(asList(buyPotion, buySpecialPotion)));

        Assert.assertTrue(buyPotion.getTargetActions().contains(bye));
    }


    @Test
    public void testLoadActionConditions() throws ParsingException {
        DialogueLoader loader = new DialogueLoader();
        npcDialogue.model.NpcDialogueData dialogueData = loader.load("merchant1Dialogue.yml");

        assertEquals(0, dialogueData.getStartAction().getConditions().size());
        assertEquals(0, dialogueData.getStartAction().getTargetActionById("smallTalkPlayer1").getConditions().size());
        assertEquals(0, dialogueData.getStartAction().getTargetActionById("smallTalkPlayer2").getConditions().size());
        assertEquals(0, dialogueData.getStartAction().getTargetActionById("smallTalkPlayer1").getTargetActionById("smallTalkNpc").getConditions().size());
        assertEquals(0, dialogueData.getStartAction().getTargetActionById("smallTalkPlayer2").getTargetActionById("smallTalkNpc").getTargetActionById("buyPotion").getConditions().size());
        assertEquals(1, dialogueData.getStartAction().getTargetActionById("smallTalkPlayer2").getTargetActionById("smallTalkNpc").getTargetActionById("buySpecialPotion").getConditions().size());

        assertEquals(60, dialogueData.getStartAction().getTargetActionById("smallTalkPlayer2").getTargetActionById("smallTalkNpc").getTargetActionById("buySpecialPotion").getConditions().get(0).getValue());
    }

    @Test
    public void testLoadNpcAttributeModifications() throws ParsingException {
        DialogueLoader loader = new DialogueLoader();
        npcDialogue.model.NpcDialogueData dialogueData = loader.load("merchant1Dialogue.yml");

        assertEquals(10, dialogueData.getStartAction().getTargetActionById("smallTalkPlayer1").getNpcAttributeModifications().get(0).getValue());
    }

    @Test(expected = ParsingException.class)
    public void testLoadNpcAttributesByException() throws ParsingException {
        DialogueLoader loader = new DialogueLoader();
        npcDialogue.model.NpcDialogueData dialogueData = loader.load("merchant2Dialogue.yml");
    }

    @Test(expected = ParsingException.class)
    public void testLoadActionGraphByException() throws ParsingException {
        DialogueLoader loader = new DialogueLoader();
        npcDialogue.model.NpcDialogueData dialogueData = loader.load("merchant3Dialogue.yml");
    }
}