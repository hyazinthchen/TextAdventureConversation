package npcDialogue.controller;

import com.queomedia.commons.asserts.AssertUtil;
import npcDialogue.model.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class DialogueScreenerTest {
    /**
     * Helper method for generating a simple action without any conditions for test purposes.
     *
     * @param actionText the actionText of the new action.
     * @return a new action object.
     */
    private Action generateTestAction(String actionText) {
        return new NpcAction(Role.NPC, actionText, actionText);
    }

    /**
     * A[B], B[C], conditions for C will not be fulfilled
     */
    @Test
    public void testScreenForLeaves_linearDialogue() {
        NpcAttributes attributes = new NpcAttributes();
        attributes.addAttribute("reputation", 50);

        Action actionA = generateTestAction("A");
        Action actionB = generateTestAction("B");
        Action actionC = generateTestAction("C");
        actionA.addTargetAction(actionB);
        actionB.addTargetAction(actionC);

        actionC.addActionCondition("reputation", 60);

        NpcDialogueData dialogueData = new NpcDialogueData(attributes, actionA);
        List<Action> leaves = new DialogueScreener(dialogueData).screenForLeaves();

        AssertUtil.containsExact(Arrays.asList("B"), leaves, Action.ACTION_BY_TEXT_EQUALS_CHECKER);
    }

    /**
     * A[B, C], B[D], conditions for D will not be fulfilled
     */
    @Test
    public void testScreenForLeaves_branchedDialogue() {
        NpcAttributes attributes = new NpcAttributes();
        attributes.addAttribute("reputation", 50);

        Action actionA = generateTestAction("A");
        Action actionB = generateTestAction("B");
        Action actionC = generateTestAction("C");
        Action actionD = generateTestAction("D");
        actionA.addTargetAction(actionB);
        actionA.addTargetAction(actionC);
        actionB.addTargetAction(actionD);

        actionD.addActionCondition("reputation", 60);

        NpcDialogueData dialogueData = new NpcDialogueData(attributes, actionA);
        List<Action> leaves = new DialogueScreener(dialogueData).screenForLeaves();

        AssertUtil.containsExact(Arrays.asList("B", "C"), leaves, Action.ACTION_BY_TEXT_EQUALS_CHECKER);
    }

    /**
     * A[B, C], B[D], C[D], conditions for B and C will not be fulfilled
     */
    @Test
    public void testScreenForEndActions_noWayThrough() {
        NpcAttributes attributes = new NpcAttributes();
        attributes.addAttribute("reputation", 50);

        Action actionA = generateTestAction("A");
        Action actionB = generateTestAction("B");
        Action actionC = generateTestAction("C");
        Action actionD = generateTestAction("D");
        actionA.addTargetAction(actionB);
        actionA.addTargetAction(actionC);
        actionB.addTargetAction(actionD);
        actionC.addTargetAction(actionD);

        actionB.addActionCondition("reputation", 60);
        actionC.addActionCondition("reputation", 70);

        NpcDialogueData dialogueData = new NpcDialogueData(attributes, actionA);

        AssertUtil.isEmpty(new DialogueScreener(dialogueData).screenForEndActions());
    }

    /**
     * A[B, C], conditions for B will not be fulfilled
     */
    @Test
    public void testScreenForEndActions_oneWayThrough() {
        NpcAttributes attributes = new NpcAttributes();
        attributes.addAttribute("reputation", 50);

        Action actionA = generateTestAction("A");
        Action actionB = generateTestAction("B");
        Action actionC = generateTestAction("C");
        actionA.addTargetAction(actionB);
        actionA.addTargetAction(actionC);

        actionB.addActionCondition("reputation", 60);

        NpcDialogueData dialogueData = new NpcDialogueData(attributes, actionA);

        List<Action> endActions = new DialogueScreener(dialogueData).screenForEndActions();
        AssertUtil.containsExact("C", endActions, Action.ACTION_BY_TEXT_EQUALS_CHECKER);
    }

    /**
     * A[B, C, D], B[E], C[E], D[F], conditions for B will not be fulfilled
     */
    @Test
    public void testScreenForEndActions_multipleWaysThrough() {
        NpcAttributes attributes = new NpcAttributes();
        attributes.addAttribute("reputation", 50);

        Action actionA = generateTestAction("A");
        Action actionB = generateTestAction("B");
        Action actionC = generateTestAction("C");
        Action actionD = generateTestAction("D");
        Action actionE = generateTestAction("E");
        Action actionF = generateTestAction("F");
        actionA.addTargetAction(actionB);
        actionA.addTargetAction(actionC);
        actionA.addTargetAction(actionD);
        actionB.addTargetAction(actionE);
        actionC.addTargetAction(actionE);
        actionD.addTargetAction(actionF);

        actionB.addActionCondition("reputation", 60);

        NpcDialogueData dialogueData = new NpcDialogueData(attributes, actionA);

        List<Action> endActions = new DialogueScreener(dialogueData).screenForEndActions();
        assertEquals(2, endActions.size());
        AssertUtil.containsExact(Arrays.asList("E", "F"), endActions, Action.ACTION_BY_TEXT_EQUALS_CHECKER);
    }
}