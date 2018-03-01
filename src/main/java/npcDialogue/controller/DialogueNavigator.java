package npcDialogue.controller;

import npcDialogue.model.Action;
import npcDialogue.model.Role;
import npcDialogue.model.NpcTraits;
import npcDialogue.view.ConsoleInputOutput;

/**
 * The DialogueNavigator is responsible for navigating from Action to Action in the conversation.
 */
public class DialogueNavigator {

    private Action currentAction;

    public DialogueNavigator(NpcTraits npcTraits, Action startAction) {
        this.currentAction = startAction;
    }

    /**
     * Navigates from action to action until an action has no more targetActions.
     *
     * @param consoleInputOutput the class that prints the text of the actions to the console.
     */
    public void start(ConsoleInputOutput consoleInputOutput) {
        //TODO: conditional printing of targetActions
        consoleInputOutput.printSingleActionText(currentAction);
        while (!currentAction.getTargetActions().isEmpty()) {


            //foreach targetAction in targetActions
                //if (targetAction.getDependencies > 0)
                    //foreach of these dependencies
                        //if (dependency value is equal to value in npcTraits)
                            //print this targetAction
                        //else
                            //don't print it



            if (currentAction.getTargetActions().size() == 1) {
                reassignCurrentAction(consoleInputOutput.chooseRandomly(currentAction.getTargetActions()));
            } else {
                if (currentAction.getTargetActionsRole() == Role.NPC) {
                    reassignCurrentAction(consoleInputOutput.chooseRandomly(currentAction.getTargetActions()));
                } else {
                    Action chosenAction = consoleInputOutput.chooseByPlayerInput(currentAction.getTargetActions());
                    consoleInputOutput.printSingleActionText(chosenAction);
                    reassignCurrentAction(chosenAction);
                }
            }
        }
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
