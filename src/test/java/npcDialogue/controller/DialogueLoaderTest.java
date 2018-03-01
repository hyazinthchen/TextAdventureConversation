package npcDialogue.controller;

import com.queomedia.commons.asserts.AssertUtil;
import npcDialogue.model.Action;
import npcDialogue.model.InvalidStateException;
import npcDialogue.model.NpcDialogueData;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.Arrays;

import static junit.framework.TestCase.assertEquals;

public class DialogueLoaderTest {

    @Test
    public void testLoadingNpcTraits() throws FileNotFoundException, InvalidStateException {
        DialogueLoader loader = new DialogueLoader();
        NpcDialogueData dialogueData = loader.load(loader.getFileFromClassPath("merchant1Dialogue.yml"));

        assertEquals(0, dialogueData.getNpcTraits().getTraits().get("bribePaid"));
        assertEquals(50, dialogueData.getNpcTraits().getTraits().get("reputation"));
    }

    @Test
    public void testLoadingStartAction() throws FileNotFoundException, InvalidStateException {
        DialogueLoader loader = new DialogueLoader();
        NpcDialogueData dialogueData = loader.load(loader.getFileFromClassPath("merchant1Dialogue.yml"));

        assertEquals("Welcome!", dialogueData.getStartAction().getActionText());
    }

    @Test
    public void testLoadingActionTexts() throws FileNotFoundException, InvalidStateException {
        DialogueLoader loader = new DialogueLoader();
        NpcDialogueData dialogueData = loader.load(loader.getFileFromClassPath("merchant1Dialogue.yml"));

        AssertUtil.containsExact(Arrays.asList("The weather is nice today.", "I heard it will snow today."), dialogueData.getStartAction().getTargetActions(), Action.ACTION_BY_TEXT_EQUALS_CHECKER);
        AssertUtil.containsExact(Arrays.asList("I heard the sun will shine all day."), dialogueData.getStartAction().getTargetActionAt(0).getTargetActions(), Action.ACTION_BY_TEXT_EQUALS_CHECKER);
        AssertUtil.containsExact(Arrays.asList("I heard the sun will shine all day."), dialogueData.getStartAction().getTargetActionAt(1).getTargetActions(), Action.ACTION_BY_TEXT_EQUALS_CHECKER);
        AssertUtil.containsExact(Arrays.asList("I want to buy a potion.", "I want to buy a special potion."), dialogueData.getStartAction().getTargetActionAt(0).getTargetActionAt(0).getTargetActions(), Action.ACTION_BY_TEXT_EQUALS_CHECKER);
        AssertUtil.containsExact(Arrays.asList("Here you go. See you!"), dialogueData.getStartAction().getTargetActionAt(0).getTargetActionAt(0).getTargetActionAt(0).getTargetActions(), Action.ACTION_BY_TEXT_EQUALS_CHECKER);
    }

    @Test
    public void testLoadingActionDependencies() throws FileNotFoundException, InvalidStateException {
        DialogueLoader loader = new DialogueLoader();
        NpcDialogueData dialogueData = loader.load(loader.getFileFromClassPath("merchant1Dialogue.yml"));

        assertEquals(0, dialogueData.getStartAction().getActionDependencies().size());
        assertEquals(0, dialogueData.getStartAction().getTargetActionAt(0).getActionDependencies().size());
        assertEquals(0, dialogueData.getStartAction().getTargetActionAt(1).getActionDependencies().size());
        assertEquals(0, dialogueData.getStartAction().getTargetActionAt(0).getTargetActionAt(0).getActionDependencies().size());
        assertEquals(0, dialogueData.getStartAction().getTargetActionAt(0).getTargetActionAt(0).getActionDependencies().size());
        assertEquals(0, dialogueData.getStartAction().getTargetActionAt(0).getTargetActionAt(0).getTargetActionAt(0).getActionDependencies().size());
        assertEquals(1, dialogueData.getStartAction().getTargetActionAt(0).getTargetActionAt(0).getTargetActionAt(1).getActionDependencies().size());
        assertEquals(0, dialogueData.getStartAction().getTargetActionAt(0).getTargetActionAt(0).getTargetActionAt(1).getTargetActionAt(0).getActionDependencies().size());

        assertEquals(60, dialogueData.getStartAction().getTargetActionAt(0).getTargetActionAt(0).getTargetActionAt(1).getActionDependencies().get("reputation"));
    }

}