package npcDialogue.controller;

import npcDialogue.model.Action;
import npcDialogue.model.ActorType;
import npcDialogue.model.NpcTraits;
import npcDialogue.view.ConsoleInputOutput;

import java.util.Random;

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
     * @param consoleInputOutput the class that prints the text of the actions to the console window
     */
    public void start(ConsoleInputOutput consoleInputOutput) {
        //TODO: conditional printing of targetActions
        while (!this.currentAction.getTargetActions().isEmpty()) {
            consoleInputOutput.printSingleAction(currentAction);
            if (currentActionHasMultipleNpcTargetActions()) {
                printNpcActionRandomly(consoleInputOutput); //TODO: delay?
            }
            if (currentAction.getTargetActions().size() == 1) {
                consoleInputOutput.printSingleAction(currentAction.getTargetActions().get(0));
                currentAction = currentAction.getTargetActions().get(0); //TODO: delay?
            }
            if (currentActionHasMultiplePlayerTargetActions()) {
                printPlayerActionsToChooseFrom(consoleInputOutput);
            }
        }
    }

    /**
     * Checks whether the currentAction has more than one targetAction with ActorType NPC.
     *
     * @return true if currentAction has more than one targetAction with ActorType NPC
     */
    private boolean currentActionHasMultipleNpcTargetActions() {
        return currentAction.getTargetActions().size() > 1 && currentAction.getTargetActionsActorType() == ActorType.NPC;
    }

    /**
     * Checks whether the currentAction has more than one targetAction with ActorType PLAYER.
     *
     * @return true if currentAction has more than one targetAction with ActorType PLAYER
     */
    private boolean currentActionHasMultiplePlayerTargetActions() {
        return currentAction.getTargetActions().size() > 1 && currentAction.getTargetActionsActorType() == ActorType.PLAYER;
    }

    /**
     * Lets the player choose from multiple playerActions and sets the currentAction as his selected Action.
     *
     * @param consoleInputOutput the class that prints the text of the actions to the console window
     */
    private void printPlayerActionsToChooseFrom(ConsoleInputOutput consoleInputOutput) {
        Action selectedAction = consoleInputOutput.choose(currentAction.getTargetActions());
        currentAction = selectedAction;
    }

    /**
     * Decides randomly which sentence the NPC says, when there are multiple options.
     *
     * @param consoleInputOutput the class that prints the text of the actions to the console window
     */
    private void printNpcActionRandomly(ConsoleInputOutput consoleInputOutput) {
        Random random = new Random();
        int randomNumber = random.nextInt(currentAction.getTargetActions().size() - 1);
        currentAction = currentAction.getTargetActions().get(randomNumber);
        consoleInputOutput.printSingleAction(currentAction);
    }
}
