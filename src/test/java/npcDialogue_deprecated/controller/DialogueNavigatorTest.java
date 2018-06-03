package npcDialogue_deprecated.controller;

import npcDialogue_deprecated.model.*;
import npcDialogue_deprecated.view.ConsoleReaderWriter;
import org.junit.Ignore;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;


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
    private NpcAttributes generateTestNpcAttributes(Integer firstValue, Integer secondValue) {
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

        assertTrue(availableTargetActionsOfA.isEmpty());

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

        assertEquals(1, availableTargetActionsOfA.size());
        assertTrue(availableTargetActionsOfA.contains(actionB));
        assertTrue(availableTargetActionsOfB.isEmpty());
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

        assertEquals(2, availableTargetActionsOfA.size());
        assertTrue(availableTargetActionsOfA.containsAll(asList(actionB, actionC)));

        assertEquals(1, availableTargetActionsOfB.size());
        assertTrue(availableTargetActionsOfB.contains(actionD));

        assertEquals(1, availableTargetActionsOfC.size());
        assertTrue(availableTargetActionsOfC.contains(actionD));

        assertTrue(availableTargetActionsOfD.isEmpty());
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

        assertEquals(2, availableTargetActionsOfA.size());
        assertTrue(availableTargetActionsOfA.containsAll(asList(actionB, actionC)));


        assertEquals(1, availableTargetActionsOfB.size());
        assertTrue(availableTargetActionsOfB.contains(actionD));

        assertEquals(1, availableTargetActionsOfC.size());
        assertTrue(availableTargetActionsOfC.contains(actionB));

        assertTrue(availableTargetActionsOfD.isEmpty());
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

        assertEquals(2, availableTargetActionsOfA.size());
        assertTrue(availableTargetActionsOfA.containsAll(asList(actionB, actionC)));

        assertEquals(2, availableTargetActionsOfB.size());
        assertTrue(availableTargetActionsOfB.containsAll(asList(actionC, actionD)));

        assertTrue(availableTargetActionsOfC.isEmpty());
        assertTrue(availableTargetActionsOfD.isEmpty());
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

        assertEquals(1, availableTargetActionsOfA.size());
        assertTrue(availableTargetActionsOfA.contains(actionB));

        assertEquals(1, availableTargetActionsOfB.size());
        assertTrue(availableTargetActionsOfB.contains(actionA));

    }

    /**
     * To be able to see option "C" two conditions must be true. Here both conditions are true.
     */
    @Test
    public void testGetAvailableTargetActionsWithConditions_fullMatch() {
        Action actionA = generateTestAction("A");
        Action actionB = generateTestAction("B");
        Action actionC = generateTestAction("C");

        actionA.addTargetAction(actionB);
        actionA.addTargetAction(actionC);
        DialogueNavigator navigator = new DialogueNavigator(generateTestNpcAttributes(50, 25), actionA);

        actionC.addCondition("D", "==", 50);
        actionC.addCondition("E", "==", 25);

        List<Action> availableTargetActions = navigator.getAvailableTargetActions(actionA.getTargetActions());

        assertEquals(2, availableTargetActions.size());
        assertTrue(availableTargetActions.containsAll(asList(actionB, actionC)));
    }

    /**
     * To be able to see option "C" two conditions must be true. Here one condition is true.
     */
    @Test
    public void testGetAvailableTargetActionsWithConditions_singleMatch() {
        Action actionA = generateTestAction("A");
        Action actionB = generateTestAction("B");
        Action actionC = generateTestAction("C");

        actionA.addTargetAction(actionB);
        actionA.addTargetAction(actionC);
        DialogueNavigator navigator = new DialogueNavigator(generateTestNpcAttributes(60, 30), actionA);

        actionC.addCondition("D", "==", 50);
        actionC.addCondition("E", "==", 25);

        List<Action> availableTargetActions = navigator.getAvailableTargetActions(actionA.getTargetActions());

        assertEquals(1, availableTargetActions.size());
        assertTrue(availableTargetActions.contains(actionB));
    }

    /**
     * To be able to see option "C" two conditions must be true. Here both conditions are false.
     */
    @Test
    public void testGetAvailableTargetActionsWithConditions_fullMismatch() {
        Action actionA = generateTestAction("A");
        Action actionB = generateTestAction("B");
        Action actionC = generateTestAction("C");

        actionA.addTargetAction(actionB);
        actionA.addTargetAction(actionC);
        DialogueNavigator navigator = new DialogueNavigator(generateTestNpcAttributes(60, 25), actionA);

        actionC.addCondition("D","==", 50);
        actionC.addCondition("E", "==", 30);

        List<Action> availableTargetActions = navigator.getAvailableTargetActions(actionA.getTargetActions());

        assertEquals(1, availableTargetActions.size());
        assertTrue(availableTargetActions.contains(actionB));
    }

    @Test
    public void testNavigateByLastCurrentAction() throws FileNotFoundException, ParsingException {
        DialogueLoader loader = new DialogueLoader();
        NpcDialogueData dialogueData = loader.load(loader.getFileFromClassPath("merchant2Dialogue.yml"));
        dialogueData.start();
        DialogueNavigator navigator = dialogueData.getDialogueNavigator();

        assertEquals("I don't want to sell anything to you!", navigator.getCurrentAction().getActionText());
    }

    /**
     * Produces a dead end where an action has no available targetActions and the dialogue stops.
     */
    @Test
    public void testNavigateByDeadEnd() {
        Action actionA = generateTestAction("A");
        Action actionB = generateTestAction("B");

        actionA.addTargetAction(actionB);
        DialogueNavigator navigator = new DialogueNavigator(generateTestNpcAttributes(50, 25), actionA);
        actionB.addCondition("D", "==",99);
        actionB.addCondition("E", "==", 30);

        navigator.navigate(new ConsoleReaderWriter());

        assertEquals("A", navigator.getCurrentAction().getActionText());
        assertEquals(0, navigator.getAvailableTargetActions(navigator.getCurrentAction().getTargetActions()).size());
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