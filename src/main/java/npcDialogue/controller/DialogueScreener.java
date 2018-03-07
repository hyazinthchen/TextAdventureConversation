package npcDialogue.controller;

import npcDialogue.model.Action;
import npcDialogue.model.NpcDialogueData;

import java.util.ArrayList;
import java.util.List;

/**
 * Checks for errors in a loaded dialogue. Errors can be: dead ends, where no available targetActions are available or cycles.
 */
public class DialogueScreener {

    private NpcDialogueData dialogueData;

    private List<Action> visitedActions;

    private List<Action> endActions;

    private DialogueNavigator navigator;

    public DialogueScreener(NpcDialogueData dialogueData) {
        this.dialogueData = dialogueData;
        this.visitedActions = new ArrayList<>();
        this.endActions = new ArrayList<>();
        this.navigator = new DialogueNavigator(dialogueData.getNpcAttributes(), dialogueData.getStartAction());
    }

    /**
     * Gets a list of actions that do not have available targetActions.
     *
     * @return a list of actions where the dialogue stops.
     */
    public List<Action> screenForEndActions() {

        addEndActionsByDepthFirstSearch(dialogueData.getStartAction());

        return endActions;
    }

    /**
     * Performs a recursive depth first search and adds actions that do not have available targetActions to a list of actions. (leaves of the tree)
     *
     * @param currentAction
     */
    private void addEndActionsByDepthFirstSearch(final Action currentAction) {
        List<Action> availableActions = navigator.getAvailableTargetActions(currentAction.getTargetActions());
        visitedActions.add(currentAction);
        if (availableActions.isEmpty()) {
            endActions.add(currentAction);
        }
        for (Action targetAction : availableActions) {
            if (!visitedActions.contains(targetAction)) {
                addEndActionsByDepthFirstSearch(targetAction);
            }
        }
    }


/*
    public Set<Action> leaves(final Action action) {
        return leavesInternal(action, new HashSet<>());
    }

    private Set<Action> leavesInternal(final Action action, final Set<Action> alreadyVisited) {
        if (alreadyVisited.contains(action)) {
            return Collections.emptySet();
        }
        alreadyVisited.add(action);


        List<Action> targetActions = navigator.getAvailableTargetActions(action.getTargetActions());
        if (targetActions.isEmpty()) {
            return new HashSet<>(Arrays.asList(action));
        } else {
            Set<Action> result = new HashSet<>();
            for (Action targetAction : targetActions) {
                result.addAll(leavesInternal(targetAction, alreadyVisited));
            }
            return result;
        }
    }
*/
}
