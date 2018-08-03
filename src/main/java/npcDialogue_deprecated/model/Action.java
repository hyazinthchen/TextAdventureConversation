package npcDialogue_deprecated.model;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Describes an act of the player or an NPC in a conversation where something is done or said. An action can have following actions or none, marking it as the end of the dialogue.
 */
public abstract class Action implements TreeNode {
    private final Role role;
    private final Role targetActionsRole;
    private final String actionText;
    private final String id;
    private final List<Action> targetActions;
    private final List<Condition> conditions;
    private final List<Modification> npcAttributeModifications;

    public Action(Role role, Role targetActionRole, String actionText, String id) {
        this.role = role;
        this.targetActionsRole = targetActionRole;
        this.actionText = actionText;
        this.id = id;
        this.targetActions = new ArrayList<>();
        this.conditions = new ArrayList<>();
        this.npcAttributeModifications = new ArrayList<>();
    }

    public Role getRole() {
        return role;
    }

    public Role getTargetActionsRole() {
        return targetActionsRole;
    }

    public String getActionText() {
        return actionText;
    }

    public List<Action> getTargetActions() {
        return targetActions;
    }

    /*public List<? extends npcDialogue.model.TreeNode> getChildren() {
        return targetActions;
    }*/

    public List<Condition> getConditions() {
        return conditions;
    }

    public List<Modification> getNpcAttributeModifications() {
        return npcAttributeModifications;
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
     * Adds a condition to an action. For example: action "buyStuff" can only be used when npcAttribute "reputation" == 60.
     *
     * @param npcAttribute       the attribute which should fulfill the condition
     * @param relationalOperator the operator of the condition, can be: less, greater, less equal, greater equal, equal, not equal
     * @param value              the value the npcAttributes should have, according to the condition
     */
    public void addCondition(String npcAttribute, String relationalOperator, int value) {
        switch (relationalOperator) {
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
     *
     * @param npcAttribute the npcAttribute which should later be changed
     * @param operator     the operator which determines the change of the value, can be: "+" or "-"
     * @param value        the value by which the value of the npcAttribute will change
     */
    public void addNpcAttributeModification(String npcAttribute, String operator, int value) {
        switch (operator) {
            case "+":
                npcAttributeModifications.add(new Modification(npcAttribute, Operator.PLUS, value));
                break;
            case "-": {
                npcAttributeModifications.add(new Modification(npcAttribute, Operator.MINUS, value));
                break;
            }
        }
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
}
