package npcDialogue.controller;

import npcDialogue.model.Action;
import npcDialogue.model.NpcTraits;
import npcDialogue.model.Role;
import npcDialogue.view.ConsoleInputOutput;

import java.util.*;

/**
 * The DialogueNavigator is responsible for navigating from Action to Action in the conversation.
 */
public class DialogueNavigator {

    private Action currentAction;
    private NpcTraits npcTraits;
    private ArrayList<Action> availableTargetActions;

    public DialogueNavigator(NpcTraits npcTraits, Action startAction) {
        this.currentAction = startAction;
        this.npcTraits = npcTraits;
        this.availableTargetActions = new ArrayList<>();
    }

    /**
     * Navigates from action to action until an action has no more targetActions.
     *
     * @param consoleInputOutput the class that prints the text of the actions to the console.
     */
    public void navigate(ConsoleInputOutput consoleInputOutput) {
        consoleInputOutput.printSingleActionText(currentAction);
        while (!currentAction.getTargetActions().isEmpty()) {
            ArrayList<Action> availableTargetActions = getAvailableTargetActions(currentAction.getTargetActions());
            if (availableTargetActions.size() == 1) {
                consoleInputOutput.printSingleActionText(availableTargetActions.get(0));
                reassignCurrentAction(availableTargetActions.get(0));
            } else {
                if (currentAction.getTargetActionsRole() == Role.NPC) {
                    Action chosenAction = chooseRandomly(availableTargetActions);
                    consoleInputOutput.printSingleActionText(chosenAction);
                    reassignCurrentAction(chosenAction);
                } else {
                    Action chosenAction = consoleInputOutput.chooseByPlayerInput(availableTargetActions);
                    consoleInputOutput.printSingleActionText(chosenAction);
                    reassignCurrentAction(chosenAction);
                }
            }
            availableTargetActions.clear();
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
     * Only gets the targetActions that do not depend on any npcTraits and those that depend on npcTraits but fulfill their dependency.
     *
     * @param targetActions all targetActions.
     * @return only targetActions that should be available.
     */
    private ArrayList<Action> getAvailableTargetActions(ArrayList<Action> targetActions) { //TODO: make public to be able to test it
        for (Action targetAction : targetActions) {
            Map<String, Object> fulfilledConditions = new HashMap<>();
            for (Map.Entry<String, Object> dependencyEntry : targetAction.getActionConditions().entrySet()) {
                if (dependencyEntry.getValue().equals(npcTraits.getNpcTraits().get(dependencyEntry.getKey()))) {
                    fulfilledConditions.put(dependencyEntry.getKey(), dependencyEntry.getValue());
                }
            }
            //check if all conditions are fulfilled, not only one
            if (fulfilledConditions.size() == targetAction.getActionConditions().size()) {
                availableTargetActions.add(targetAction);
            }
            if (targetAction.getActionConditions().isEmpty()) {
                availableTargetActions.add(targetAction);
            }
        }
        return availableTargetActions;
    }

    /**
     * Reassigns the value of currentAction to the next action that has been chosen by the player or by the ConsoleInputOutput.
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
