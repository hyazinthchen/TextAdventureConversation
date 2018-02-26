package npcDialogue.model;


import java.util.ArrayList;
import java.util.List;

public abstract class Action {
    private final List<Action> targetActions;
    private final ActorType actorType; // not using generics because content is read from text file
    private final ActorType targetActionsActorType; // all targetActionActors must be of same type
    private final String ActionText;

    public Action(ActorType actorType, ActorType targetActionActorType, String actionText) {
        this.ActionText = actionText;
        this.targetActions = new ArrayList<>();
        this.actorType = actorType;
        this.targetActionsActorType = targetActionActorType;
    }

    public ActorType getActorType() {
        return actorType;
    }

    public String getActionText() {
        return ActionText;
    }

    //TODO: only for test purposes, remove when wrapping up project
    public String getTargetActionsAsString() {
        String targetActionList = "";
        int i = 0;
        for (Action action : targetActions) {
            targetActionList = targetActionList + "[" + i + "]" + action.ActionText;
            i++;
        }
        return targetActionList;
    }

    public void addTargetAction(Action targetAction) throws InvalidStateException {
        if (targetAction.actorType == targetActionsActorType) {
            targetActions.add(targetAction);
        } else {
            throw new InvalidStateException("Actortype of new action does not match. Can't build dialogue. Got: " + targetAction.actorType + ". Expected: " + this.targetActionsActorType);
        }
    }
}
