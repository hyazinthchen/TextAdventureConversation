package npcDialogue.model;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public interface TreeNode extends Iterable<TreeNode> {



    @Override
    default Iterator<TreeNode> iterator() {

        return new Iterator<TreeNode>() {
            Set<TreeNode> knownNodes = new HashSet<>();

            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public TreeNode next() {
                return getNextChild();
            }
        };
    }

    List<? extends TreeNode> getChildren();
    TreeNode getNextChild();
}
