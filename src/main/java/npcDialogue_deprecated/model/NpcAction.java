package npcDialogue_deprecated.model;

import npcDialogue.model.TreeNode;

import java.util.List;

/**
 * A statement, message or activity of the NPC directed towards the player.
 */
public class NpcAction extends Action {
    public NpcAction(final Role targetActionRole, final String actionText, final String name) {
        super(Role.NPC, targetActionRole, actionText, name);
    }

    @Override
    public List<? extends TreeNode> getChildren() {
        return null;
    }
}
