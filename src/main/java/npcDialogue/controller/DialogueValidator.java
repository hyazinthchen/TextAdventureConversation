package npcDialogue.controller;

import npcDialogue.model.TreeNode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DialogueValidator {

    private TreeNode root;
    private Set<TreeNode> done = new HashSet<>();
    private Set<TreeNode> active = new HashSet<>();
    private Set<TreeNode> active_temp = new HashSet<>();

    public DialogueValidator(TreeNode root) {
        this.root = root;
    }

    public Set getDone() {
        return done;
    }

    public List<TreeNode> findBackEdges() {
        active.add(root);
        List<TreeNode> backEdgeList = new ArrayList<>();
        do {
            for (TreeNode activeNode : active) {
                done.add(activeNode);
                active_temp.addAll(activeNode.getChildren());
            }
            active_temp.removeAll(active);
            for (TreeNode active_tempNode : active_temp) {
                if (done.contains(active_tempNode)) {
                    backEdgeList.add(active_tempNode);
                    active_temp.remove(active_tempNode);
                }
            }
            active = active_temp;
            active_temp = new HashSet<>();
        } while (!active.isEmpty());
        return backEdgeList;
    }

    private void findLoops(){

    }

    private void findWayOutOfLoop(){

    }
}
