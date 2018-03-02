package npcDialogue.model;

/**
 * A statement, message or activity of the NPC directed towards the player.
 */
public class NpcAction extends Action {
    public NpcAction(Role targetActionRole, String actionText, String name) {
        super(Role.NPC, targetActionRole, actionText, name);
    }
}
