package npcDialogue.model;

/**
 * A statement, message or activity of the NPC directed towards the player.
 */
public class NpcAction extends Action {


    //TODO model conditions ?
    public NpcAction(Role targetActionRole, String actionText) {
        super(Role.NPC, targetActionRole, actionText);
    }
}
