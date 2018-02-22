package npcDialogue.model;


import java.util.ArrayList;
import java.util.List;

public abstract class Action {
    private final List<Action> targetActions;
    private final ActorType actorType; // not using generics because content is read from text file
    private final ActorType targetActionsActorType; // all targetActionActors must be of same type

    public Action(ActorType actorType, ActorType targetActionActorType) {
        this.targetActions = new ArrayList<>();
        this.actorType = actorType;
        this.targetActionsActorType = targetActionActorType;
    }

    public void addTargetAction(Action targetAction) throws InvalidStateException {
        if (targetAction.actorType == targetActionsActorType) {
            targetActions.add(targetAction);
        } else {
            throw new InvalidStateException("Actortype of new action does not match. Can't build dialogue. Got: " + targetAction.actorType + ". Expected: " + this.targetActionsActorType);
        }
    }
}
