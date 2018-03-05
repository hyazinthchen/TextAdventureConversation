package npcDialogue.controller;

import com.queomedia.commons.asserts.AssertUtil;
import npcDialogue.model.*;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

import static junit.framework.TestCase.assertEquals;

public class DialogueNavigatorTest {

    /**
     * Helper method for generating a simple dialogue to test without any conditions.
     *
     * @param actionTexts
     */
    private ArrayList<Action> generateSimpleActionList(String... actionTexts) {
        ArrayList<Action> testActions = new ArrayList<>();
        for (String actionText : actionTexts) {
            testActions.add(generateSimpleActionList(actionText));
        }
        return testActions;
    }

    /**
     * Helper method for generating a simple dialogue to test without any conditions.
     *
     * @param actionText
     */
    private Action generateSimpleActionList(String actionText) {
        return new NpcAction(Role.NPC, actionText, actionText);
    }

    /**
     * Helper method that generates an NpcTraits object for test purposes.
     *
     * @return
     */
    private NpcTraits generateTestNpcTraits(Object firstValue, Object secondValue) {
        NpcTraits traits = new NpcTraits();
        traits.addDataEntry("D", firstValue);
        traits.addDataEntry("E", secondValue);
        return traits;
    }

    /**
     * A
     */
    @Test
    public void testGetAvailableTargetActions1() {
        Action actionA = generateSimpleActionList("A");
        DialogueNavigator navigator = new DialogueNavigator(new NpcTraits(), actionA);
        ArrayList<Action> availableTargetActionsOfA = navigator.getAvailableTargetActions(actionA.getTargetActions());

        AssertUtil.isEmpty(availableTargetActionsOfA);
    }

    /**
     * A [B]
     */
    @Test
    public void testGetAvailableTargetActions2() {
        ArrayList<Action> actionList = generateSimpleActionList("A", "B");
        actionList.get(0).addTargetAction(actionList.get(1));
        DialogueNavigator navigator = new DialogueNavigator(new NpcTraits(), actionList.get(0));
        ArrayList<Action> availableTargetActionsOfA = navigator.getAvailableTargetActions(actionList.get(0).getTargetActions());
        ArrayList<Action> availableTargetActionsOfB = navigator.getAvailableTargetActions(actionList.get(1).getTargetActions());

        AssertUtil.containsExact(Arrays.asList("B"), availableTargetActionsOfA, Action.ACTION_BY_TEXT_EQUALS_CHECKER);
        AssertUtil.isEmpty(availableTargetActionsOfB);
    }

    /**
     * A [B, C], B[D], C[D]
     */
    @Test
    public void testGetAvailableTargetActions3() {

        Action actionA = generateSimpleActionList("A");
        Action actionB = generateSimpleActionList("B");
        Action actionC = generateSimpleActionList("A");
        Action actionD = generateSimpleActionList("D");

        actionA.addTargetAction(actionB);
        actionA.addTargetAction(actionC);
        actionB.addTargetAction(actionD);
        actionC.addTargetAction(actionD);
        DialogueNavigator navigator = new DialogueNavigator(new NpcTraits(), actionA);
        ArrayList<Action> availableTargetActionsOfA = navigator.getAvailableTargetActions(Arrays.asList(actionA));
        ArrayList<Action> availableTargetActionsOfB = navigator.getAvailableTargetActions(Arrays.asList(actionB));
        ArrayList<Action> availableTargetActionsOfC = navigator.getAvailableTargetActions(Arrays.asList(actionC));
        ArrayList<Action> availableTargetActionsOfD = navigator.getAvailableTargetActions(Arrays.asList(actionD));

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
        ArrayList<Action> actionList = generateSimpleActionList(new String[]{"A", "B", "C", "D"});
        actionList.get(0).addTargetAction(actionList.get(1));
        actionList.get(0).addTargetAction(actionList.get(2));
        actionList.get(1).addTargetAction(actionList.get(3));
        actionList.get(2).addTargetAction(actionList.get(1));
        DialogueNavigator navigator = new DialogueNavigator(new NpcTraits(), actionList.get(0));
        ArrayList<Action> availableTargetActionsOfA = navigator.getAvailableTargetActions(actionList.get(0).getTargetActions());
        ArrayList<Action> availableTargetActionsOfB = navigator.getAvailableTargetActions(actionList.get(1).getTargetActions());
        ArrayList<Action> availableTargetActionsOfC = navigator.getAvailableTargetActions(actionList.get(2).getTargetActions());
        ArrayList<Action> availableTargetActionsOfD = navigator.getAvailableTargetActions(actionList.get(3).getTargetActions());

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
        ArrayList<Action> actionList = generateSimpleActionList(new String[]{"A", "B", "C", "D"});
        actionList.get(0).addTargetAction(actionList.get(1));
        actionList.get(0).addTargetAction(actionList.get(2));
        actionList.get(1).addTargetAction(actionList.get(3));
        actionList.get(1).addTargetAction(actionList.get(2));
        DialogueNavigator navigator = new DialogueNavigator(new NpcTraits(), actionList.get(0));
        ArrayList<Action> availableTargetActionsOfA = navigator.getAvailableTargetActions(actionList.get(0).getTargetActions());
        ArrayList<Action> availableTargetActionsOfB = navigator.getAvailableTargetActions(actionList.get(1).getTargetActions());
        ArrayList<Action> availableTargetActionsOfC = navigator.getAvailableTargetActions(actionList.get(2).getTargetActions());
        ArrayList<Action> availableTargetActionsOfD = navigator.getAvailableTargetActions(actionList.get(3).getTargetActions());

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
        ArrayList<Action> actionList = generateSimpleActionList(new String[]{"A", "B"});
        actionList.get(0).addTargetAction(actionList.get(1));
        actionList.get(1).addTargetAction(actionList.get(0));
        DialogueNavigator navigator = new DialogueNavigator(new NpcTraits(), actionList.get(0));
        ArrayList<Action> availableTargetActionsOfA = navigator.getAvailableTargetActions(actionList.get(0).getTargetActions());
        ArrayList<Action> availableTargetActionsOfB = navigator.getAvailableTargetActions(actionList.get(1).getTargetActions());

        AssertUtil.containsExact(Arrays.asList("B"), availableTargetActionsOfA, Action.ACTION_BY_TEXT_EQUALS_CHECKER);
        AssertUtil.containsExact(Arrays.asList("A"), availableTargetActionsOfB, Action.ACTION_BY_TEXT_EQUALS_CHECKER);
    }

    /**
     * To be able to see option "C" two conditions must be true. Here both conditions are true.
     */
    @Test
    public void testGetAvailableTargetActionsWithConditions1() {
        ArrayList<Action> actionList = generateSimpleActionList(new String[]{"A", "B", "C"});
        actionList.get(0).addTargetAction(actionList.get(1));
        actionList.get(0).addTargetAction(actionList.get(2));
        DialogueNavigator navigator = new DialogueNavigator(generateTestNpcTraits(50, true), actionList.get(0));

        navigator.getCurrentAction().getTargetActionByName("C").addActionConditions("D", 50);
        navigator.getCurrentAction().getTargetActionByName("C").addActionConditions("E", true);

        ArrayList<Action> availableTargetActions = navigator.getAvailableTargetActions(actionList.get(0).getTargetActions());

        AssertUtil.containsExact(Arrays.asList("B", "C"), availableTargetActions, Action.ACTION_BY_TEXT_EQUALS_CHECKER);
    }

    /**
     * To be able to see option "C" two conditions must be true. Here one condition is true.
     */
    @Test
    public void testGetAvailableTargetActionsWithConditions2() {
        ArrayList<Action> actionList = generateSimpleActionList(new String[]{"A", "B", "C"});
        actionList.get(0).addTargetAction(actionList.get(1));
        actionList.get(0).addTargetAction(actionList.get(2));
        DialogueNavigator navigator = new DialogueNavigator(generateTestNpcTraits(60, true), actionList.get(0));

        navigator.getCurrentAction().getTargetActionByName("C").addActionConditions("D", 50);
        navigator.getCurrentAction().getTargetActionByName("C").addActionConditions("E", true);

        ArrayList<Action> availableTargetActions = navigator.getAvailableTargetActions(actionList.get(0).getTargetActions());

        AssertUtil.containsExact(Arrays.asList("B"), availableTargetActions, Action.ACTION_BY_TEXT_EQUALS_CHECKER);
    }

    /**
     * To be able to see option "C" two conditions must be true. Here both conditions are false.
     */
    @Test
    public void testGetAvailableTargetActionsWithConditions3() {
        ArrayList<Action> actionList = generateSimpleActionList(new String[]{"A", "B", "C"});
        actionList.get(0).addTargetAction(actionList.get(1));
        actionList.get(0).addTargetAction(actionList.get(2));
        DialogueNavigator navigator = new DialogueNavigator(generateTestNpcTraits(60, false), actionList.get(0));

        navigator.getCurrentAction().getTargetActionByName("C").addActionConditions("D", 50);
        navigator.getCurrentAction().getTargetActionByName("C").addActionConditions("E", true);

        ArrayList<Action> availableTargetActions = navigator.getAvailableTargetActions(actionList.get(0).getTargetActions());

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

}