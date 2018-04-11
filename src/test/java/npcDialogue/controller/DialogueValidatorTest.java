package npcDialogue.controller;

import com.queomedia.commons.asserts.AssertUtil;
import npcDialogue.model.*;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
     * A[B, C], B[D], conditions for D will not be fulfilled
     */
    @Test
    public void testFindLeavesFrom() {
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
        List<Action> leaves = new DialogueValidator(dialogueData).findLeavesFrom(dialogueData.getStartAction());

        AssertUtil.hasSize(2, leaves);
        AssertUtil.containsExact(Arrays.asList("B", "C"), leaves, Action.ACTION_BY_TEXT_EQUALS_CHECKER);
    }

    /**
     * A[B, C], B[D], C[D], conditions for B and C will not be fulfilled
     */
    @Test
    public void testFindEndActionsFrom_noWayThrough() {
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

        AssertUtil.isEmpty(new DialogueValidator(dialogueData).findEndActionsFrom(dialogueData.getStartAction()));
    }

    /**
     * A[B, C], conditions for B will not be fulfilled
     */
    @Test
    public void testFindEndActionsFrom_oneWayThrough() {
        NpcAttributes attributes = new NpcAttributes();
        attributes.addAttribute("reputation", 50);

        Action actionA = generateTestAction("A");
        Action actionB = generateTestAction("B");
        Action actionC = generateTestAction("C");
        actionA.addTargetAction(actionB);
        actionA.addTargetAction(actionC);

        actionB.addActionCondition("reputation", 60);

        NpcDialogueData dialogueData = new NpcDialogueData(attributes, actionA);

        List<Action> endActions = new DialogueValidator(dialogueData).findEndActionsFrom(dialogueData.getStartAction());
        AssertUtil.hasSize(1, endActions);
        AssertUtil.containsExact("C", endActions, Action.ACTION_BY_TEXT_EQUALS_CHECKER);
    }

    /**
     * A[B, C, D], B[E], C[E], D[F], conditions for B will not be fulfilled
     */
    @Test
    public void testFindEndActionsFrom_multipleWaysThrough() {
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

        List<Action> endActions = new DialogueValidator(dialogueData).findEndActionsFrom(dialogueData.getStartAction());
        AssertUtil.hasSize(2, endActions);
        AssertUtil.containsExact(Arrays.asList("E", "F"), endActions, Action.ACTION_BY_TEXT_EQUALS_CHECKER);
    }

    /**
     * A[B, C]
     */
    @Test
    public void testFindAllPathsTo_TwoEnds_TwoPaths() {
        Action actionA = generateTestAction("A");
        Action actionB = generateTestAction("B");
        Action actionC = generateTestAction("C");
        actionA.addTargetAction(actionB);
        actionA.addTargetAction(actionC);

        NpcDialogueData dialogueData = new NpcDialogueData(new NpcAttributes(), actionA);

        List<Path> pathsToB = new DialogueValidator(dialogueData).findAllPathsToSpecificAction(dialogueData.getStartAction(), actionB);
        List<Path> pathsToC = new DialogueValidator(dialogueData).findAllPathsToSpecificAction(dialogueData.getStartAction(), actionC);

        AssertUtil.hasSize(1, pathsToB);
        AssertUtil.hasSize(1, pathsToC);
        AssertUtil.containsExact(Arrays.asList("A", "B"), pathsToB.get(0).getWayPoints(), Action.ACTION_BY_TEXT_EQUALS_CHECKER);
        AssertUtil.containsExact(Arrays.asList("A", "C"), pathsToC.get(0).getWayPoints(), Action.ACTION_BY_TEXT_EQUALS_CHECKER);
    }

    /**
     * A[B, C], B[D], C[D]
     */
    @Test
    public void testFindAllPathsTo_OneEnd_TwoPaths() {
        Action actionA = generateTestAction("A");
        Action actionB = generateTestAction("B");
        Action actionC = generateTestAction("C");
        Action actionD = generateTestAction("D");
        actionA.addTargetAction(actionB);
        actionA.addTargetAction(actionC);
        actionB.addTargetAction(actionD);
        actionC.addTargetAction(actionD);

        NpcDialogueData dialogueData = new NpcDialogueData(new NpcAttributes(), actionA);

        List<Path> pathsToD = new DialogueValidator(dialogueData).findAllPathsToSpecificAction(dialogueData.getStartAction(), actionD);

        AssertUtil.hasSize(2, pathsToD);
        AssertUtil.containsExact(Arrays.asList("A", "B", "D"), pathsToD.get(0).getWayPoints(), Action.ACTION_BY_TEXT_EQUALS_CHECKER);
        AssertUtil.containsExact(Arrays.asList("A", "C", "D"), pathsToD.get(1).getWayPoints(), Action.ACTION_BY_TEXT_EQUALS_CHECKER);
    }

    /**
     * A[B, C], B[D], D[A]
     */
    @Test
    public void testFindAllPathsTo_OneEnd_TwoPaths_WithCircle() {
        Action actionA = generateTestAction("A");
        Action actionB = generateTestAction("B");
        Action actionC = generateTestAction("C");
        Action actionD = generateTestAction("D");
        actionA.addTargetAction(actionB);
        actionA.addTargetAction(actionC);
        actionB.addTargetAction(actionD);
        actionD.addTargetAction(actionA);

        NpcDialogueData dialogueData = new NpcDialogueData(new NpcAttributes(), actionA);

        List<Path> pathsToC = new DialogueValidator(dialogueData).findAllPathsToSpecificAction(dialogueData.getStartAction(), actionC);

        AssertUtil.hasSize(2, pathsToC);
        AssertUtil.containsExact(Arrays.asList("A", "B", "D", "A", "C"), pathsToC.get(0).getWayPoints(), Action.ACTION_BY_TEXT_EQUALS_CHECKER);
        AssertUtil.containsExact(Arrays.asList("A", "C"), pathsToC.get(1).getWayPoints(), Action.ACTION_BY_TEXT_EQUALS_CHECKER);
    }

    /**
     * A[B, C, D], B[A]
     */
    @Test
    public void testFindAllPathsTo_TwoEnds_FourPaths_WithCircle() {
        Action actionA = generateTestAction("A");
        Action actionB = generateTestAction("B");
        Action actionC = generateTestAction("C");
        Action actionD = generateTestAction("D");
        actionA.addTargetAction(actionB);
        actionA.addTargetAction(actionC);
        actionA.addTargetAction(actionD);
        actionB.addTargetAction(actionA);

        NpcDialogueData dialogueData = new NpcDialogueData(new NpcAttributes(), actionA);

        List<Path> pathsToC = new DialogueValidator(dialogueData).findAllPathsToSpecificAction(dialogueData.getStartAction(), actionC);
        List<Path> pathsToD = new DialogueValidator(dialogueData).findAllPathsToSpecificAction(dialogueData.getStartAction(), actionD);

        AssertUtil.hasSize(2, pathsToC);
        AssertUtil.hasSize(2, pathsToD);

        AssertUtil.containsExact(Arrays.asList("A", "B", "A", "C"), pathsToC.get(0).getWayPoints(), Action.ACTION_BY_TEXT_EQUALS_CHECKER);
        AssertUtil.containsExact(Arrays.asList("A", "C"), pathsToC.get(1).getWayPoints(), Action.ACTION_BY_TEXT_EQUALS_CHECKER);

        AssertUtil.containsExact(Arrays.asList("A", "B", "A", "D"), pathsToD.get(0).getWayPoints(), Action.ACTION_BY_TEXT_EQUALS_CHECKER);
        AssertUtil.containsExact(Arrays.asList("A", "D"), pathsToD.get(1).getWayPoints(), Action.ACTION_BY_TEXT_EQUALS_CHECKER);

        //AssertUtil.containsExact(Arrays.asList(new String[]{"A", "C"}, new String[]{"A", "B", "A", "C"}), pathsToC, Path.PATH_WAYPOINT_NAME_EQUALS_CHECKER);
    }

    /**
     * A[B, C, D], B[A], C[A]
     */
    @Test
    public void testFindAllPathsTo_OneEnd_FivePaths_WithTwoCircles() {
        Action actionA = generateTestAction("A");
        Action actionB = generateTestAction("B");
        Action actionC = generateTestAction("C");
        Action actionD = generateTestAction("D");
        actionA.addTargetAction(actionB);
        actionA.addTargetAction(actionC);
        actionA.addTargetAction(actionD);
        actionB.addTargetAction(actionA);
        actionC.addTargetAction(actionA);

        NpcDialogueData dialogueData = new NpcDialogueData(new NpcAttributes(), actionA);

        List<Path> pathsToD = new DialogueValidator(dialogueData).findAllPathsToSpecificAction(dialogueData.getStartAction(), actionD);

        AssertUtil.hasSize(5, pathsToD);
        AssertUtil.containsExact(Arrays.asList("A", "B", "A", "C", "A", "D"), pathsToD.get(0).getWayPoints(), Action.ACTION_BY_TEXT_EQUALS_CHECKER);
        AssertUtil.containsExact(Arrays.asList("A", "B", "A", "D"), pathsToD.get(1).getWayPoints(), Action.ACTION_BY_TEXT_EQUALS_CHECKER);
        AssertUtil.containsExact(Arrays.asList("A", "C", "A", "B", "A", "D"), pathsToD.get(2).getWayPoints(), Action.ACTION_BY_TEXT_EQUALS_CHECKER);
        AssertUtil.containsExact(Arrays.asList("A", "C", "A", "D"), pathsToD.get(3).getWayPoints(), Action.ACTION_BY_TEXT_EQUALS_CHECKER);
        AssertUtil.containsExact(Arrays.asList("A", "D"), pathsToD.get(4).getWayPoints(), Action.ACTION_BY_TEXT_EQUALS_CHECKER);
    }

    /**
     * A[B, C], B[D, A], D[B]
     */
    @Test
    public void testFindAllPathsTo_OneEnd_threePaths_WithTwoCircles() {
        Action actionA = generateTestAction("A");
        Action actionB = generateTestAction("B");
        Action actionC = generateTestAction("C");
        Action actionD = generateTestAction("D");
        actionA.addTargetAction(actionB);
        actionA.addTargetAction(actionC);
        actionB.addTargetAction(actionD);
        actionB.addTargetAction(actionA);
        actionD.addTargetAction(actionB);

        NpcDialogueData dialogueData = new NpcDialogueData(new NpcAttributes(), actionA);

        List<Path> pathsToC = new DialogueValidator(dialogueData).findAllPathsToSpecificAction(dialogueData.getStartAction(), actionC);

        AssertUtil.hasSize(3, pathsToC);
        AssertUtil.containsExact(Arrays.asList("A", "B", "D", "B", "A", "C"), pathsToC.get(0).getWayPoints(), Action.ACTION_BY_TEXT_EQUALS_CHECKER);
        AssertUtil.containsExact(Arrays.asList("A", "B", "A", "C"), pathsToC.get(1).getWayPoints(), Action.ACTION_BY_TEXT_EQUALS_CHECKER);
        AssertUtil.containsExact(Arrays.asList("A", "C"), pathsToC.get(2).getWayPoints(), Action.ACTION_BY_TEXT_EQUALS_CHECKER);
    }

    /**
     * A[B, C, D], B[C], C[A]
     */
    @Test
    public void testFindAllPathsTo_OneEnd_ThreePaths_OneCircle() {
        Action actionA = generateTestAction("A");
        Action actionB = generateTestAction("B");
        Action actionC = generateTestAction("C");
        Action actionD = generateTestAction("D");
        actionA.addTargetAction(actionB);
        actionA.addTargetAction(actionC);
        actionA.addTargetAction(actionD);
        actionB.addTargetAction(actionC);
        actionC.addTargetAction(actionA);

        NpcDialogueData dialogueData = new NpcDialogueData(new NpcAttributes(), actionA);

        List<Path> pathsToD = new DialogueValidator(dialogueData).findAllPathsToSpecificAction(dialogueData.getStartAction(), actionD);

        AssertUtil.hasSize(3, pathsToD);
        AssertUtil.containsExact(Arrays.asList("A", "B", "C", "A", "D"), pathsToD.get(0).getWayPoints(), Action.ACTION_BY_TEXT_EQUALS_CHECKER);
        AssertUtil.containsExact(Arrays.asList("A", "C", "A", "D"), pathsToD.get(1).getWayPoints(), Action.ACTION_BY_TEXT_EQUALS_CHECKER);
        AssertUtil.containsExact(Arrays.asList("A", "D"), pathsToD.get(2).getWayPoints(), Action.ACTION_BY_TEXT_EQUALS_CHECKER);
    }

    /**
     * A[B, C], B[D], C[D], conditions for B  will not be fulfilled
     */
    @Test
    public void testFindAllPathsTo_oneWayThrough() {
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

        NpcDialogueData dialogueData = new NpcDialogueData(attributes, actionA);

        List<Path> pathsToD = new DialogueValidator(dialogueData).findAllPathsToSpecificAction(dialogueData.getStartAction(), actionD);

        AssertUtil.hasSize(1, pathsToD);
        AssertUtil.containsExact(Arrays.asList("A", "C", "D"), pathsToD.get(0).getWayPoints(), Action.ACTION_BY_TEXT_EQUALS_CHECKER);
    }

    /**
     * A[B, C, D], C[A], conditions for B will not be fulfilled
     */
    @Test
    public void testFindAllPathsTo_twoWaysThrough() {
        NpcAttributes attributes = new NpcAttributes();
        attributes.addAttribute("reputation", 50);

        Action actionA = generateTestAction("A");
        Action actionB = generateTestAction("B");
        Action actionC = generateTestAction("C");
        Action actionD = generateTestAction("D");
        actionA.addTargetAction(actionB);
        actionA.addTargetAction(actionC);
        actionA.addTargetAction(actionD);
        actionC.addTargetAction(actionA);

        actionB.addActionCondition("reputation", 60);

        NpcDialogueData dialogueData = new NpcDialogueData(attributes, actionA);

        List<Path> pathsToD = new DialogueValidator(dialogueData).findAllPathsToSpecificAction(dialogueData.getStartAction(), actionD);

        AssertUtil.hasSize(2, pathsToD);
        AssertUtil.containsExact(Arrays.asList("A", "C", "A", "D"), pathsToD.get(0).getWayPoints(), Action.ACTION_BY_TEXT_EQUALS_CHECKER);
        AssertUtil.containsExact(Arrays.asList("A", "D"), pathsToD.get(1).getWayPoints(), Action.ACTION_BY_TEXT_EQUALS_CHECKER);
    }

    /**
     * A[B, C], B[D], D[B]
     */
    @Test
    public void testFindAllPathsTo_twoActionCycle() {
        Action actionA = generateTestAction("A");
        Action actionB = generateTestAction("B");
        Action actionC = generateTestAction("C");
        Action actionD = generateTestAction("D");
        actionA.addTargetAction(actionB);
        actionA.addTargetAction(actionC);
        actionB.addTargetAction(actionD);
        actionD.addTargetAction(actionB);

        NpcDialogueData dialogueData = new NpcDialogueData(new NpcAttributes(), actionA);

        List<Path> pathsToC = new DialogueValidator(dialogueData).findAllPathsToSpecificAction(dialogueData.getStartAction(), actionC);

        AssertUtil.hasSize(1, pathsToC);
        AssertUtil.containsExact(Arrays.asList("A", "C"), pathsToC.get(0).getWayPoints(), Action.ACTION_BY_TEXT_EQUALS_CHECKER);

        Assert.assertTrue(new DialogueValidator(dialogueData).findCyclesWithoutExit(dialogueData.getStartAction()));
    }

    /**
     * A[B, C], B[D], D[E], E[B]
     */
    @Test
    public void testFindAllPathsTo_threeActionCycle() {
        Action actionA = generateTestAction("A");
        Action actionB = generateTestAction("B");
        Action actionC = generateTestAction("C");
        Action actionD = generateTestAction("D");
        Action actionE = generateTestAction("E");
        actionA.addTargetAction(actionB);
        actionA.addTargetAction(actionC);
        actionB.addTargetAction(actionD);
        actionD.addTargetAction(actionE);
        actionE.addTargetAction(actionB);

        NpcDialogueData dialogueData = new NpcDialogueData(new NpcAttributes(), actionA);

        List<Path> pathsToC = new DialogueValidator(dialogueData).findAllPathsToSpecificAction(dialogueData.getStartAction(), actionC);

        AssertUtil.hasSize(1, pathsToC);
        AssertUtil.containsExact(Arrays.asList("A", "C"), pathsToC.get(0).getWayPoints(), Action.ACTION_BY_TEXT_EQUALS_CHECKER);

        Assert.assertTrue(new DialogueValidator(dialogueData).findCyclesWithoutExit(dialogueData.getStartAction()));
    }

    /**
     * A[B, C], B[D, E], D[B], conditions for E will not be fulfilled
     */
    @Test
    public void testFindAllPathsTo_twoActionCycle_withCondition() {
        NpcAttributes attributes = new NpcAttributes();
        attributes.addAttribute("reputation", 50);

        Action actionA = generateTestAction("A");
        Action actionB = generateTestAction("B");
        Action actionC = generateTestAction("C");
        Action actionD = generateTestAction("D");
        Action actionE = generateTestAction("E");
        actionA.addTargetAction(actionB);
        actionA.addTargetAction(actionC);
        actionB.addTargetAction(actionD);
        actionB.addTargetAction(actionE);
        actionD.addTargetAction(actionB);

        actionE.addActionCondition("reputation", 60);

        NpcDialogueData dialogueData = new NpcDialogueData(attributes, actionA);

        List<Path> pathsToC = new DialogueValidator(dialogueData).findAllPathsToSpecificAction(dialogueData.getStartAction(), actionC);

        AssertUtil.hasSize(1, pathsToC);
        AssertUtil.containsExact(Arrays.asList("A", "C"), pathsToC.get(0).getWayPoints(), Action.ACTION_BY_TEXT_EQUALS_CHECKER);

        Assert.assertTrue(new DialogueValidator(dialogueData).findCyclesWithoutExit(dialogueData.getStartAction()));
    }

    /**
     * A[B, C], B[D, A], D[E, A]
     */
    @Test
    public void testFindAllPathsTo_twoNestedCycles() {

        Action actionA = generateTestAction("A");
        Action actionB = generateTestAction("B");
        Action actionC = generateTestAction("C");
        Action actionD = generateTestAction("D");
        Action actionE = generateTestAction("E");
        actionA.addTargetAction(actionB);
        actionA.addTargetAction(actionC);
        actionB.addTargetAction(actionD);
        actionB.addTargetAction(actionA);
        actionD.addTargetAction(actionA);
        actionD.addTargetAction(actionE);

        NpcDialogueData dialogueData = new NpcDialogueData(new NpcAttributes(), actionA);

        List<Path> pathsToC = new DialogueValidator(dialogueData).findAllPathsToSpecificAction(dialogueData.getStartAction(), actionC);

        AssertUtil.hasSize(6, pathsToC);
    }

    /**
     * A[B], B[C], condition of C is only fulfilled when B is reached
     */
    @Test
    public void testFindAllPathsTo_withModificationAndCondition_onePath() {
        NpcAttributes attributes = new NpcAttributes();
        attributes.addAttribute("reputation", 50);

        Action actionA = generateTestAction("A");
        Action actionB = generateTestAction("B");
        Action actionC = generateTestAction("C");
        actionA.addTargetAction(actionB);
        actionB.addTargetAction(actionC);

        actionB.addNpcAttributeModification("reputation", 60);
        actionC.addActionCondition("reputation", 60);

        NpcDialogueData dialogueData = new NpcDialogueData(attributes, actionA);

        List<Path> pathsToC = new DialogueValidator(dialogueData).findAllPathsToSpecificAction(dialogueData.getStartAction(), actionC);

        AssertUtil.hasSize(1, pathsToC);
        AssertUtil.containsExact(Arrays.asList("A", "B", "C"), pathsToC.get(0).getWayPoints(), Action.ACTION_BY_TEXT_EQUALS_CHECKER);


        Path p1 = new Path(actionA, actionB, actionC); //TODO: better than with equalsChecker because its independent from cardinality

        List<Path> expectedPathList = new ArrayList<>();
        expectedPathList.add(p1);
        CollectionUtils.isEqualCollection(expectedPathList, pathsToC);
    }

    /**
     * A[B], B[C], condition of C is not fulfilled because B changes the attributes beforehand
     */
    @Test
    public void testFindAllPathsTo_withModificationAndCondition_noPath() {
        NpcAttributes attributes = new NpcAttributes();
        attributes.addAttribute("reputation", 50);

        Action actionA = generateTestAction("A");
        Action actionB = generateTestAction("B");
        Action actionC = generateTestAction("C");
        actionA.addTargetAction(actionB);
        actionB.addTargetAction(actionC);

        actionB.addNpcAttributeModification("reputation", 60);
        actionC.addActionCondition("reputation", 50);

        NpcDialogueData dialogueData = new NpcDialogueData(attributes, actionA);

        List<Path> pathsToC = new DialogueValidator(dialogueData).findAllPathsToSpecificAction(dialogueData.getStartAction(), actionC);

        AssertUtil.isEmpty(pathsToC);
    }

    /**
     * A[B, D], B[C, A], D[E]
     */
    @Test
    public void testFindAllPathsTo_withModificationAndConditionAndCycle_ThreePaths() {
        NpcAttributes attributes = new NpcAttributes();
        attributes.addAttribute("reputation", 50);

        Action actionA = generateTestAction("A");
        Action actionB = generateTestAction("B");
        Action actionC = generateTestAction("C");
        Action actionD = generateTestAction("D");
        Action actionE = generateTestAction("E");
        actionA.addTargetAction(actionB);
        actionA.addTargetAction(actionD);
        actionB.addTargetAction(actionA);
        actionB.addTargetAction(actionC);
        actionD.addTargetAction(actionE);

        actionB.addNpcAttributeModification("reputation", 60);
        actionC.addActionCondition("reputation", 60);
        actionE.addActionCondition("reputation", 50);

        NpcDialogueData dialogueData = new NpcDialogueData(attributes, actionA);

        List<Path> pathsToC = new DialogueValidator(dialogueData).findAllPathsToSpecificAction(dialogueData.getStartAction(), actionC);
        List<Path> pathsToE = new DialogueValidator(dialogueData).findAllPathsToSpecificAction(dialogueData.getStartAction(), actionE);

        AssertUtil.hasSize(1, pathsToC);
        AssertUtil.hasSize(2, pathsToE);

        AssertUtil.containsExact(Arrays.asList("A", "B", "C"), pathsToC.get(0).getWayPoints(), Action.ACTION_BY_TEXT_EQUALS_CHECKER);
        AssertUtil.containsExact(Arrays.asList("A", "D", "C"), pathsToE.get(0).getWayPoints(), Action.ACTION_BY_TEXT_EQUALS_CHECKER);
        AssertUtil.containsExact(Arrays.asList("A", "B", "A", "D", "E"), pathsToE.get(1).getWayPoints(), Action.ACTION_BY_TEXT_EQUALS_CHECKER);
    }
}