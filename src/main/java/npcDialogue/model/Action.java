package npcDialogue.model;

import com.queomedia.commons.equals.EqualsChecker;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Describes something in a conversation that can be done or said. Each action has following actions or no following actions.
 */
public abstract class Action {
    private final ArrayList<Action> targetActions;
    private final List<Condition> conditions;
    private final Role role;
    private final Role targetActionsRole; // all targetActionRoles must be of same type
    private final String actionText;
    private final String id;
    private final List<Modification> npcAttributeModifications;

    public Action(Role role, Role targetActionRole, String actionText, String id) {
        this.actionText = actionText;
        this.targetActions = new ArrayList<>();
        this.conditions = new ArrayList<>();
        this.role = role;
        this.targetActionsRole = targetActionRole;
        this.id = id;
        this.npcAttributeModifications = new ArrayList<>();
    }

    public List<Condition> getConditions() {
        return conditions;
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
     * Adds a targetAction to the actions list of targetActions if the role of the action you wish to add the targetActionsRole of the action fulfills.
     *
     * @param targetAction the action to be added as a targetAction.
     * @throws IllegalArgumentException when the role of the action to be added and the targetActionsRole of the action don't fulfills.
     */
    public void addTargetAction(Action targetAction) throws IllegalArgumentException {
        if (targetAction.role == targetActionsRole) {
            targetActions.add(targetAction);
        } else {
            throw new IllegalArgumentException("Role of new action does not fulfills. Can't build dialogue. Got: " + targetAction.role + ". Expected: " + this.targetActionsRole);
        }
    }

    /**
     * Adds a condition to an action. For example: action "buyStuff" can only be used when npcAttribute "reputation" = 60.
     */
    public void addCondition(String npcAttribute, String relationalOperator, int value) {
        switch(relationalOperator){
            case "<":
                conditions.add(new Condition(npcAttribute, RelationalOperator.LESS, value));
                break;
            case ">":
                conditions.add(new Condition(npcAttribute, RelationalOperator.GREATER, value));
                break;
            case "<=":
                conditions.add(new Condition(npcAttribute, RelationalOperator.LESSEQUAL, value));
                break;
            case ">=":
                conditions.add(new Condition(npcAttribute, RelationalOperator.GREATEREQUAL, value));
                break;
            case "==":
                conditions.add(new Condition(npcAttribute, RelationalOperator.EQUAL, value));
                break;
            case "!=":
                conditions.add(new Condition(npcAttribute, RelationalOperator.NOTEQUAL, value));
                break;
        }
    }

    /**
     * Adds a modification to an action. The modification will change the npcAttributes once the action is currentAction.
     */
    public void addNpcAttributeModification(String npcAttribute, String operator, int value) {
        switch (operator){
            case "+":
                npcAttributeModifications.add(new Modification(npcAttribute, Operator.PLUS, value));
                break;
            case "-":{
                npcAttributeModifications.add(new Modification(npcAttribute, Operator.MINUS, value));
                break;
            }
        }
    }

    public List<Action> getTargetActions() {
        return targetActions;
    }

    /**
     * Gets a targetAction by its id from the List of targetActions.
     *
     * @param id the id of the action that is looked for.
     * @return the targetAction with the specific id.
     */
    public Action getTargetActionById(String id) {
        for (Action targetAction : targetActions) {
            if (targetAction.id.equals(id)) {
                return targetAction;
            }
        }
        throw new NoSuchElementException("No targetAction with id '" + id + "' found in the targetActions of action '" + this.id + "'.");
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

    public List<Modification> getNpcAttributeModifications() {
        return npcAttributeModifications;
    }

    public static final EqualsChecker<String, Action> ACTION_BY_TEXT_EQUALS_CHECKER =
            (String actionText, Action action) -> action.getActionText().equals(actionText);
}
