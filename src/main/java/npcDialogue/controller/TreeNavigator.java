package npcDialogue.controller;

import npcDialogue.model.TreeNode;

import java.util.HashSet;
import java.util.Set;

public class TreeNavigator {

    private TreeNode root;
    private Set<TreeNode> done = new HashSet<>();
    private Set<TreeNode> active = new HashSet<>();
    private Set<TreeNode> active_temp = new HashSet<>();

    public TreeNavigator(TreeNode root) {
        this.root = root;
    }

    public Set getDone() {
        return done;
    }

    public void navigate() {
        active.add(root);
        while (root.hasNext()) { //TODO: infinite loop here
            for (TreeNode node : active) {
                done.add(node);
                Set<TreeNode> nodeChildren = new HashSet<>(node.getChildren());
                nodeChildren.removeAll(active);
                nodeChildren.removeAll(done);
                active_temp = nodeChildren;
            }
            active = active_temp;
            active_temp = new HashSet<>();
        }
    }
}
