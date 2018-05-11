package npcDialogue.model;

/**
 * A statement, message or activity of the NPC directed towards the player.
 */
public class NpcAction extends Action {
    public NpcAction(final Role targetActionRole, final String actionText, final String name) {
        super(Role.NPC, targetActionRole, actionText, name);
    }
}
