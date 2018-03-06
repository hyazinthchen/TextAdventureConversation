package npcDialogue.controller;

import com.queomedia.commons.asserts.AssertUtil;
import npcDialogue.model.*;
import npcDialogue.view.ConsoleReaderWriter;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class DialogueNavigatorTest {

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
     * /**
     * Helper method that generates an NpcAttributes object for test purposes.
     *
     * @param firstValue  the value of the first attribute.
     * @param secondValue the value of the second attribute.
     * @return an npcAttributes object with two entries.
     */
    private NpcAttributes generateTestNpcAttributes(Object firstValue, Object secondValue) {
        NpcAttributes attributes = new NpcAttributes();
        attributes.addAttribute("D", firstValue);
        attributes.addAttribute("E", secondValue);
        return attributes;
    }

    /**
     * A
     */
    @Test
    public void testGetAvailableTargetActions1() {
        Action actionA = generateTestAction("A");

        DialogueNavigator navigator = new DialogueNavigator(new NpcAttributes(), actionA);
        List<Action> availableTargetActionsOfA = navigator.getAvailableTargetActions(actionA.getTargetActions());

        AssertUtil.isEmpty(availableTargetActionsOfA);
    }

    /**
     * A [B]
     */
    @Test
    public void testGetAvailableTargetActions2() {
        Action actionA = generateTestAction("A");
        Action actionB = generateTestAction("B");

        actionA.addTargetAction(actionB);
        DialogueNavigator navigator = new DialogueNavigator(new NpcAttributes(), actionA);
        List<Action> availableTargetActionsOfA = navigator.getAvailableTargetActions(actionA.getTargetActions());
        List<Action> availableTargetActionsOfB = navigator.getAvailableTargetActions(actionB.getTargetActions());

        AssertUtil.containsExact(Arrays.asList("B"), availableTargetActionsOfA, Action.ACTION_BY_TEXT_EQUALS_CHECKER);
        AssertUtil.isEmpty(availableTargetActionsOfB);
    }

    /**
     * A [B, C], B[D], C[D]
     */
    @Test
    public void testGetAvailableTargetActions3() {

        Action actionA = generateTestAction("A");
        Action actionB = generateTestAction("B");
        Action actionC = generateTestAction("C");
        Action actionD = generateTestAction("D");

        actionA.addTargetAction(actionB);
        actionA.addTargetAction(actionC);
        actionB.addTargetAction(actionD);
        actionC.addTargetAction(actionD);

        DialogueNavigator navigator = new DialogueNavigator(new NpcAttributes(), actionA);
        List<Action> availableTargetActionsOfA = navigator.getAvailableTargetActions(actionA.getTargetActions());
        List<Action> availableTargetActionsOfB = navigator.getAvailableTargetActions(actionB.getTargetActions());
        List<Action> availableTargetActionsOfC = navigator.getAvailableTargetActions(actionC.getTargetActions());
        List<Action> availableTargetActionsOfD = navigator.getAvailableTargetActions(actionD.getTargetActions());

        AssertUtil.containsExact(Arrays.asList("B", "C"), availableTargetActionsOfA, Action.ACTION_BY_TEXT_EQUALS_CHECKER);
        AssertUtil.containsExact(Arrays.asList("D"), availableTargetActionsOfB, Action.ACTION_BY_TEXT_EQUALS_CHECKER);
        AssertUtil.containsExact(Arrays.asList("D"), availableTargetActionsOfC, Action.ACTION_BY_TEXT_EQUALS_CHECKER);
        AssertUtil.isEmpty(availableTargetActionsOfD);
    }

    /**
     * A[B, C], B[D], C[B]
     */
    @Test
    public void testGetAvailableTargetActions4() {
        Action actionA = generateTestAction("A");
        Action actionB = generateTestAction("B");
        Action actionC = generateTestAction("C");
        Action actionD = generateTestAction("D");

        actionA.addTargetAction(actionB);
        actionA.addTargetAction(actionC);
        actionB.addTargetAction(actionD);
        actionC.addTargetAction(actionB);

        DialogueNavigator navigator = new DialogueNavigator(new NpcAttributes(), actionA);
        List<Action> availableTargetActionsOfA = navigator.getAvailableTargetActions(actionA.getTargetActions());
        List<Action> availableTargetActionsOfB = navigator.getAvailableTargetActions(actionB.getTargetActions());
        List<Action> availableTargetActionsOfC = navigator.getAvailableTargetActions(actionC.getTargetActions());
        List<Action> availableTargetActionsOfD = navigator.getAvailableTargetActions(actionD.getTargetActions());

        AssertUtil.containsExact(Arrays.asList("B", "C"), availableTargetActionsOfA, Action.ACTION_BY_TEXT_EQUALS_CHECKER);
        AssertUtil.containsExact(Arrays.asList("D"), availableTargetActionsOfB, Action.ACTION_BY_TEXT_EQUALS_CHECKER);
        AssertUtil.containsExact(Arrays.asList("B"), availableTargetActionsOfC, Action.ACTION_BY_TEXT_EQUALS_CHECKER);
        AssertUtil.isEmpty(availableTargetActionsOfD);
    }

    /**
     * A[B, C], B[C, D]
     */
    @Test
    public void testGetAvailableTargetActions5() {
        Action actionA = generateTestAction("A");
        Action actionB = generateTestAction("B");
        Action actionC = generateTestAction("C");
        Action actionD = generateTestAction("D");

        actionA.addTargetAction(actionB);
        actionA.addTargetAction(actionC);
        actionB.addTargetAction(actionC);
        actionB.addTargetAction(actionD);

        DialogueNavigator navigator = new DialogueNavigator(new NpcAttributes(), actionA);
        List<Action> availableTargetActionsOfA = navigator.getAvailableTargetActions(actionA.getTargetActions());
        List<Action> availableTargetActionsOfB = navigator.getAvailableTargetActions(actionB.getTargetActions());
        List<Action> availableTargetActionsOfC = navigator.getAvailableTargetActions(actionC.getTargetActions());
        List<Action> availableTargetActionsOfD = navigator.getAvailableTargetActions(actionD.getTargetActions());

        AssertUtil.containsExact(Arrays.asList("B", "C"), availableTargetActionsOfA, Action.ACTION_BY_TEXT_EQUALS_CHECKER);
        AssertUtil.containsExact(Arrays.asList("C", "D"), availableTargetActionsOfB, Action.ACTION_BY_TEXT_EQUALS_CHECKER);
        AssertUtil.isEmpty(availableTargetActionsOfC);
        AssertUtil.isEmpty(availableTargetActionsOfD);
    }

    /**
     * A[B], B[A]
     */
    @Test
    public void testGetAvailableTargetActions6() {
        Action actionA = generateTestAction("A");
        Action actionB = generateTestAction("B");

        actionA.addTargetAction(actionB);
        actionB.addTargetAction(actionA);

        DialogueNavigator navigator = new DialogueNavigator(new NpcAttributes(), actionA);
        List<Action> availableTargetActionsOfA = navigator.getAvailableTargetActions(actionA.getTargetActions());
        List<Action> availableTargetActionsOfB = navigator.getAvailableTargetActions(actionB.getTargetActions());

        AssertUtil.containsExact(Arrays.asList("B"), availableTargetActionsOfA, Action.ACTION_BY_TEXT_EQUALS_CHECKER);
        AssertUtil.containsExact(Arrays.asList("A"), availableTargetActionsOfB, Action.ACTION_BY_TEXT_EQUALS_CHECKER);
    }

    /**
     * To be able to see option "C" two conditions must be true. Here both conditions are true.
     */
    @Test
    public void testGetAvailableTargetActionsWithConditions1() {
        Action actionA = generateTestAction("A");
        Action actionB = generateTestAction("B");
        Action actionC = generateTestAction("C");

        actionA.addTargetAction(actionB);
        actionA.addTargetAction(actionC);
        DialogueNavigator navigator = new DialogueNavigator(generateTestNpcAttributes(50, true), actionA);

        navigator.getCurrentAction().getTargetActionByName("C").addActionCondition("D", 50);
        navigator.getCurrentAction().getTargetActionByName("C").addActionCondition("E", true);

        List<Action> availableTargetActions = navigator.getAvailableTargetActions(actionA.getTargetActions());

        AssertUtil.containsExact(Arrays.asList("B", "C"), availableTargetActions, Action.ACTION_BY_TEXT_EQUALS_CHECKER);
    }

    /**
     * To be able to see option "C" two conditions must be true. Here one condition is true.
     */
    @Test
    public void testGetAvailableTargetActionsWithConditions2() {
        Action actionA = generateTestAction("A");
        Action actionB = generateTestAction("B");
        Action actionC = generateTestAction("C");

        actionA.addTargetAction(actionB);
        actionA.addTargetAction(actionC);
        DialogueNavigator navigator = new DialogueNavigator(generateTestNpcAttributes(60, true), actionA);

        navigator.getCurrentAction().getTargetActionByName("C").addActionCondition("D", 50);
        navigator.getCurrentAction().getTargetActionByName("C").addActionCondition("E", true);

        List<Action> availableTargetActions = navigator.getAvailableTargetActions(actionA.getTargetActions());

        AssertUtil.containsExact(Arrays.asList("B"), availableTargetActions, Action.ACTION_BY_TEXT_EQUALS_CHECKER);
    }

    /**
     * To be able to see option "C" two conditions must be true. Here both conditions are false.
     */
    @Test
    public void testGetAvailableTargetActionsWithConditions3() {
        Action actionA = generateTestAction("A");
        Action actionB = generateTestAction("B");
        Action actionC = generateTestAction("C");

        actionA.addTargetAction(actionB);
        actionA.addTargetAction(actionC);
        DialogueNavigator navigator = new DialogueNavigator(generateTestNpcAttributes(60, false), actionA);

        navigator.getCurrentAction().getTargetActionByName("C").addActionCondition("D", 50);
        navigator.getCurrentAction().getTargetActionByName("C").addActionCondition("E", true);

        List<Action> availableTargetActions = navigator.getAvailableTargetActions(actionA.getTargetActions());

        AssertUtil.containsExact(Arrays.asList("B"), availableTargetActions, Action.ACTION_BY_TEXT_EQUALS_CHECKER);
    }

    @Test
    public void testNavigateByLastCurrentAction() throws FileNotFoundException {
        DialogueLoader loader = new DialogueLoader();
        NpcDialogueData dialogueData = loader.load(loader.getFileFromClassPath("merchant2Dialogue.yml"));
        dialogueData.start();
        DialogueNavigator navigator = dialogueData.getDialogueNavigator();

        assertEquals("I don't want to sell anything to you!", navigator.getCurrentAction().getActionText());
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Produces a dead end. Expects an IllegalArgumentException.
     */
    @Test
    public void testNavigateByDeadEnd() throws IllegalArgumentException {
        Action actionA = generateTestAction("A");
        Action actionB = generateTestAction("B");

        actionA.addTargetAction(actionB);
        DialogueNavigator navigator = new DialogueNavigator(generateTestNpcAttributes(50, true), actionA);
        actionB.addActionCondition("D", 99);
        actionB.addActionCondition("E", false);

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Dead end reached. availableTargetActions = 0");
        navigator.navigate(new ConsoleReaderWriter());
    }

    /**
     * Produces an endless loop resulting in a stack overflow
     */
    @Ignore
    @Test
    public void testNavigateByEndlessLoop() {
        Action actionA = generateTestAction("A");
        Action actionB = generateTestAction("B");

        actionA.addTargetAction(actionB);
        actionB.addTargetAction(actionA);
        DialogueNavigator navigator = new DialogueNavigator(new NpcAttributes(), actionA);
        navigator.navigate(new ConsoleReaderWriter());
    }

}