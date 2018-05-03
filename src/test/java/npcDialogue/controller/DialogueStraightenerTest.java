package npcDialogue.controller;

import com.queomedia.commons.asserts.AssertUtil;
import npcDialogue.model.*;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertTrue;

public class DialogueStraightenerTest {

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
     * A[B, C], B[D], C[D], B and C are locked
     */
    @Test
    public void testFindAllPathsThroughDialogue_NoPath_WithCondition() {
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

        actionC.addActionCondition("reputation", 60);
        actionB.addActionCondition("reputation", 60);

        NpcDialogueData dialogueData = new NpcDialogueData(attributes, actionA);

        List<Path> paths = new DialogueStraightener(dialogueData).findAllPathsThroughDialogue();

        AssertUtil.isEmpty(paths);
    }

    /**
     * A[B, C]
     */
    @Test
    public void testFindAllPathsThroughDialogue_OnePath_WithCondition() {
        NpcAttributes attributes = new NpcAttributes();
        attributes.addAttribute("reputation", 50);

        Action actionA = generateTestAction("A");
        Action actionB = generateTestAction("B");
        Action actionC = generateTestAction("C");
        actionA.addTargetAction(actionB);
        actionA.addTargetAction(actionC);

        actionB.addActionCondition("reputation", 60);

        NpcDialogueData dialogueData = new NpcDialogueData(attributes, actionA);

        List<Path> paths = new DialogueStraightener(dialogueData).findAllPathsThroughDialogue();
        List<Path> expectedPaths = asList(new Path(actionA, actionC));

        AssertUtil.hasSize(1, paths);

        assertTrue(paths.containsAll(expectedPaths));
        assertTrue(expectedPaths.containsAll(paths));
    }

    /**
     * A[B, C, D], B[E], C[E], D[F], B is locked
     */
    @Test
    public void testFindAllPathsThroughDialogue_TwoPaths_WithCondition() {
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

        List<Path> paths = new DialogueStraightener(dialogueData).findAllPathsThroughDialogue();
        List<Path> expectedPaths = asList(new Path(actionA, actionC, actionE), new Path(actionA, actionD, actionF));

        AssertUtil.hasSize(2, paths);

        assertTrue(paths.containsAll(expectedPaths));
        assertTrue(expectedPaths.containsAll(paths));
    }

    /**
     * A[B, D], B[C], D[E], B unlocks C and locks E
     */
    @Test
    public void testFindAllPathsThroughDialogue_TwoPaths_WithCondition_WithModification() {
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

        actionB.addNpcAttributeModification("reputation", 60);
        actionC.addActionCondition("reputation", 60);
        actionE.addActionCondition("reputation", 50);

        NpcDialogueData dialogueData = new NpcDialogueData(attributes, actionA);

        List<Path> paths = new DialogueStraightener(dialogueData).findAllPathsThroughDialogue();
        List<Path> expectedPaths = asList(new Path(actionA, actionB, actionC), new Path(actionA, actionD, actionE));

        AssertUtil.hasSize(2, paths);

        assertTrue(paths.containsAll(expectedPaths));
        assertTrue(expectedPaths.containsAll(paths));
    }

    /**
     * A[B, C], B[D], D[A]
     */
    @Test
    public void testFindAllPathsThroughDialogue_MultiplePaths_OneCircle() {
        Action actionA = generateTestAction("A");
        Action actionB = generateTestAction("B");
        Action actionC = generateTestAction("C");
        Action actionD = generateTestAction("D");
        actionA.addTargetAction(actionB);
        actionA.addTargetAction(actionC);
        actionB.addTargetAction(actionD);
        actionD.addTargetAction(actionA);

        NpcDialogueData dialogueData = new NpcDialogueData(new NpcAttributes(), actionA);

        List<Path> paths = new DialogueStraightener(dialogueData).findAllPathsThroughDialogue();

        AssertUtil.hasSize(2, paths);
    }

    /**
     * A[B, C], B[D, A], D[B]
     */
    @Ignore
    @Test
    public void testFindAllPathsThroughDialogue_MultiplePaths_TwoCircles() {
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

        List<Path> paths = new DialogueStraightener(dialogueData).findAllPathsThroughDialogue();

        AssertUtil.hasSize(2, paths);
    }

    /**
     * A[B, C], B[D, E], D[B]
     */
    @Test
    public void testFindAllPathsThroughDialogue_OnePath_OneCircle() {
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

        List<Path> paths = new DialogueStraightener(dialogueData).findAllPathsThroughDialogue();
        List<Path> expectedPaths = asList(new Path(actionA, actionC));

        AssertUtil.hasSize(1, paths);

        assertTrue(paths.containsAll(expectedPaths));
        assertTrue(expectedPaths.containsAll(paths));
    }

    /**
     * A[B, C], B[A, D], D[E, A]
     */
    @Ignore
    @Test
    public void testFindAllPathsThroughDialogue_MultiplePaths_TwoNestedCircles() {
        Action actionA = generateTestAction("A");
        Action actionB = generateTestAction("B");
        Action actionC = generateTestAction("C");
        Action actionD = generateTestAction("D");
        Action actionE = generateTestAction("E");
        actionA.addTargetAction(actionB);
        actionA.addTargetAction(actionC);
        actionB.addTargetAction(actionA);
        actionB.addTargetAction(actionD);
        actionD.addTargetAction(actionE);
        actionD.addTargetAction(actionA);

        NpcDialogueData dialogueData = new NpcDialogueData(new NpcAttributes(), actionA);

        List<Path> paths = new DialogueStraightener(dialogueData).findAllPathsThroughDialogue();

        AssertUtil.hasSize(1, paths);
    }
}