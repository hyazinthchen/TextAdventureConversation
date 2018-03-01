package npcDialogue.model;

/**
 * A statement, message or activity of the player directed towards the NPC.
 */
public class PlayerAction extends Action {
    public PlayerAction(Role targetActionRole, String actionText) {
        super(Role.PLAYER, targetActionRole, actionText);
    }
}
