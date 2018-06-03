package npcDialogue_deprecated.controller;

import npcDialogue_deprecated.model.Action;
import npcDialogue_deprecated.model.NpcAttributes;
import npcDialogue_deprecated.model.NpcDialogueData;
import npcDialogue_deprecated.model.Path;
import npcDialogue_deprecated.view.ConsoleReaderWriter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Checks for errors in a loaded dialogue. Errors can be: dead ends, where no available targetActions are available or cycles without any exits.
 */
public class DialogueValidator_old {

    private final NpcDialogueData dialogueData;

    private boolean cycleFound;

    public DialogueValidator_old(final NpcDialogueData dialogueData) {
        this.dialogueData = dialogueData;
    }

    /**
     * Returns whether the dialogue is valid. It is valid when (A) - an endAction can be reached and (B) - the user can't get caught in an infinite cycle.
     *
     * @return true when the dialogue is valid
     */
    public boolean isValid() {
        if (findAllPathsToAllEndActionsFrom(dialogueData.getStartAction()).isEmpty()) {
            new ConsoleReaderWriter().printErrorMessage("Error in loaded dialogue. Dialogue will never reach a desired end.");
            return false;
        }
        if (cycleFound) {
            new ConsoleReaderWriter().printErrorMessage("Error in loaded dialogue. Dialogue contains loops which the player might never exit. Dialogue can be played regardless.");
        }
        return true;
    }

    /**
     * Gets a list of actions that do not have targetActions.
     *
     * @param startAction the action from which the search for endActions should commence
     * @return a list of end points of the dialogue.
     */
    public List<Action> findEndActionsFrom(Action startAction) {
        List<Action> endActions = new ArrayList<>();
        List<Action> visitedActions = new ArrayList<>();
        addEndActionsByDepthFirstSearch(startAction, endActions, visitedActions);

        return endActions;
    }

    /**
     * Performs a recursive depth first search and adds actions that can be reached and do not have targetActions to a list of actions.
     *
     * @param currentAction  the action from which the search for endActions should commence
     * @param endActions     a list of endActions (actions that have no targetActions) that have been found while traversing the dialogue
     * @param visitedActions a list of actions the algorithm has already visited
     */
    private void addEndActionsByDepthFirstSearch(final Action currentAction, List<Action> endActions, List<Action> visitedActions) {
        visitedActions.add(currentAction);
        if (currentAction.getTargetActions().isEmpty()) {
            endActions.add(currentAction);
        }
        DialogueNavigator navigator = new DialogueNavigator(dialogueData.getNpcAttributes(), dialogueData.getStartAction());
        navigator.modifyNpcAttributes(currentAction);
        List<Action> availableActions = navigator.getAvailableTargetActions(currentAction.getTargetActions());
        for (Action availableTargetAction : availableActions) {
            if (!visitedActions.contains(availableTargetAction)) {
                addEndActionsByDepthFirstSearch(availableTargetAction, endActions, visitedActions);
            }
        }
    }

    /**
     * Gets every possible path to to any endAction in a dialogue.
     *
     * @param startAction the action from which the search for paths should commence
     * @return a list of paths to all endActions that have been completely traversed.
     */
    private List<Path> findAllPathsToAllEndActionsFrom(Action startAction) {
        Path path = new Path();
        path.addWayPoint(startAction);

        NpcAttributes initialAttributes = dialogueData.getNpcAttributes().copy();
        List<Action> reachableEndActions = findEndActionsFrom(startAction);
        dialogueData.setNpcAttributes(initialAttributes);

        return addPathToPathList(path, reachableEndActions);
    }

    /**
     * Adds a path to a list of paths when an endAction is reached.
     * When no endAction is found it continues to travel through the graph and adds new waypoints to the path.
     * Each time the graph branches out, the former path is copied.
     *
     * @param path                a list of actions that have already been visited. At first call it only contains the startAction of the dialogue.
     * @param reachableEndActions a list of endActions that a player is able to reach while navigating through the dialogue.
     * @return a list of paths that have been completely traversed.
     */
    private List<Path> addPathToPathList(Path path, List<Action> reachableEndActions) {
        NpcAttributes initialAttributes = dialogueData.getNpcAttributes().copy();
        if (reachableEndActions.contains(path.getLastWayPoint())) {
            return Arrays.asList(path);
        } else {
            List<Path> pathList = new ArrayList<>();
            DialogueNavigator navigator = new DialogueNavigator(dialogueData.getNpcAttributes(), dialogueData.getStartAction());
            navigator.modifyNpcAttributes(path.getLastWayPoint());
            for (Action targetAction : navigator.getAvailableTargetActions(path.getLastWayPoint().getTargetActions())) {
                if (!path.getWayPoints().contains(targetAction)) {
                    Path newPath = path.copy();
                    newPath.addWayPoint(targetAction);
                    pathList.addAll(addPathToPathList(newPath, reachableEndActions));
                } else {
                    cycleFound = true;
                }
            }
            dialogueData.setNpcAttributes(initialAttributes);
            return pathList;
        }
    }

    /**
     * Only gets paths that lead to a certain endAction from a certain startAction.
     *
     * @param endAction   the endAction where any found path should lead to
     * @param startAction the action from which the search for paths should commence
     * @return a list of paths that lead to endAction
     */
    public List<Path> findAllPathsToSpecificAction(Action startAction, Action endAction) {
        List<Path> allPaths = findAllPathsToAllEndActionsFrom(startAction);
        List<Path> pathsToAction = new ArrayList<>();
        for (Path path : allPaths) {
            if (path.getLastWayPoint().equals(endAction)) {
                pathsToAction.add(path);
            }
        }
        return pathsToAction;
    }
}
