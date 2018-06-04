package npcDialogue.controller;

import npcDialogue.model.Action;
import npcDialogue.model.NpcAction;
import npcDialogue.model.Role;
import npcDialogue.model.TreeNode;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Test
    public void testTreeNavigatorTreeEdgesOnly() {
        Action actionA = generateTestAction("A");
        Action actionB = generateTestAction("B");
        Action actionC = generateTestAction("C");
        Action actionD = generateTestAction("D");
        Action actionE = generateTestAction("E");
        Action actionF = generateTestAction("F");
        Action actionG = generateTestAction("G");
        actionA.addTargetAction(actionB);
        actionA.addTargetAction(actionC);
        actionB.addTargetAction(actionD);
        actionB.addTargetAction(actionE);
        actionC.addTargetAction(actionF);
        actionC.addTargetAction(actionG);

        Set<TreeNode> expectedSet = new HashSet<>(Arrays.asList(actionA, actionB, actionC, actionD, actionE, actionF, actionG));
        DialogueValidator dialogueValidator = new DialogueValidator(actionA);
        List<TreeNode> backEdgeList = dialogueValidator.findBackEdges();

        Assert.assertEquals(expectedSet, dialogueValidator.getDone());
        Assert.assertTrue(backEdgeList.isEmpty());
    }

    @Test
    public void testTreeNavigatorOneBackEdge() {
        Action actionA = generateTestAction("A");
        Action actionB = generateTestAction("B");
        Action actionC = generateTestAction("C");
        Action actionD = generateTestAction("D");
        Action actionE = generateTestAction("E");
        Action actionF = generateTestAction("F");
        Action actionG = generateTestAction("G");
        actionA.addTargetAction(actionB);
        actionA.addTargetAction(actionC);
        actionB.addTargetAction(actionD);
        actionB.addTargetAction(actionE);
        actionC.addTargetAction(actionF);
        actionC.addTargetAction(actionG);
        actionD.addTargetAction(actionB);

        Set<TreeNode> expectedSet = new HashSet<>(Arrays.asList(actionA, actionB, actionC, actionD, actionE, actionF, actionG));
        DialogueValidator dialogueValidator = new DialogueValidator(actionA);
        List<TreeNode> backEdgeList = dialogueValidator.findBackEdges();

        Assert.assertEquals(expectedSet, dialogueValidator.getDone());
        Assert.assertTrue(backEdgeList.size() == 1);
    }

    @Test
    public void testTreeNavigatorOneForwardEdge() {
        Action actionA = generateTestAction("A");
        Action actionB = generateTestAction("B");
        Action actionC = generateTestAction("C");
        Action actionD = generateTestAction("D");
        Action actionE = generateTestAction("E");
        Action actionF = generateTestAction("F");
        Action actionG = generateTestAction("G");
        actionA.addTargetAction(actionB);
        actionA.addTargetAction(actionC);
        actionA.addTargetAction(actionG);
        actionB.addTargetAction(actionD);
        actionB.addTargetAction(actionE);
        actionC.addTargetAction(actionF);
        actionC.addTargetAction(actionG);

        Set<TreeNode> expectedSet = new HashSet<>(Arrays.asList(actionA, actionB, actionC, actionD, actionE, actionF, actionG));
        DialogueValidator dialogueValidator = new DialogueValidator(actionA);
        List<TreeNode> backEdgeList = dialogueValidator.findBackEdges();

        Assert.assertEquals(expectedSet, dialogueValidator.getDone());
        Assert.assertTrue(backEdgeList.isEmpty());
    }

}