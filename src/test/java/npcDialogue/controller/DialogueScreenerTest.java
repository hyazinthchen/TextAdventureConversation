package npcDialogue.controller;

import com.queomedia.commons.asserts.AssertUtil;
import npcDialogue.model.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class DialogueScreenerTest {
    /**
     * /**
     * Helper method for generating a simple action without any conditions for test purposes.
     *
     * @param actionText the actionText of the new action.
     * @return a new action object.
     */
    private Action generateTestAction(String actionText) {
        return new NpcAction(Role.NPC, actionText, actionText);
    }

    /**
     * Action B will have no available targetActions. B is the end.
     */
    @Test
    public void testScreenForEndActions_linearDialogue() {
        NpcAttributes attributes = new NpcAttributes();
        attributes.addAttribute("reputation", 50);

        Action actionA = generateTestAction("A");
        Action actionB = generateTestAction("B");
        Action actionC = generateTestAction("C");
        actionA.addTargetAction(actionB);
        actionB.addTargetAction(actionC);

        actionC.addActionCondition("reputation", 60);

        NpcDialogueData dialogueData = new NpcDialogueData(attributes, actionA);
        List<Action> deadEndActions = new DialogueScreener(dialogueData).screenForEndActions();

        AssertUtil.containsExact(Arrays.asList("B"), deadEndActions, Action.ACTION_BY_TEXT_EQUALS_CHECKER);
    }

    /**
     * Action B will have no available targetActions. C the only available end.
     */
    @Test
    public void testScreenForEndActions_branchedDialogue() {
        NpcAttributes attributes = new NpcAttributes();
        attributes.addAttribute("reputation", 50);

        Action actionA = generateTestAction("A");
        Action actionB = generateTestAction("B");
        Action actionC = generateTestAction("C");
        actionA.addTargetAction(actionB);
        actionA.addTargetAction(actionC);

        actionB.addActionCondition("reputation", 60);

        NpcDialogueData dialogueData = new NpcDialogueData(attributes, actionA);
        List<Action> deadEndActions = new DialogueScreener(dialogueData).screenForEndActions();

        AssertUtil.containsExact(Arrays.asList("C"), deadEndActions, Action.ACTION_BY_TEXT_EQUALS_CHECKER);
    }

}