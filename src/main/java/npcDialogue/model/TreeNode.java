package npcDialogue.model;

import java.util.Iterator;
import java.util.List;

public interface TreeNode extends Iterable<TreeNode> {


    @Override
    default Iterator<TreeNode> iterator() {

        return new Iterator<TreeNode>() {
            @Override
            public boolean hasNext() {
                return hasNext();
            }

            @Override
            public TreeNode next() {
                return getNext();
            }
        };
    }

    List<? extends TreeNode> getChildren();

    TreeNode getNext();

    boolean hasNext();
}
