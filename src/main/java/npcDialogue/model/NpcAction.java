package npcDialogue.model;

/**
 * A statement, message or activity of the NPC directed towards the Player
 */
public class NpcAction extends Action {


    //TODO model conditions ?
    public NpcAction(ActorType targetActionActorType, String actionText) {
        super(ActorType.NPC, targetActionActorType, actionText);
    }
}
