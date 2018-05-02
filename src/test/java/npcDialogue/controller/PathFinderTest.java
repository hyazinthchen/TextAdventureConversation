package npcDialogue.controller;

import com.queomedia.commons.asserts.AssertUtil;
import npcDialogue.model.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertTrue;

public class PathFinderTest {

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
     * A[B, D], B[C, A], D[E], B unlocks C and locks E
     */
    @Test
    public void testFindAllPathsThroughDialogue() {
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

        List<Path> paths = new PathFinder(dialogueData).findAllPathsThroughDialogue();
        List<Path> expectedPaths = asList(new Path(actionA, actionB, actionC), new Path(actionA, actionD, actionE));

        AssertUtil.hasSize(2, paths);

        assertTrue(paths.containsAll(expectedPaths));
        assertTrue(expectedPaths.containsAll(paths));
    }

    @Test
    public void testPathContainsSuccessiveSequence1() {
        Action actionA = generateTestAction("A");
        Action actionB = generateTestAction("B");
        Action actionC = generateTestAction("C");
        Action actionD = generateTestAction("D");

        Path path1 = new Path(actionA, actionB, actionC, actionA, actionB, actionD);
        Path path2 = new Path(actionA, actionB, actionC, actionA, actionB, actionC);
        Path path3 = new Path(actionA, actionB, actionA, actionB, actionA, actionB);
        Path path4 = new Path(actionA, actionB, actionA, actionB, actionA, actionC);
        Path path5 = new Path(actionA, actionB, actionB, actionB, actionB, actionB);

        NpcDialogueData dialogueData = new NpcDialogueData(new NpcAttributes(), actionA);

        PathFinder pathFinder = new PathFinder(dialogueData);
        Assert.assertFalse(pathFinder.pathContainsSuccessiveSequence1(path1));
        Assert.assertTrue(pathFinder.pathContainsSuccessiveSequence1(path2));
        Assert.assertTrue(pathFinder.pathContainsSuccessiveSequence1(path3));
        Assert.assertTrue(pathFinder.pathContainsSuccessiveSequence1(path4));
        Assert.assertTrue(pathFinder.pathContainsSuccessiveSequence1(path5));
    }

    @Test
    public void testGetSequenceToSearchFor() {
        Action actionA = generateTestAction("A");
        Action actionB = generateTestAction("B");

        Path path = new Path(actionA, actionB, actionA, actionB, actionA, actionB);

        NpcDialogueData dialogueData = new NpcDialogueData(new NpcAttributes(), actionA);

        PathFinder pathFinder = new PathFinder(dialogueData);
        Assert.assertEquals(asList(actionA, actionB), pathFinder.getSequenceToSearchFor(path));
    }
}