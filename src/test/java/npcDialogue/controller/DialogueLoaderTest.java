package npcDialogue.controller;

import com.queomedia.commons.asserts.AssertUtil;
import npcDialogue.model.Action;
import npcDialogue.model.NpcDialogueData;
import npcDialogue.model.ParsingException;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.Arrays;

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

        AssertUtil.containsExact(Arrays.asList("The weather is nice today.", "I heard it will snow today."), dialogueData.getStartAction().getTargetActions(), Action.ACTION_BY_TEXT_EQUALS_CHECKER);
        AssertUtil.containsExact(Arrays.asList("I heard the sun will shine all day."), dialogueData.getStartAction().getTargetActionByName("smallTalkPlayer1").getTargetActions(), Action.ACTION_BY_TEXT_EQUALS_CHECKER);
        AssertUtil.containsExact(Arrays.asList("I heard the sun will shine all day."), dialogueData.getStartAction().getTargetActionByName("smallTalkPlayer2").getTargetActions(), Action.ACTION_BY_TEXT_EQUALS_CHECKER);
        AssertUtil.containsExact(Arrays.asList("I want to buy a potion.", "I want to buy a special potion."), dialogueData.getStartAction().getTargetActionByName("smallTalkPlayer2").getTargetActionByName("smallTalkNpc").getTargetActions(), Action.ACTION_BY_TEXT_EQUALS_CHECKER);
        AssertUtil.containsExact(Arrays.asList("Here you go. See you!"), dialogueData.getStartAction().getTargetActionByName("smallTalkPlayer2").getTargetActionByName("smallTalkNpc").getTargetActionByName("buyPotion").getTargetActions(), Action.ACTION_BY_TEXT_EQUALS_CHECKER);
    }

    @Test
    public void testLoadingActionConditions() throws FileNotFoundException, ParsingException {
        DialogueLoader loader = new DialogueLoader();
        NpcDialogueData dialogueData = loader.load(loader.getFileFromClassPath("merchant1Dialogue.yml"));

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