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

    private NpcDialogueData dialogueData;

    private List<Action> visitedActions; //TODO: put in method as local variable

    private DialogueNavigator navigator;

    public DialogueValidator(NpcDialogueData dialogueData) {
        this.dialogueData = dialogueData;
        this.visitedActions = new ArrayList<>();
        this.navigator = new DialogueNavigator(dialogueData.getNpcAttributes(), dialogueData.getStartAction());
    }

    public boolean isValid() {
        if (findEndActions().isEmpty()) {
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
    public List<Action> findLeaves() {
        List<Action> leaves = new ArrayList<>();

        addAllLeavesByDepthFirstSearch(dialogueData.getStartAction(), leaves);

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
     *
     * @return a list of reachable end points of the dialogue.
     */
    public List<Action> findEndActions() {
        List<Action> endActions = new ArrayList<>();

        addEndActionsByDepthFirstSearch(dialogueData.getStartAction(), endActions);

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
     * Gets a list of paths that lead from a currentAction to any endAction.
     *
     * @return a list of paths the player can take to all endActions.
     */
    public List<Path> findPaths() {
        findEndActions();
        List<Path> paths = new ArrayList<>();
        for (Action endAction : findEndActions()) {
            Path path = new Path();
            path.addWayPoint(dialogueData.getStartAction());
            addAllPathsToEndActionToList(dialogueData.getStartAction(), endAction, path, paths);
        }
        return paths;
    }

    /**
     * Adds each patch that leads from the currentAction to an endAction to a list of paths.
     *
     * @return a list of paths the player can take to one endAction.
     */
    private void addAllPathsToEndActionToList(Action currentAction, Action endAction, Path path, List<Path> paths) {
        List<Action> visitedWayPoints = new ArrayList<>(path.getWayPoints());
        visitedWayPoints.add(currentAction);
        if (currentAction.equals(endAction)) {
            Path newPath = new Path();
            for (Action action : path.getWayPoints()) {
                newPath.addWayPoint(action);
            }
            paths.add(newPath);
        }
        for (Action targetAction : currentAction.getTargetActions()) {
            if (!visitedWayPoints.contains(targetAction)) {
                path.addWayPoint(targetAction);
                addAllPathsToEndActionToList(targetAction, endAction, path, paths);
                path.removeWayPoint(targetAction);
            }
        }
        visitedWayPoints.remove(currentAction);
    }

    /**
     * Gets every possible path to to any endAction in a dialogue.
     *
     * @return a list of paths to all endActions that have been completely traversed.
     */
    public List<Path> findAllPathsToAllEndActionsFrom() {
        List<Path> paths = new ArrayList<>();
        Path path = new Path();
        path.addWayPoint(dialogueData.getStartAction());
        return addPathToPathList(dialogueData.getStartAction(), path, paths);
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
    private List<Path> addPathToPathList(Action currentAction, Path path, List<Path> paths) {
        if (currentAction.isEndAction()) { //wenn currentAction eine endAction ist, dann ist der Pfad vollständig und kann der Liste hinzugefügt werden
            paths.add(path);
        } else {
            for (Action targetAction : currentAction.getTargetActions()) {
                if (!targetAction.hasBackEdgeIntoCycle()) { //wenn die targetAction kein Ausgangspunkt für eine Rückwärtskante ist
                    if (path.getWayPoints().contains(targetAction)) { //wenn der Pfad bereits die targetAction enthält
                        targetAction.setHasBackEdgeIntoCycle(true); //markiere die targetAction als Ausgangspunkt einer Rückwärtskante
                    }
                    Path newPath = path.copy(); //kopiere den bisherigen Pfad
                    newPath.addWayPoint(targetAction); //füge die targetAction dem Pfad hinzu
                    addPathToPathList(targetAction, newPath, paths); //arbeite mit dem neuen Pfad weiter
                }
            }
        }
        return paths;
    }

    /**
     * Only gets paths that lead to a certain action.
     *
     * @param action
     * @return
     */
    public List<Path> findAllPathsTo(Action action) {
        List<Path> allPaths = findAllPathsToAllEndActionsFrom();
        List<Path> pathsToAction = new ArrayList<>();
        for (Path path : allPaths) {
            if (path.getLastAction().equals(action)) {
                pathsToAction.add(path);
            }
        }
        return pathsToAction;
    }
}
