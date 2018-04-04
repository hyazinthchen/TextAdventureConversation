package npcDialogue.model;


import com.queomedia.commons.equals.EqualsChecker;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Describes something in a conversation that can be done or said. Each action has following actions or no following actions.
 */
public abstract class Action {
    private final ArrayList<Action> targetActions;
    private final Map<String, Object> actionConditions; // example: action "buyStuff" can only be used when npcAttribute "reputation" = 60
    private final Role role;
    private final Role targetActionsRole; // all targetActionRoles must be of same type
    private final String actionText;
    private final String name;
    private final Map<String, Object> npcAttributeModifications;
    private boolean hasBackEdgeIntoCycle;

    public Action(Role role, Role targetActionRole, String actionText, String name) {
        this.actionText = actionText;
        this.targetActions = new ArrayList<>();
        this.actionConditions = new HashMap<>();
        this.role = role;
        this.targetActionsRole = targetActionRole;
        this.name = name;
        this.npcAttributeModifications = new HashMap<>();
        this.hasBackEdgeIntoCycle = false;
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

    public boolean hasBackEdgeIntoCycle() {
        return hasBackEdgeIntoCycle;
    }

    public void setHasBackEdgeIntoCycle(boolean value) {
        this.hasBackEdgeIntoCycle = value;
    }

    /**
     * Adds a targetAction to the actions list of targetActions if the role of the action you wish to add the targetActionsRole of the action match.
     *
     * @param targetAction the action to be added as a targetAction.
     * @throws IllegalArgumentException when the role of the action to be added and the targetActionsRole of the action don't match.
     */
    public void addTargetAction(Action targetAction) throws IllegalArgumentException {
        if (targetAction.role == targetActionsRole) {
            targetActions.add(targetAction);
        } else {
            throw new IllegalArgumentException("Role of new action does not match. Can't build dialogue. Got: " + targetAction.role + ". Expected: " + this.targetActionsRole);
        }
    }

    /**
     * Adds a condition to an action. For example: action "buyStuff" can only be used when npcAttribute "reputation" = 60.
     *
     * @param key   the key of an npcAttribute
     * @param value the value the npcAttribute should have
     */
    public void addActionCondition(String key, Object value) {
        actionConditions.put(key, value);
    }

    /**
     * Adds a modification to an action. The modification will change the npcAttributes once the action is currentAction.
     *
     * @param key   the key of an npcAttribute
     * @param value the value the npcAttribute will have
     */
    public void addNpcAttributeModification(String key, Object value) {
        npcAttributeModifications.put(key, value);
    }

    public List<Action> getTargetActions() {
        return targetActions;
    }

    /**
     * Gets a targetAction by its name from the List of targetActions.
     *
     * @param actionName the name of the action that is looked for.
     * @return the targetAction with the specific name.
     */
    public Action getTargetActionByName(String actionName) {
        for (Action targetAction : targetActions) {
            if (targetAction.name.equals(actionName)) {
                return targetAction;
            }
        }
        throw new NoSuchElementException("No targetAction with name '" + actionName + "' found in the targetActions of action '" + name + "'.");
    }

    @Override
    public String toString() {

        final String actionTextList;
        actionTextList = targetActions.stream()
                .map(Action::getActionText)
                .collect(Collectors.joining(", "));

        return "Action{" +
                "role=" + role +
                ", actionText='" + actionText + "'" +
                ", targetActionsRole=" + targetActionsRole +
                ", targetActions=" + actionTextList +
                '}';
    }

    public static final EqualsChecker<String, Action> ACTION_BY_TEXT_EQUALS_CHECKER =
            (String actionText, Action action) -> action.getActionText().equals(actionText);

    public Map<String, Object> getNpcAttributeModifications() {
        return npcAttributeModifications;
    }

    public boolean isEndAction() {
        return getTargetActions().isEmpty();
    }
}
