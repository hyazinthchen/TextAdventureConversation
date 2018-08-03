package npcDialogue.model;

import java.util.List;

public interface TreeNode {
    List<? extends TreeNode> getChildren();

    void addTargetNode(TreeNode treeNode);

    void addCondition(String attribute, String operator, Integer value);

    void addNpcAttributeModification(String attribute, String operator, Integer value);
}
