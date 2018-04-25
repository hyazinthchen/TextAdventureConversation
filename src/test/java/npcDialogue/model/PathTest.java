package npcDialogue.model;

import com.queomedia.commons.asserts.AssertUtil;
import org.junit.Assert;
import org.junit.Test;

import static java.util.Arrays.asList;


public class PathTest {

    /**
     * /**
     * Helper method for generating a simple action without any conditions for test purposes.
     *
     * @param actionText the actionText of the new action.
     * @return a new action object.
     */
    private Action generateTestAction(String actionText) {
        return new NpcAction(Role.NPC, actionText, actionText);
    }

    /**
     * A[B], B[C], C[D]
     */
    @Test
    public void testGetEdgesManyElements() {
        Action actionA = generateTestAction("A");
        Action actionB = generateTestAction("B");
        Action actionC = generateTestAction("C");
        Action actionD = generateTestAction("D");

        Path path = new Path(actionA, actionB, actionC, actionD);

        Assert.assertTrue(path.getEdges().size() == 3);
        AssertUtil.sameOrder(asList(new Edge(actionA, actionB), new Edge(actionB, actionC), new Edge(actionC, actionD)), path.getEdges());
    }

    /**
     * A
     */
    @Test
    public void testGetEdgesOneElement() {
        Action actionA = generateTestAction("A");

        Path path = new Path(actionA);

        AssertUtil.isEmpty(path.getEdges());
    }

    @Test
    public void testGetEdgesNoElement() {
        Path path = new Path();
        AssertUtil.isEmpty(path.getEdges());
    }


    @Test
    public void testGetEdgeTraversalCount() {
        Action actionA = generateTestAction("A");
        Action actionB = generateTestAction("B");
        Action actionC = generateTestAction("C");
        Action actionD = generateTestAction("D");

        Path path = new Path(actionA, actionB, actionC, actionA, actionB, actionD);

        Assert.assertEquals(2, path.getEdgeCount(new Edge(actionA, actionB)));
    }
}