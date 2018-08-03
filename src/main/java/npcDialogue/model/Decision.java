package npcDialogue.model;

import java.util.ArrayList;
import java.util.List;

public class Decision implements TreeNode {
    private final TreeNode thenAction;
    private final TreeNode elseAction;
    private final List<Condition> conditions;

    public Decision(final TreeNode thenAction, final TreeNode elseAction, final List<Condition> conditions) {
        this.thenAction = thenAction;
        this.elseAction = elseAction;
        this.conditions = conditions;
    }

    public TreeNode decide(NpcAttributes npcAttributes) {
        if (npcAttributes.match(conditions)) {
            return thenAction;
        }
        return elseAction;
    }

    @Override
    public List<? extends TreeNode> getChildren() {
        return new ArrayList<Decision>();
    }
}
