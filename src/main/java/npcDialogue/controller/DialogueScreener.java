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
public class DialogueScreener {

    private NpcDialogueData dialogueData;

    private List<Action> visitedActions;

    private List<Action> leaves;

    private List<Action> endActions;

    private DialogueNavigator navigator;

    private ConsoleReaderWriter consoleReaderWriter;

    public DialogueScreener(NpcDialogueData dialogueData) {
        this.dialogueData = dialogueData;
        this.visitedActions = new ArrayList<>();
        this.leaves = new ArrayList<>();
        this.endActions = new ArrayList<>();
        this.navigator = new DialogueNavigator(dialogueData.getNpcAttributes(), dialogueData.getStartAction());
        this.consoleReaderWriter = new ConsoleReaderWriter();
    }

    public boolean screenIsClean() {
        if (screenForEndActions().isEmpty()) {
            consoleReaderWriter.printErrorMessage("Error in loaded dialogue. Dialogue will never reach a desired end.");
            return false;
        }
        return true;
    }

    /**
     * Gets a list of actions that do not have available targetActions.
     *
     * @return a list of actions where the dialogue stops.
     */
    public List<Action> screenForLeaves() {

        addAllLeavesByDepthFirstSearch(dialogueData.getStartAction());

        return leaves;
    }

    /**
     * Performs a recursive depth first search and adds actions that do not have available targetActions to a list of actions. (leaves of the tree)
     *
     * @param currentAction
     */
    private void addAllLeavesByDepthFirstSearch(final Action currentAction) {
        visitedActions.add(currentAction);
        List<Action> availableActions = navigator.getAvailableTargetActions(currentAction.getTargetActions());
        if (availableActions.isEmpty() || currentAction.getTargetActions().isEmpty()) {
            leaves.add(currentAction);
        }
        for (Action targetAction : availableActions) {
            if (!visitedActions.contains(targetAction)) {
                addAllLeavesByDepthFirstSearch(targetAction);
            }
        }
    }

    /**
     * Gets a list of reachable actions that do not have targetActions.
     *
     * @return a list of reachable end points of the dialogue.
     */
    public List<Action> screenForEndActions() {

        addEndActionsByDepthFirstSearch(dialogueData.getStartAction());

        return endActions;
    }

    /**
     * Performs a recursive depth first search and adds actions that can be reached and do not have targetActions to a list of actions.
     *
     * @param currentAction
     */
    private void addEndActionsByDepthFirstSearch(final Action currentAction) {
        visitedActions.add(currentAction);
        if (currentAction.getTargetActions().isEmpty()) {
            endActions.add(currentAction);
        }
        List<Action> availableActions = navigator.getAvailableTargetActions(currentAction.getTargetActions());
        for (Action availableTargetAction : availableActions) {
            if (!visitedActions.contains(availableTargetAction)) {
                addEndActionsByDepthFirstSearch(availableTargetAction);
            }
        }
    }

    /**
     * Gets a list of paths that lead from an action to an endAction of the dialogue.
     *
     * @return a list of paths the player can take to endActions.
     */
    public List<Path> screenForPaths() {
        screenForEndActions();
        List<Path> paths = new ArrayList<>();
        for (Action endAction : endActions) {
            Path path = new Path();
            path.addWayPoint(dialogueData.getStartAction());
            getAllPathsToEndAction(dialogueData.getStartAction(), endAction, path, paths);
        }
        return paths;
    }

    private void getAllPathsToEndAction(Action currentAction, Action endAction, Path path, List<Path> paths) {
        List<Action> visitedWayPoints = new ArrayList<>(path.getWayPoints());
        visitedWayPoints.add(currentAction); //A is now twice in visitedWayPoints!
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
                getAllPathsToEndAction(targetAction, endAction, path, paths);
                path.removeWayPoint(targetAction);
            }
        }
        visitedWayPoints.remove(currentAction);
    }

    /**
     * Gets every possible path to to any endAction in a dialogue.
     *
     * @param currentAction
     * @param path
     * @param paths
     * @return
     */
    public List<Path> getAllPathsToAllEndActions(Action currentAction, Path path, List<Path> paths) {
        path.addWayPoint(currentAction);
        if (currentAction.getTargetActions().isEmpty()) {
            paths.add(path);
        }
        List<Action> leaves = screenForLeaves();
        if (!containsVisitedLeaf(path.getWayPoints(), leaves)) {
            for (Action targetAction : currentAction.getTargetActions()) {
                Path newPath = path.copy();
                getAllPathsToAllEndActions(targetAction, newPath, paths);
            }
        }
        return paths;
    }

    /**
     * Only gets paths that lead to a certain endAction.
     *
     * @param endAction
     * @return
     */
    public List<Path> getAllPathsToSpecificEndAction(Action endAction) {
        //List<Path> allPaths= getAllPathsToAllEndActions();
        List<Path> pathsToEndAction = new ArrayList<>();
        //get every path in allPaths that ends with endAction and add to pathsToEndAction
        return pathsToEndAction;
    }

    /**
     * Checks whether a leaf has already been visited.
     *
     * @param visitedWayPoints
     * @param leaves
     * @return
     */
    private boolean containsVisitedLeaf(List<Action> visitedWayPoints, List<Action> leaves) {
        for (Action visitedWayPoint : visitedWayPoints) {
            for (Action leaf : leaves) {
                if (visitedWayPoint.equals(leaf)) {
                    return true;
                }
            }
        }
        return false;
    }
}
