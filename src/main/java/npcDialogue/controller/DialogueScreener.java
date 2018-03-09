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

    private List<Path> paths;

    private DialogueNavigator navigator;

    private ConsoleReaderWriter consoleReaderWriter;

    private List<Action> visitedWayPoints;

    public DialogueScreener(NpcDialogueData dialogueData) {
        this.dialogueData = dialogueData;
        this.visitedActions = new ArrayList<>();
        this.leaves = new ArrayList<>();
        this.endActions = new ArrayList<>();
        this.navigator = new DialogueNavigator(dialogueData.getNpcAttributes(), dialogueData.getStartAction());
        this.consoleReaderWriter = new ConsoleReaderWriter();
        this.paths = new ArrayList<>();
        this.visitedWayPoints = new ArrayList<>();
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
        for (Action endAction : endActions) {
            visitedWayPoints.clear();
            Path path = new Path();
            path.addWayPoint(dialogueData.getStartAction());
            getAllPathsToEndAction(dialogueData.getStartAction(), endAction, path.getWayPoints());
        }
        return paths;
    }

    private void getAllPathsToEndAction(Action currentAction, Action endAction, List<Action> localPath) { //TODO: doesn't work with cycles
        visitedWayPoints.add(currentAction);
        if (currentAction.equals(endAction)) {
            Path path = new Path();
            for (Action action : localPath) {
                path.addWayPoint(action);
            }
            paths.add(path);
        }
        for (Action targetAction : currentAction.getTargetActions()) {
            if (!visitedWayPoints.contains(targetAction)) {
                localPath.add(targetAction);
                getAllPathsToEndAction(targetAction, endAction, localPath);
                localPath.remove(targetAction);
            }
        }
        visitedWayPoints.remove(currentAction);
    }
}
