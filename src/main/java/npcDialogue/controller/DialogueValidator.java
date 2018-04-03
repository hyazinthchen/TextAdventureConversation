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
        if (screenForEndActions().isEmpty()) {
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
    public List<Action> screenForLeaves() {
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
    public List<Action> screenForEndActions() {
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
     * Gets a list of paths that lead from an action to an endAction of the dialogue.
     *
     * @return a list of paths the player can take to endActions.
     */
    public List<Path> screenForPaths() {
        screenForEndActions();
        List<Path> paths = new ArrayList<>();
        for (Action endAction : screenForEndActions()) {
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
     * @return
     */
    public List<Path> findAllPathsToAllEndActionsFrom(Action currentAction) {
        List<Path> paths = new ArrayList<>();
        Path path = new Path();
        path.addWayPoint(currentAction);
        return addPathToPathList(currentAction, path, paths);
    }

    private List<Path> addPathToPathList(Action currentAction, Path path, List<Path> paths) { //TODO: look for cycles
        if (screenForEndActions().contains(currentAction)) {
            paths.add(path);
        }else{
            for (Action targetAction : currentAction.getTargetActions()){
                path.addWayPoint(targetAction);
                addPathToPathList(targetAction, path.copy(), paths);
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
    public List<Path> findAllPathsToSpecificEndActionFrom(Action endAction) {
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
