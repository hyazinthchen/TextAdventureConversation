package npcDialogue_deprecated.model;

import npcDialogue.model.TreeNode;

import java.util.List;

/**
 * A statement, message or activity of the player directed towards the NPC.
 */
public class PlayerAction extends Action {
    public PlayerAction(final Role targetActionRole, final String actionText, final String name) {
        super(Role.PLAYER, targetActionRole, actionText, name);
    }

    @Override
    public List<? extends TreeNode> getChildren() {
        return null;
    }
}
