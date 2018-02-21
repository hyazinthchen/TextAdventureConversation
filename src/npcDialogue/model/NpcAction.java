package npcDialogue.model;
/**
 * A statement, message or activity of the NPC directed towards the Player
 */
public class NpcAction extends Action {
    public NpcAction(ActorType targetActionActorType) {
        super(ActorType.NPC, targetActionActorType);
    }
}
