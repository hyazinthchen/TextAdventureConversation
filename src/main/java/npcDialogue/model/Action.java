package npcDialogue.model;


import com.queomedia.commons.checks.Check;
import com.queomedia.commons.equals.EqualsChecker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Describes something an actor in the conversation can do or say. Each action has following actions or no following actions.
 */
public abstract class Action { //TODO: add List of npcTraits whose value will change when action is currentAction in the dialogueNavigator
    private final ArrayList<Action> targetActions;
    private final Map<String, Object> actionConditions; // example: action "buyStuff" can only be used when npcTrait "reputation" = 60
    private final Role role; // not using generics because content is read from text file
    private final Role targetActionsRole; // all targetActionActors must be of same type
    private final String actionText;
    private final String name;

    public Action(Role role, Role targetActionRole, String actionText, String name) {
        this.actionText = actionText;
        this.targetActions = new ArrayList<>();
        this.actionConditions = new HashMap<>();
        this.role = role;
        this.targetActionsRole = targetActionRole;
        this.name = name;
    }

    public Map<String, Object> getActionConditions() {
        return actionConditions;
    }

    public Role getTargetActionsRole() {
        return targetActionsRole;
    }

    public Role getRole() {
        return role;
    }

    public String getActionText() {
        return actionText;
    }

    /**
     * Adds a targetAction to the actions list of targetActions if the targetActions Role and the targetActionsRole of the action match.
     *
     * @param targetAction
     * @throws IllegalArgumentException
     */
    public void addTargetAction(Action targetAction) throws IllegalArgumentException {
        if (targetAction.role == targetActionsRole) {
            targetActions.add(targetAction);
        } else {
            throw new IllegalArgumentException("Actortype of new action does not match. Can't build dialogue. Got: " + targetAction.role + ". Expected: " + this.targetActionsRole);
        }
    }

    /**
     * Adds a dependency to an action. For example: action "buyStuff" can only be used when npcTrait "reputation" = 60.
     *
     * @param key
     * @param value
     */
    public void addActionDependency(String key, Object value) {
        actionConditions.put(key, value);
    }

    public ArrayList<Action> getTargetActions() {
        return targetActions;
    }

    //TODO: replace with getTargetActionByName
    public Action getTargetActionAt(int index) {
        Check.argumentBetween(index, 0, targetActions.size() - 1, "index");

        return targetActions.get(index);
    }

    //TODO: implement
    public Action getTargetActionByName(String actionName) {
        return null;
    }

    @Override
    public String toString() {

        final String actionTextList;
        if (targetActions != null) {
            actionTextList = targetActions.stream()
                    .map(Action::getActionText)
                    .collect(Collectors.joining(", "));
        } else {
            actionTextList = "null";
        }

        return "Action{" +
                "role=" + role +
                ", actionText='" + actionText + "'" +
                ", targetActionsRole=" + targetActionsRole +
                ", targetActions=" + actionTextList +
                '}';
    }

    //TODO: add javadoc
    public static final EqualsChecker<String, Action> ACTION_BY_TEXT_EQUALS_CHECKER =
            (String actionText, Action action) -> action.getActionText().equals(actionText);
}
