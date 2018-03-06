package npcDialogue.controller;

import npcDialogue.model.Action;
import npcDialogue.model.NpcAttributes;
import npcDialogue.model.Role;
import npcDialogue.view.ConsoleReaderWriter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * The DialogueNavigator is responsible for navigating from Action to Action in the conversation.
 */
public class DialogueNavigator {

    private Action currentAction;
    private NpcAttributes npcAttributes;

    public DialogueNavigator(NpcAttributes npcAttributes, Action startAction) {
        this.currentAction = startAction;
        this.npcAttributes = npcAttributes;
    }

    /**
     * Navigates from action to action until an action has no more targetActions.
     *
     * @param consoleReaderWriter the class that prints the text of the actions to the console.
     */
    public void navigate(ConsoleReaderWriter consoleReaderWriter) { //TODO: detect cycles & detect dead ends
        modifyNpcAttributes();
        consoleReaderWriter.printSingleActionText(currentAction);
        while (!currentAction.getTargetActions().isEmpty()) {
            List<Action> availableTargetActions = getAvailableTargetActions(currentAction.getTargetActions());
            if (availableTargetActions.size() == 1) {
                consoleReaderWriter.printSingleActionText(availableTargetActions.get(0));
                reassignCurrentAction(availableTargetActions.get(0));
            } else if (availableTargetActions.size() == 0) { //TODO: remove me. I'm currently only for test purposes.
                throw new IllegalArgumentException("Dead end reached. availableTargetActions = " + availableTargetActions.size());
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
     * Modifies the npcAttributes if the currentAction has one or more npcAttributeModifications.
     */
    private void modifyNpcAttributes() {
        for (Map.Entry<String, Object> modification : currentAction.getNpcAttributeModifications().entrySet()) {
            npcAttributes.modifyAttribute(modification.getKey(), modification.getValue());
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
        return availableActions.get(randomNumber);
    }

    /**
     * Only gets the targetActions that do not depend on any npcAttributes and those that depend on npcAttributes but fulfill their condition.
     *
     * @param targetActions all targetActions.
     * @return only targetActions that should be available.
     */
    public List<Action> getAvailableTargetActions(List<Action> targetActions) {
        List<Action> availableTargetActions = new ArrayList<>();
        for (Action targetAction : targetActions) {
            if (npcAttributes.contain(targetAction.getActionConditions().entrySet())) {
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
