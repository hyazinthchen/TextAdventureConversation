package npcDialogue.model;

import java.util.List;

/**
 * Represents a cycle in a dialogue consisting of TreeNodes.
 */
public class Cycle {
    private List<TreeNode> nodes;

    public List<TreeNode> getNodes() {
        return nodes;
    }

    public void addNode(TreeNode node) {
        nodes.add(node);
    }
}
