package npcDialogue_deprecated.model;

import java.util.ArrayList;
import java.util.List;

public class Decision implements TreeNode {
    private final Action thenAction;
    private final Action elseAction;
    private final List<Condition> conditions;

    public Decision(final Action thenAction, final Action elseAction, final List<Condition> conditions) {
        this.thenAction = thenAction;
        this.elseAction = elseAction;
        this.conditions = conditions;
    }

    public Action decide(NpcAttributes npcAttributes) {
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
