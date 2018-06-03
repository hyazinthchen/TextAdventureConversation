package npcDialogue_deprecated.controller;

import com.queomedia.commons.asserts.AssertUtil;
import npcDialogue_deprecated.model.*;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertTrue;

public class DialogueValidatorOldTest {
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
     * A[B, C], B[D], C[D], conditions for B and C will not be fulfilled
     */
    @Test
    public void testFindEndActionsFrom_NoWayThrough() {
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

        actionB.addCondition("reputation","==",  60);
        actionC.addCondition("reputation","==",  60);

        NpcDialogueData dialogueData = new NpcDialogueData(attributes, actionA);

        AssertUtil.isEmpty(new DialogueValidator_old(dialogueData).findEndActionsFrom(dialogueData.getStartAction()));
    }

    /**
     * A[B, C], conditions for B will not be fulfilled
     */
    @Test
    public void testFindEndActionsFrom_OneWayThrough() {
        NpcAttributes attributes = new NpcAttributes();
        attributes.addAttribute("reputation", 50);

        Action actionA = generateTestAction("A");
        Action actionB = generateTestAction("B");
        Action actionC = generateTestAction("C");
        actionA.addTargetAction(actionB);
        actionA.addTargetAction(actionC);

        actionB.addCondition("reputation","==",  60);

        NpcDialogueData dialogueData = new NpcDialogueData(attributes, actionA);

        List<Action> endActions = new DialogueValidator_old(dialogueData).findEndActionsFrom(dialogueData.getStartAction());
        AssertUtil.hasSize(1, endActions);
        assertTrue(CollectionUtils.isEqualCollection(asList(actionC), endActions));
    }

    /**
     * A[B, C, D], B[E], C[E], D[F], conditions for B will not be fulfilled
     */
    @Test
    public void testFindEndActionsFrom_MultipleWaysThrough() {
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

        actionB.addCondition("reputation","==",  60);

        NpcDialogueData dialogueData = new NpcDialogueData(attributes, actionA);

        List<Action> endActions = new DialogueValidator_old(dialogueData).findEndActionsFrom(dialogueData.getStartAction());
        AssertUtil.hasSize(2, endActions);
        assertTrue(CollectionUtils.isEqualCollection(asList(actionE, actionF), endActions));
    }

    /**
     * A[B, D], B[C], D[E], B locks E
     */
    @Test
    public void testFindEndActionsFrom_WithModification() {
        NpcAttributes attributes = new NpcAttributes();
        attributes.addAttribute("reputation", 50);

        Action actionA = generateTestAction("A");
        Action actionB = generateTestAction("B");
        Action actionC = generateTestAction("C");
        Action actionD = generateTestAction("D");
        Action actionE = generateTestAction("E");
        actionA.addTargetAction(actionB);
        actionB.addTargetAction(actionC);
        actionA.addTargetAction(actionD);
        actionD.addTargetAction(actionE);

        actionB.addNpcAttributeModification("reputation","+", 10);
        actionE.addCondition("reputation","==",  50);

        NpcDialogueData dialogueData = new NpcDialogueData(attributes, actionA);

        List<Action> endActions = new DialogueValidator_old(dialogueData).findEndActionsFrom(dialogueData.getStartAction());
        AssertUtil.hasSize(2, endActions);
        assertTrue(CollectionUtils.isEqualCollection(asList(actionC, actionE), endActions));
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

        List<Path> pathsToB = new DialogueValidator_old(dialogueData).findAllPathsToSpecificAction(dialogueData.getStartAction(), actionB);
        List<Path> pathsToC = new DialogueValidator_old(dialogueData).findAllPathsToSpecificAction(dialogueData.getStartAction(), actionC);
        List<Path> expectedPathsToB = asList(new Path(actionA, actionB));
        List<Path> expectedPathsToC = asList(new Path(actionA, actionC));

        AssertUtil.hasSize(1, pathsToB);
        AssertUtil.hasSize(1, pathsToC);

        assertTrue(pathsToB.containsAll(expectedPathsToB));
        assertTrue(expectedPathsToB.containsAll(pathsToB));

        assertTrue(pathsToC.containsAll(expectedPathsToC));
        assertTrue(expectedPathsToC.containsAll(pathsToC));
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

        List<Path> pathsToD = new DialogueValidator_old(dialogueData).findAllPathsToSpecificAction(dialogueData.getStartAction(), actionD);
        List<Path> expectedPathsToD = asList(new Path(actionA, actionB, actionD), new Path(actionA, actionC, actionD));

        AssertUtil.hasSize(2, pathsToD);
        assertTrue(pathsToD.containsAll(expectedPathsToD));
        assertTrue(expectedPathsToD.containsAll(pathsToD));
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

        List<Path> pathsToC = new DialogueValidator_old(dialogueData).findAllPathsToSpecificAction(dialogueData.getStartAction(), actionC);
        List<Path> expectedPathsToC = asList(new Path(actionA, actionB, actionD, actionA, actionC), new Path(actionA, actionC));

        AssertUtil.hasSize(2, pathsToC);
        assertTrue(pathsToC.containsAll(expectedPathsToC));
        assertTrue(expectedPathsToC.containsAll(pathsToC));
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

        List<Path> pathsToC = new DialogueValidator_old(dialogueData).findAllPathsToSpecificAction(dialogueData.getStartAction(), actionC);
        List<Path> pathsToD = new DialogueValidator_old(dialogueData).findAllPathsToSpecificAction(dialogueData.getStartAction(), actionD);
        List<Path> expectedPathsToC = asList(new Path(actionA, actionB, actionA, actionC), new Path(actionA, actionC));
        List<Path> expectedPathsToD = asList(new Path(actionA, actionB, actionA, actionD), new Path(actionA, actionD));

        AssertUtil.hasSize(2, pathsToC);
        AssertUtil.hasSize(2, pathsToD);

        assertTrue(pathsToC.containsAll(expectedPathsToC));
        assertTrue(expectedPathsToC.containsAll(pathsToC));

        assertTrue(pathsToD.containsAll(expectedPathsToD));
        assertTrue(expectedPathsToD.containsAll(pathsToD));
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

        List<Path> pathsToD = new DialogueValidator_old(dialogueData).findAllPathsToSpecificAction(dialogueData.getStartAction(), actionD);
        List<Path> expectedPathsToD = asList(new Path(actionA, actionB, actionA, actionC, actionA, actionD), new Path(actionA, actionB, actionA, actionD), new Path(actionA, actionC, actionA, actionB, actionA, actionD), new Path(actionA, actionC, actionA, actionD), new Path(actionA, actionD));

        AssertUtil.hasSize(5, pathsToD);
        assertTrue(pathsToD.containsAll(expectedPathsToD));
        assertTrue(expectedPathsToD.containsAll(pathsToD));
    }

    /**
     * A[B, C], B[D, A], D[B]
     */
    @Test
    public void testFindAllPathsTo_OneEnd_ThreePaths_WithTwoCircles() {
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

        List<Path> pathsToC = new DialogueValidator_old(dialogueData).findAllPathsToSpecificAction(dialogueData.getStartAction(), actionC);
        List<Path> expectedPathsToC = asList(new Path(actionA, actionB, actionD, actionB, actionA, actionC), new Path(actionA, actionB, actionA, actionC), new Path(actionA, actionC));

        AssertUtil.hasSize(3, pathsToC);
        assertTrue(pathsToC.containsAll(expectedPathsToC));
        assertTrue(expectedPathsToC.containsAll(pathsToC));
    }

    /**
     * A[B, C, D], B[C], C[A]
     */
    @Test
    public void testFindAllPathsTo_OneEnd_ThreePaths_WithCircle() {
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

        List<Path> pathsToD = new DialogueValidator_old(dialogueData).findAllPathsToSpecificAction(dialogueData.getStartAction(), actionD);
        List<Path> expectedPathsToD = asList(new Path(actionA, actionB, actionC, actionA, actionD), new Path(actionA, actionC, actionA, actionD), new Path(actionA, actionD));

        AssertUtil.hasSize(3, pathsToD);
        assertTrue(pathsToD.containsAll(expectedPathsToD));
        assertTrue(expectedPathsToD.containsAll(pathsToD));
    }

    /**
     * A[B, C], B[D], C[D], conditions for B  will not be fulfilled
     */
    @Test
    public void testFindAllPathsTo_OneWayThrough() {
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

        actionB.addCondition("reputation","==",  60);

        NpcDialogueData dialogueData = new NpcDialogueData(attributes, actionA);

        List<Path> pathsToD = new DialogueValidator_old(dialogueData).findAllPathsToSpecificAction(dialogueData.getStartAction(), actionD);
        List<Path> expectedPathsToD = asList(new Path(actionA, actionC, actionD));

        AssertUtil.hasSize(1, pathsToD);
        assertTrue(pathsToD.containsAll(expectedPathsToD));
        assertTrue(expectedPathsToD.containsAll(pathsToD));
    }

    /**
     * A[B, C, D], C[A], conditions for B will not be fulfilled
     */
    @Test
    public void testFindAllPathsTo_TwoWaysThrough_WithCircle() {
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

        actionB.addCondition("reputation","==",  60);

        NpcDialogueData dialogueData = new NpcDialogueData(attributes, actionA);

        List<Path> pathsToD = new DialogueValidator_old(dialogueData).findAllPathsToSpecificAction(dialogueData.getStartAction(), actionD);
        List<Path> expectedPathsToD = asList(new Path(actionA, actionC, actionA, actionD), new Path(actionA, actionD));

        AssertUtil.hasSize(2, pathsToD);
        assertTrue(pathsToD.containsAll(expectedPathsToD));
        assertTrue(expectedPathsToD.containsAll(pathsToD));
    }

    /**
     * A[B, C], B[D], D[B]
     */
    @Test
    public void testFindAllPathsTo_OneWayThrough_WithCircle1() {
        Action actionA = generateTestAction("A");
        Action actionB = generateTestAction("B");
        Action actionC = generateTestAction("C");
        Action actionD = generateTestAction("D");
        actionA.addTargetAction(actionB);
        actionA.addTargetAction(actionC);
        actionB.addTargetAction(actionD);
        actionD.addTargetAction(actionB);

        NpcDialogueData dialogueData = new NpcDialogueData(new NpcAttributes(), actionA);

        List<Path> pathsToC = new DialogueValidator_old(dialogueData).findAllPathsToSpecificAction(dialogueData.getStartAction(), actionC);
        List<Path> expectedPathsToC = asList(new Path(actionA, actionC));

        AssertUtil.hasSize(1, pathsToC);
        assertTrue(pathsToC.containsAll(expectedPathsToC));
        assertTrue(expectedPathsToC.containsAll(pathsToC));
    }

    /**
     * A[B, C], B[D], D[E], E[B]
     */
    @Test
    public void testFindAllPathsTo_OneWayThrough_WithCircle2() {
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

        List<Path> pathsToC = new DialogueValidator_old(dialogueData).findAllPathsToSpecificAction(dialogueData.getStartAction(), actionC);
        List<Path> expectedPathsToC = asList(new Path(actionA, actionC));

        AssertUtil.hasSize(1, pathsToC);
        assertTrue(pathsToC.containsAll(expectedPathsToC));
        assertTrue(expectedPathsToC.containsAll(pathsToC));
    }

    /**
     * A[B, C], B[D, E], D[B], conditions for E will not be fulfilled
     */
    @Test
    public void testFindAllPathsTo_OneWayThrough_WithCircle_WithCondition() {
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

        actionE.addCondition("reputation","==",  60);

        NpcDialogueData dialogueData = new NpcDialogueData(attributes, actionA);

        List<Path> pathsToC = new DialogueValidator_old(dialogueData).findAllPathsToSpecificAction(dialogueData.getStartAction(), actionC);
        List<Path> expectedPathsToC = asList(new Path(actionA, actionC));

        AssertUtil.hasSize(1, pathsToC);
        assertTrue(pathsToC.containsAll(expectedPathsToC));
        assertTrue(expectedPathsToC.containsAll(pathsToC));
    }

    /**
     * A[B], B[C], condition of C is only fulfilled when B is reached
     */
    @Test
    public void testFindAllPathsTo_WithModificationAndCondition_OneWayThrough() {
        NpcAttributes attributes = new NpcAttributes();
        attributes.addAttribute("reputation", 50);

        Action actionA = generateTestAction("A");
        Action actionB = generateTestAction("B");
        Action actionC = generateTestAction("C");
        actionA.addTargetAction(actionB);
        actionB.addTargetAction(actionC);

        actionB.addNpcAttributeModification("reputation","+",  10);
        actionC.addCondition("reputation","==",  60);

        NpcDialogueData dialogueData = new NpcDialogueData(attributes, actionA);

        List<Path> pathsToC = new DialogueValidator_old(dialogueData).findAllPathsToSpecificAction(dialogueData.getStartAction(), actionC);
        List<Path> expectedPathsToC = asList(new Path(actionA, actionB, actionC));

        AssertUtil.hasSize(1, pathsToC);
        assertTrue(pathsToC.containsAll(expectedPathsToC));
        assertTrue(expectedPathsToC.containsAll(pathsToC));
    }

    /**
     * A[B], B[C], condition of C is not fulfilled because B changes the attributes beforehand
     */
    @Test
    public void testFindAllPathsTo_WithModificationAndCondition_NoWayThrough() {
        NpcAttributes attributes = new NpcAttributes();
        attributes.addAttribute("reputation", 50);

        Action actionA = generateTestAction("A");
        Action actionB = generateTestAction("B");
        Action actionC = generateTestAction("C");
        actionA.addTargetAction(actionB);
        actionB.addTargetAction(actionC);

        actionB.addNpcAttributeModification("reputation","+", 60);
        actionC.addCondition("reputation","==", 50);

        NpcDialogueData dialogueData = new NpcDialogueData(attributes, actionA);

        List<Path> pathsToC = new DialogueValidator_old(dialogueData).findAllPathsToSpecificAction(dialogueData.getStartAction(), actionC);

        AssertUtil.isEmpty(pathsToC);
    }

    /**
     * A[B, D], B[C, A], D[E], B unlocks C and locks E
     */
    @Test
    public void testFindAllPathsTo_WithModificationAndCondition_ThreeWaysThrough() {
        NpcAttributes attributes = new NpcAttributes();
        attributes.addAttribute("reputation", 50);

        Action actionA = generateTestAction("A");
        Action actionB = generateTestAction("B");
        Action actionC = generateTestAction("C");
        Action actionD = generateTestAction("D");
        Action actionE = generateTestAction("E");
        actionA.addTargetAction(actionB);
        actionA.addTargetAction(actionD);
        actionB.addTargetAction(actionC);
        actionD.addTargetAction(actionE);

        actionB.addNpcAttributeModification("reputation","+", 10);
        actionC.addCondition("reputation","==", 60);
        actionE.addCondition("reputation","==", 50);

        NpcDialogueData dialogueData = new NpcDialogueData(attributes, actionA);

        List<Path> pathsToC = new DialogueValidator_old(dialogueData).findAllPathsToSpecificAction(dialogueData.getStartAction(), actionC);
        List<Path> pathsToE = new DialogueValidator_old(dialogueData).findAllPathsToSpecificAction(dialogueData.getStartAction(), actionE);
        List<Path> expectedPathsToC = asList(new Path(actionA, actionB, actionC));
        List<Path> expectedPathsToE = asList(new Path(actionA, actionD, actionE));

        AssertUtil.hasSize(1, pathsToC);
        AssertUtil.hasSize(1, pathsToE);

        assertTrue(pathsToC.containsAll(expectedPathsToC));
        assertTrue(expectedPathsToC.containsAll(pathsToC));

        assertTrue(pathsToE.containsAll(expectedPathsToE));
        assertTrue(expectedPathsToE.containsAll(pathsToE));
    }

    /**
     * A[B, C], B[D, A], D[E, A]
     */
    @Ignore
    @Test
    public void testFindAllPathsTo_TwoNestedCircles() {

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

        List<Path> pathsToC = new DialogueValidator_old(dialogueData).findAllPathsToSpecificAction(dialogueData.getStartAction(), actionC);

        AssertUtil.hasSize(6, pathsToC);
    }
}