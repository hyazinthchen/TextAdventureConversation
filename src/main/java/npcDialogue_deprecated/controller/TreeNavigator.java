package npcDialogue_deprecated.controller;

import npcDialogue_deprecated.model.TreeNode;

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
        do {
            for (TreeNode activeNode : active) {
                done.add(activeNode);
                //active_temp.addAll(activeNode.getChildren());
            }
            active_temp.removeAll(active);
            active_temp.removeAll(done);
            active = active_temp;
            active_temp = new HashSet<>();
        } while (!active.isEmpty());
    }
}
