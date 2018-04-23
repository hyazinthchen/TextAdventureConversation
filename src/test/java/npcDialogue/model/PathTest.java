package npcDialogue.model;

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
        Assert.assertEquals(asList(new Edge(actionA, actionB), new Edge(actionB, actionC), new Edge(actionC, actionD)).toString(), path.getEdges().toString());
    }

    /**
     * A
     */
    @Test
    public void testGetEdgesOneElement() {
        Action actionA = generateTestAction("A");

        Path path = new Path(actionA);

        Assert.assertTrue(path.getEdges().isEmpty());
    }

    @Test
    public void testGetEdgesNoElement() {
        Path path = new Path();
        Assert.assertTrue(path.getEdges().isEmpty());
    }
}