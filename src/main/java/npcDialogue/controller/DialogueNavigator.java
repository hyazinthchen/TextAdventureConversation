package npcDialogue.controller;

import npcDialogue.model.Action;
import npcDialogue.model.NpcTraits;
import npcDialogue.model.Role;
import npcDialogue.view.ConsoleReaderWriter;

import java.util.*;

/**
 * The DialogueNavigator is responsible for navigating from Action to Action in the conversation.
 */
public class DialogueNavigator {

    private Action currentAction;
    private NpcTraits npcTraits;

    public DialogueNavigator(NpcTraits npcTraits, Action startAction) {
        this.currentAction = startAction;
        this.npcTraits = npcTraits;
    }

    /**
     * Navigates from action to action until an action has no more targetActions.
     *
     * @param consoleReaderWriter the class that prints the text of the actions to the console.
     */
    public void navigate(ConsoleReaderWriter consoleReaderWriter) {
        consoleReaderWriter.printSingleActionText(currentAction);
        while (!currentAction.getTargetActions().isEmpty()) {
            ArrayList<Action> availableTargetActions = getAvailableTargetActions(currentAction.getTargetActions());
            if (availableTargetActions.size() == 1) {
                consoleReaderWriter.printSingleActionText(availableTargetActions.get(0));
                reassignCurrentAction(availableTargetActions.get(0));
            } else {
                if (currentAction.getTargetActionsRole() == Role.NPC) {
                    Action chosenAction = chooseRandomly(availableTargetActions);
                    consoleReaderWriter.printSingleActionText(chosenAction);
                    reassignCurrentAction(chosenAction);
                } else {
                    Action chosenAction = consoleReaderWriter.chooseByPlayerInput(availableTargetActions);
                    consoleReaderWriter.printSingleActionText(chosenAction);
                    reassignCurrentAction(chosenAction);
                }
            }
        }
    }

    /**
     * Chooses an action randomly from a list of actions and returns it.
     *
     * @param availableActions the list of available actions.
     * @return the chosen action.
     */
    private Action chooseRandomly(List<Action> availableActions) {
        Random random = new Random();
        int randomNumber = random.nextInt(availableActions.size());
        Action action = availableActions.get(randomNumber);
        return action;
    }


    /**
     * Only gets the targetActions that do not depend on any npcTraits and those that depend on npcTraits but fulfill their condition.
     *
     * @param targetActions all targetActions.
     * @return only targetActions that should be available.
     */
    public ArrayList<Action> getAvailableTargetActions(List<Action> targetActions) {
        ArrayList<Action> availableTargetActions = new ArrayList<>();
        for (Action targetAction : targetActions) {
            Map<String, Object> fulfilledConditions = new HashMap<>();
            for (Map.Entry<String, Object> conditionEntry : targetAction.getActionConditions().entrySet()) {
                if (conditionEntry.getValue().equals(npcTraits.getNpcTraits().get(conditionEntry.getKey()))) {
                    fulfilledConditions.put(conditionEntry.getKey(), conditionEntry.getValue());
                }
            }
            //check if all conditions are fulfilled, not only one
            if (fulfilledConditions.size() == targetAction.getActionConditions().size() && !targetAction.getActionConditions().isEmpty()) {
                availableTargetActions.add(targetAction);
            }
            if (targetAction.getActionConditions().isEmpty()) {
                availableTargetActions.add(targetAction);
            }
        }
        return availableTargetActions;
    }

    /**
     * Reassigns the value of currentAction to the next action that has been chosen by the player or by the ConsoleReaderWriter.
     *
     * @param nextAction the action that is going to be currentAction.
     */
    private void reassignCurrentAction(Action nextAction) {
        currentAction = nextAction;
    }

    public Action getCurrentAction() {
        return currentAction;
    }
}
