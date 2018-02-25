package npcDialogue.model;

/**
 * A statement, message or activity of the player directed towards the NPC
 */
public class PlayerAction extends Action {
    public PlayerAction(ActorType targetActionActorType, String actionText) {
        super(ActorType.PLAYER, targetActionActorType, actionText);
    }
}
