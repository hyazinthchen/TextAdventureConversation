package npcDialogue.controller;

import npcDialogue.model.*;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

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
        List<Action> expectedLeaves = asList(actionB, actionC);

        Assert.assertEquals(2, leaves.size());
        Assert.assertTrue(leaves.containsAll(expectedLeaves));

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
        Assert.assertTrue(new DialogueValidator(dialogueData).findEndActionsFrom(dialogueData.getStartAction()).isEmpty());
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
        Assert.assertEquals(1, endActions.size());
        Assert.assertTrue(endActions.contains(actionC));
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
        List<Action> expectedEndActions = asList(actionE, actionF);

        Assert.assertEquals(2, endActions.size());
        Assert.assertTrue(endActions.containsAll(expectedEndActions));
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

        List<Action> expectedWayPointsToB = asList(actionA, actionB);
        List<Action> expectedWayPointsToC = asList(actionA, actionC);

        Assert.assertEquals(1, pathsToB.size());
        Assert.assertEquals(1, pathsToC.size());

        Assert.assertEquals(2, pathsToB.get(0).getWayPoints().size());
        Assert.assertTrue(pathsToB.get(0).getWayPoints().containsAll(expectedWayPointsToB));

        Assert.assertEquals(2, pathsToC.get(0).getWayPoints().size());
        Assert.assertTrue(pathsToC.get(0).getWayPoints().containsAll(expectedWayPointsToC));
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

        List<Action> expectedWayPointsToDPath1 = asList(actionA, actionB, actionD);
        List<Action> expectedWayPointsToDPath2 = asList(actionA, actionC, actionD);

        Assert.assertEquals(2, pathsToD.size());

        Assert.assertEquals(3, pathsToD.get(0).getWayPoints().size());
        Assert.assertTrue(pathsToD.get(0).getWayPoints().containsAll(expectedWayPointsToDPath1));

        Assert.assertEquals(3, pathsToD.get(1).getWayPoints().size());
        Assert.assertTrue(pathsToD.get(1).getWayPoints().containsAll(expectedWayPointsToDPath2));
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

        List<Action> expectedWayPointsToCPath1 = asList(actionA, actionB, actionD, actionA, actionC);
        List<Action> expectedWayPointsToCPath2 = asList(actionA, actionC);

        Assert.assertEquals(2, pathsToC.size());

        Assert.assertEquals(5, pathsToC.get(0).getWayPoints().size());
        Assert.assertTrue(pathsToC.get(0).getWayPoints().containsAll(expectedWayPointsToCPath1));

        Assert.assertEquals(2, pathsToC.get(1).getWayPoints().size());
        Assert.assertTrue(pathsToC.get(1).getWayPoints().containsAll(expectedWayPointsToCPath2));
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

        List<Action> expectedWayPointsToCPath1 = asList(actionA, actionB, actionA, actionC);
        List<Action> expectedWayPointsToCPath2 = asList(actionA, actionC);

        List<Action> expectedWayPointsToDPath1 = asList(actionA, actionB, actionA, actionD);
        List<Action> expectedWayPointsToDPath2 = asList(actionA, actionD);

        Assert.assertEquals(2, pathsToC.size());
        Assert.assertEquals(2, pathsToD.size());

        Assert.assertEquals(4, pathsToC.get(0).getWayPoints().size());
        Assert.assertTrue(pathsToC.get(0).getWayPoints().containsAll(expectedWayPointsToCPath1));

        Assert.assertEquals(2, pathsToC.get(1).getWayPoints().size());
        Assert.assertTrue(pathsToC.get(1).getWayPoints().containsAll(expectedWayPointsToCPath2));

        Assert.assertEquals(4, pathsToD.get(0).getWayPoints().size());
        Assert.assertTrue(pathsToD.get(0).getWayPoints().containsAll(expectedWayPointsToDPath1));

        Assert.assertEquals(2, pathsToD.get(1).getWayPoints().size());
        Assert.assertTrue(pathsToD.get(1).getWayPoints().containsAll(expectedWayPointsToDPath2));

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

        List<Action> expectedWayPointsToDPath1 = asList(actionA, actionB, actionA, actionC, actionA, actionD);
        List<Action> expectedWayPointsToDPath2 = asList(actionA, actionB, actionA, actionD);
        List<Action> expectedWayPointsToDPath3 = asList(actionA, actionC, actionA, actionB, actionA, actionD);
        List<Action> expectedWayPointsToDPath4 = asList(actionA, actionC, actionA, actionD);
        List<Action> expectedWayPointsToDPath5 = asList(actionA, actionD);

        Assert.assertEquals(5, pathsToD.size());

        Assert.assertEquals(6, pathsToD.get(0).getWayPoints().size());
        Assert.assertTrue(pathsToD.get(0).getWayPoints().containsAll(expectedWayPointsToDPath1));

        Assert.assertEquals(4, pathsToD.get(1).getWayPoints().size());
        Assert.assertTrue(pathsToD.get(1).getWayPoints().containsAll(expectedWayPointsToDPath2));

        Assert.assertEquals(6, pathsToD.get(2).getWayPoints().size());
        Assert.assertTrue(pathsToD.get(2).getWayPoints().containsAll(expectedWayPointsToDPath3));

        Assert.assertEquals(4, pathsToD.get(3).getWayPoints().size());
        Assert.assertTrue(pathsToD.get(3).getWayPoints().containsAll(expectedWayPointsToDPath4));

        Assert.assertEquals(2, pathsToD.get(4).getWayPoints().size());
        Assert.assertTrue(pathsToD.get(4).getWayPoints().containsAll(expectedWayPointsToDPath5));
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

        List<Action> expectedWayPointsToCPath1 = asList(actionA, actionB, actionD, actionB, actionA, actionC);
        List<Action> expectedWayPointsToCPath2 = asList(actionA, actionB, actionA, actionC);
        List<Action> expectedWayPointsToCPath3 = asList(actionA, actionC);

        Assert.assertEquals(3, pathsToC.size());

        Assert.assertEquals(6, pathsToC.get(0).getWayPoints().size());
        Assert.assertTrue(pathsToC.get(0).getWayPoints().containsAll(expectedWayPointsToCPath1));

        Assert.assertEquals(4, pathsToC.get(1).getWayPoints().size());
        Assert.assertTrue(pathsToC.get(1).getWayPoints().containsAll(expectedWayPointsToCPath2));

        Assert.assertEquals(2, pathsToC.get(2).getWayPoints().size());
        Assert.assertTrue(pathsToC.get(2).getWayPoints().containsAll(expectedWayPointsToCPath3));
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

        List<Action> expectedWayPointsToDPath1 = asList(actionA, actionB, actionC, actionA, actionD);
        List<Action> expectedWayPointsToDPath2 = asList(actionA, actionC, actionA, actionD);
        List<Action> expectedWayPointsToDPath3 = asList(actionA, actionD);

        Assert.assertEquals(3, pathsToD.size());

        Assert.assertEquals(5, pathsToD.get(0).getWayPoints().size());
        Assert.assertTrue(pathsToD.get(0).getWayPoints().containsAll(expectedWayPointsToDPath1));

        Assert.assertEquals(4, pathsToD.get(1).getWayPoints().size());
        Assert.assertTrue(pathsToD.get(1).getWayPoints().containsAll(expectedWayPointsToDPath2));

        Assert.assertEquals(2, pathsToD.get(2).getWayPoints().size());
        Assert.assertTrue(pathsToD.get(2).getWayPoints().containsAll(expectedWayPointsToDPath3));
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

        List<Action> expectedWayPointsToD = asList(actionA, actionC, actionD);

        Assert.assertEquals(1, pathsToD.size());

        Assert.assertEquals(3, pathsToD.get(0).getWayPoints().size());
        Assert.assertTrue(pathsToD.get(0).getWayPoints().containsAll(expectedWayPointsToD));
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

        List<Action> expectedWayPointsToDPath1 = asList(actionA, actionC, actionA, actionD);
        List<Action> expectedWayPointsToDPath2 = asList(actionA, actionD);

        Assert.assertEquals(2, pathsToD.size());

        Assert.assertEquals(4, pathsToD.get(0).getWayPoints().size());
        Assert.assertTrue(pathsToD.get(0).getWayPoints().containsAll(expectedWayPointsToDPath1));

        Assert.assertEquals(2, pathsToD.get(1).getWayPoints().size());
        Assert.assertTrue(pathsToD.get(1).getWayPoints().containsAll(expectedWayPointsToDPath2));
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

        List<Action> expectedWayPointsToC = asList(actionA, actionC);

        Assert.assertEquals(1, pathsToC.size());

        Assert.assertEquals(2, pathsToC.get(0).getWayPoints().size());
        Assert.assertTrue(pathsToC.get(0).getWayPoints().containsAll(expectedWayPointsToC));

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

        List<Action> expectedWayPointsToC = asList(actionA, actionC);

        Assert.assertEquals(1, pathsToC.size());

        Assert.assertEquals(2, pathsToC.get(0).getWayPoints().size());
        Assert.assertTrue(pathsToC.get(0).getWayPoints().containsAll(expectedWayPointsToC));

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
        List<Action> expectedWayPointsToC = asList(actionA, actionC);

        Assert.assertEquals(1, pathsToC.size());

        Assert.assertEquals(2, pathsToC.get(0).getWayPoints().size());
        Assert.assertTrue(pathsToC.get(0).getWayPoints().containsAll(expectedWayPointsToC));

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
        Assert.assertEquals(6, pathsToC.size());
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

        List<Action> expectedWayPointsToC = asList(actionA, actionB, actionC);

        Assert.assertEquals(1, pathsToC.size());

        Assert.assertEquals(3, pathsToC.get(0).getWayPoints().size());
        Assert.assertTrue(pathsToC.get(0).getWayPoints().containsAll(expectedWayPointsToC));

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
        Assert.assertTrue(pathsToC.isEmpty());
    }

    /**
     * A[B, D], B[A, C], D[E], entering B blocks the way to C but unlocks the way to E
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

        List<Action> expectedWayPointsToC = asList(actionA, actionB, actionC);
        List<Action> expectedWayPointsToEPath1 = asList(actionA, actionD, actionC);
        List<Action> expectedWayPointsToEPath2 = asList(actionA, actionB, actionA, actionD, actionE);

        Assert.assertEquals(1, pathsToC.size());
        Assert.assertEquals(2, pathsToE.size());

        Assert.assertEquals(3, pathsToC.get(0).getWayPoints().size());
        Assert.assertTrue(pathsToC.get(0).getWayPoints().containsAll(expectedWayPointsToC));

        Assert.assertEquals(3, pathsToE.get(0).getWayPoints().size());
        Assert.assertTrue(pathsToE.get(0).getWayPoints().containsAll(expectedWayPointsToEPath1));

        Assert.assertEquals(5, pathsToE.get(1).getWayPoints().size());
        Assert.assertTrue(pathsToE.get(1).getWayPoints().containsAll(expectedWayPointsToEPath2));
    }
}