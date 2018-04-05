package npcDialogue.controller;

import npcDialogue.model.Action;
import npcDialogue.model.NpcDialogueData;
import npcDialogue.model.Path;
import npcDialogue.view.ConsoleReaderWriter;

import java.util.ArrayList;
import java.util.List;

/**
 * Checks for errors in a loaded dialogue. Errors can be: dead ends, where no available targetActions are available or cycles.
 */
public class DialogueValidator {

    private List<Action> visitedActions; //TODO: put in method as local variable

    private DialogueNavigator navigator;

    private NpcDialogueData dialogueData;

    public DialogueValidator(NpcDialogueData dialogueData) {
        this.dialogueData = dialogueData;
        this.visitedActions = new ArrayList<>();
        this.navigator = new DialogueNavigator(dialogueData.getNpcAttributes(), dialogueData.getStartAction());
    }

    public boolean isValid() {
        if (findEndActionsFrom(dialogueData.getStartAction()).isEmpty()) {
            new ConsoleReaderWriter().printErrorMessage("Error in loaded dialogue. Dialogue will never reach a desired end.");
            return false;
        }
        return true;
    }

    /**
     * Gets a list of actions that do not have available targetActions.
     *
     * @return a list of actions where the dialogue stops.
     */
    public List<Action> findLeavesFrom(Action startAction) {
        List<Action> leaves = new ArrayList<>();

        addAllLeavesByDepthFirstSearch(startAction, leaves);

        return leaves;
    }

    /**
     * Performs a recursive depth first search and adds actions that do not have available targetActions to a list of actions. (leaves of the tree)
     *
     * @param currentAction
     */
    private void addAllLeavesByDepthFirstSearch(final Action currentAction, List<Action> leaves) {
        visitedActions.add(currentAction);
        List<Action> availableActions = navigator.getAvailableTargetActions(currentAction.getTargetActions());
        if (availableActions.isEmpty() || currentAction.getTargetActions().isEmpty()) {
            leaves.add(currentAction);
        }
        for (Action targetAction : availableActions) {
            if (!visitedActions.contains(targetAction)) {
                addAllLeavesByDepthFirstSearch(targetAction, leaves);
            }
        }
    }

    /**
     * Gets a list of reachable actions that do not have targetActions.
     * Some endActions might not be reachable because an action ahead of it doesn't fulfill its conditions. Such unreachable actions won't be returned.
     *
     * @return a list of reachable end points of the dialogue.
     */
    public List<Action> findEndActionsFrom(Action startAction) {
        List<Action> endActions = new ArrayList<>();

        addEndActionsByDepthFirstSearch(startAction, endActions);

        return endActions;
    }

    /**
     * Performs a recursive depth first search and adds actions that can be reached and do not have targetActions to a list of actions.
     *
     * @param currentAction
     */
    private void addEndActionsByDepthFirstSearch(final Action currentAction, List<Action> endActions) {
        visitedActions.add(currentAction);
        if (currentAction.getTargetActions().isEmpty()) {
            endActions.add(currentAction);
        }
        List<Action> availableActions = navigator.getAvailableTargetActions(currentAction.getTargetActions());
        for (Action availableTargetAction : availableActions) {
            if (!visitedActions.contains(availableTargetAction)) {
                addEndActionsByDepthFirstSearch(availableTargetAction, endActions);
            }
        }
    }

    /**
     * Gets every possible path to to any endAction in a dialogue.
     *
     * @return a list of paths to all endActions that have been completely traversed.
     */
    private List<Path> findAllPathsToAllEndActionsFrom(Action startAction) {
        List<Path> paths = new ArrayList<>();
        Path path = new Path();
        path.addWayPoint(startAction);
        List<Action> wayPointsWithBackEdges = new ArrayList<>();
        List<Action> reachableEndActions = findEndActionsFrom(startAction);
        return addPathToPathList(startAction, path, paths, wayPointsWithBackEdges, reachableEndActions);
    }

    /**
     * Adds a path to a list of paths when an endAction is reached.
     * When no endAction is found it continues to travel through the graph and adds new waypoints to the path.
     * Each time the graph branches out, the former path is copied.
     *
     * @param currentAction
     * @param path          a list of actions that have already been visited
     * @param paths         a list of paths that have been completely traversed
     * @return a list of paths that have been completely traversed.
     */
    private List<Path> addPathToPathList(Action currentAction, Path path, List<Path> paths, List<Action> wayPointsWithBackEdges, List<Action> reachableEndActions) {
        if (reachableEndActions.contains(currentAction)) {
            paths.add(path);
            wayPointsWithBackEdges.clear();
        } else {
            for (Action targetAction : currentAction.getTargetActions()) {
                if (!wayPointsWithBackEdges.contains(targetAction)) { //wenn die targetAction kein Ausgangspunkt für eine Rückwärtskante ist
                    if (path.getWayPoints().contains(targetAction)) { //wenn der Pfad bereits die targetAction enthält
                        wayPointsWithBackEdges.add(currentAction); //markiere die currentAction als Ausgangspunkt einer Rückwärtskante
                    }
                    Path newPath = path.copy(); //kopiere den bisherigen Pfad
                    newPath.addWayPoint(targetAction); //füge die targetAction dem Pfad hinzu
                    addPathToPathList(targetAction, newPath, paths, wayPointsWithBackEdges, reachableEndActions); //arbeite mit dem neuen Pfad weiter
                }
            }
        }
        return paths;
    }

    /**
     * Only gets paths that lead to a certain endAction from a certain startAction.
     *
     * @param endAction
     * @return
     */
    public List<Path> findAllPathsTo(Action endAction, Action startAction) {
        List<Path> allPaths = findAllPathsToAllEndActionsFrom(startAction);
        List<Path> pathsToAction = new ArrayList<>();
        for (Path path : allPaths) {
            if (path.getLastAction().equals(endAction)) {
                pathsToAction.add(path);
            }
        }
        return pathsToAction;
    }
}
