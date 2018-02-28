package npcDialogue.model;


import com.queomedia.commons.equals.EqualsChecker;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Describes something an actor in the conversation can do or say. Each action has following actions or no following actions.
 */
public abstract class Action { //TODO: add List of npcTraits whose value will change when action is currentAction in the dialogueNavigator
    private final ArrayList<Action> targetActions;
    private final ActorType actorType; // not using generics because content is read from text file
    private final ActorType targetActionsActorType; // all targetActionActors must be of same type
    private final String actionText;

    public Action(ActorType actorType, ActorType targetActionActorType, String actionText) {
        this.actionText = actionText;
        this.targetActions = new ArrayList<>();
        this.actorType = actorType;
        this.targetActionsActorType = targetActionActorType;
    }

    public ActorType getTargetActionsActorType() {
        return targetActionsActorType;
    }

    public ActorType getActorType() {
        return actorType;
    }

    public String getActionText() {
        return actionText;
    }

    /**
     * Adds a targetAction to the actions list of targetActions if the targetActions ActorType and the targetActionsActorType of the action match.
     *
     * @param targetAction
     * @throws InvalidStateException
     */
    public void addTargetAction(Action targetAction) throws InvalidStateException {
        if (targetAction.actorType == targetActionsActorType) {
            targetActions.add(targetAction);
        } else {
            throw new InvalidStateException("Actortype of new action does not match. Can't build dialogue. Got: " + targetAction.actorType + ". Expected: " + this.targetActionsActorType);
        }
    }

    public ArrayList<Action> getTargetActions() {
        return targetActions;
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
                "actorType=" + actorType +
                ", actionText='" + actionText + "'" +
                ", targetActionsActorType=" + targetActionsActorType +
                ", targetActions=" + actionTextList +
                '}';
    }

    public static final EqualsChecker<String, Action> ACTION_BY_TEXT_EQUALS_CHECKER =
            (String actionText, Action action) -> action.getActionText().equals(actionText);
}
