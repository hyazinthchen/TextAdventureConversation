package npcDialogue.controller;

import com.queomedia.commons.asserts.AssertUtil;
import npcDialogue.model.*;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class DialogueValidatorTest {
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
        List<Action> leaves = new DialogueValidator(dialogueData).findLeaves();

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
        List<Action> leaves = new DialogueValidator(dialogueData).findLeaves();

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

        AssertUtil.isEmpty(new DialogueValidator(dialogueData).findEndActions());
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

        List<Action> endActions = new DialogueValidator(dialogueData).findEndActions();
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

        List<Action> endActions = new DialogueValidator(dialogueData).findEndActions();
        assertEquals(2, endActions.size());
        AssertUtil.containsExact(Arrays.asList("E", "F"), endActions, Action.ACTION_BY_TEXT_EQUALS_CHECKER);
    }

    /**
     * A[B, C]
     */
    @Test
    public void testScreenForPaths_TwoEnds_TwoPaths() {
        NpcAttributes attributes = new NpcAttributes();

        Action actionA = generateTestAction("A");
        Action actionB = generateTestAction("B");
        Action actionC = generateTestAction("C");
        actionA.addTargetAction(actionB);
        actionA.addTargetAction(actionC);

        NpcDialogueData dialogueData = new NpcDialogueData(attributes, actionA);

        List<Path> paths = new DialogueValidator(dialogueData).findAllPathsToAllEndActionsFrom();
        assertEquals(2, paths.size());
        AssertUtil.containsExact(Arrays.asList("A", "B"), paths.get(0).getWayPoints(), Action.ACTION_BY_TEXT_EQUALS_CHECKER);
        AssertUtil.containsExact(Arrays.asList("A", "C"), paths.get(1).getWayPoints(), Action.ACTION_BY_TEXT_EQUALS_CHECKER);
    }

    /**
     * A[B, C], B[D], C[D]
     */
    @Test
    public void testScreenForPaths_OneEnd_TwoPaths() {
        NpcAttributes attributes = new NpcAttributes();

        Action actionA = generateTestAction("A");
        Action actionB = generateTestAction("B");
        Action actionC = generateTestAction("C");
        Action actionD = generateTestAction("D");
        actionA.addTargetAction(actionB);
        actionA.addTargetAction(actionC);
        actionB.addTargetAction(actionD);
        actionC.addTargetAction(actionD);

        NpcDialogueData dialogueData = new NpcDialogueData(attributes, actionA);

        List<Path> paths = new DialogueValidator(dialogueData).findAllPathsToAllEndActionsFrom();
        assertEquals(2, paths.size());
        AssertUtil.containsExact(Arrays.asList("A", "B", "D"), paths.get(0).getWayPoints(), Action.ACTION_BY_TEXT_EQUALS_CHECKER);
        AssertUtil.containsExact(Arrays.asList("A", "C", "D"), paths.get(1).getWayPoints(), Action.ACTION_BY_TEXT_EQUALS_CHECKER);
    }

    /**
     * A[B, C], B[D], D[A]
     */
    @Test
    public void testScreenForPaths_OneEnd_TwoPaths_WithCircle() {
        NpcAttributes attributes = new NpcAttributes();

        Action actionA = generateTestAction("A");
        Action actionB = generateTestAction("B");
        Action actionC = generateTestAction("C");
        Action actionD = generateTestAction("D");
        actionA.addTargetAction(actionB);
        actionA.addTargetAction(actionC);
        actionB.addTargetAction(actionD);
        actionD.addTargetAction(actionA);

        NpcDialogueData dialogueData = new NpcDialogueData(attributes, actionA);

        List<Path> paths = new DialogueValidator(dialogueData).findAllPathsToAllEndActionsFrom();
        AssertUtil.hasSize(2, paths);
        AssertUtil.containsExact(Arrays.asList("A", "B", "D", "A", "C"), paths.get(0).getWayPoints(), Action.ACTION_BY_TEXT_EQUALS_CHECKER);
        AssertUtil.containsExact(Arrays.asList("A", "C"), paths.get(1).getWayPoints(), Action.ACTION_BY_TEXT_EQUALS_CHECKER);


        //AssertUtil.containsExact(Arrays.asList(new String[]{"A", "C"}, new String[]{"A", "B", "D", "A", "C"} ), paths, Path.PATH_WAYPOINT_NAME_EQUALS_CHECKER);
    }

    /**
     * A[B, C, D], B[A]
     */
    @Test
    public void testScreenForPaths_TwoEnds_FourPaths_WithCircle() {
        NpcAttributes attributes = new NpcAttributes();

        Action actionA = generateTestAction("A");
        Action actionB = generateTestAction("B");
        Action actionC = generateTestAction("C");
        Action actionD = generateTestAction("D");
        actionA.addTargetAction(actionB);
        actionA.addTargetAction(actionC);
        actionA.addTargetAction(actionD);
        actionB.addTargetAction(actionA);

        NpcDialogueData dialogueData = new NpcDialogueData(attributes, actionA);

        List<Path> paths = new DialogueValidator(dialogueData).findAllPathsToAllEndActionsFrom();
        AssertUtil.hasSize(4, paths);
        AssertUtil.containsExact(Arrays.asList("A", "B", "A", "C"), paths.get(0).getWayPoints(), Action.ACTION_BY_TEXT_EQUALS_CHECKER);
        AssertUtil.containsExact(Arrays.asList("A", "B", "A", "D"), paths.get(1).getWayPoints(), Action.ACTION_BY_TEXT_EQUALS_CHECKER);
        AssertUtil.containsExact(Arrays.asList("A", "C"), paths.get(2).getWayPoints(), Action.ACTION_BY_TEXT_EQUALS_CHECKER);
        AssertUtil.containsExact(Arrays.asList("A", "D"), paths.get(3).getWayPoints(), Action.ACTION_BY_TEXT_EQUALS_CHECKER);
    }

    /**
     * A[B, C, D], B[A], C[A]
     */
    @Test
    public void testScreenForPaths_OneEnd_FivePaths_WithTwoCircles() {
        NpcAttributes attributes = new NpcAttributes();

        Action actionA = generateTestAction("A");
        Action actionB = generateTestAction("B");
        Action actionC = generateTestAction("C");
        Action actionD = generateTestAction("D");
        actionA.addTargetAction(actionB);
        actionA.addTargetAction(actionC);
        actionA.addTargetAction(actionD);
        actionB.addTargetAction(actionA);
        actionC.addTargetAction(actionA);

        NpcDialogueData dialogueData = new NpcDialogueData(attributes, actionA);

        List<Path> paths = new DialogueValidator(dialogueData).findAllPathsToAllEndActionsFrom();
        AssertUtil.hasSize(5, paths);
        AssertUtil.containsExact(Arrays.asList("A", "B", "A", "D"), paths.get(0).getWayPoints(), Action.ACTION_BY_TEXT_EQUALS_CHECKER);
        AssertUtil.containsExact(Arrays.asList("A", "C", "A", "D"), paths.get(1).getWayPoints(), Action.ACTION_BY_TEXT_EQUALS_CHECKER);
        AssertUtil.containsExact(Arrays.asList("A", "B", "A", "C", "A", "D"), paths.get(2).getWayPoints(), Action.ACTION_BY_TEXT_EQUALS_CHECKER);
        AssertUtil.containsExact(Arrays.asList("A", "D"), paths.get(3).getWayPoints(), Action.ACTION_BY_TEXT_EQUALS_CHECKER);
        AssertUtil.containsExact(Arrays.asList("A", "C", "A", "B", "A", "D"), paths.get(4).getWayPoints(), Action.ACTION_BY_TEXT_EQUALS_CHECKER);
    }

    /**
     * A[B, C], B[D, A], D[B]
     */
    @Test
    public void testScreenForPaths_OneEnd_tHREEPaths_WithTwoCircles() {
        NpcAttributes attributes = new NpcAttributes();

        Action actionA = generateTestAction("A");
        Action actionB = generateTestAction("B");
        Action actionC = generateTestAction("C");
        Action actionD = generateTestAction("D");
        actionA.addTargetAction(actionB);
        actionA.addTargetAction(actionC);
        actionB.addTargetAction(actionD);
        actionB.addTargetAction(actionA);
        actionD.addTargetAction(actionB);

        NpcDialogueData dialogueData = new NpcDialogueData(attributes, actionA);

        List<Path> paths = new DialogueValidator(dialogueData).findAllPathsToAllEndActionsFrom();
        AssertUtil.hasSize(3, paths);
        AssertUtil.containsExact(Arrays.asList("A", "B", "D", "B", "A", "C"), paths.get(0).getWayPoints(), Action.ACTION_BY_TEXT_EQUALS_CHECKER);
        AssertUtil.containsExact(Arrays.asList("A", "C"), paths.get(1).getWayPoints(), Action.ACTION_BY_TEXT_EQUALS_CHECKER);
        AssertUtil.containsExact(Arrays.asList("A", "B", "A", "C"), paths.get(2).getWayPoints(), Action.ACTION_BY_TEXT_EQUALS_CHECKER);
    }
}