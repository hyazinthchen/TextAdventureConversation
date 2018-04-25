package npcDialogue.model;

import com.queomedia.commons.exceptions.NotFoundRuntimeException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Path {
    private List<Action> wayPoints;

    public Path(List<Action> wayPoints) {
        this.wayPoints = new ArrayList<>(wayPoints);
    }

    public Path(Action... actions) {
        this(Arrays.asList(actions));
    }

    public Path() {
        this(Collections.emptyList());
    }


    public void addWayPoint(Action action) {
        wayPoints.add(action);
    }

    public List<Action> getWayPoints() {
        return wayPoints;
    }

    /**
     * Gets the last waypoint in the current path.
     *
     * @return the last waypoint
     */
    public Action getLastWayPoint() {
        if (wayPoints.isEmpty()) {
            throw new NotFoundRuntimeException("Path is empty, therefore there is no last element");
        }
        return wayPoints.get(wayPoints.size() - 1);
    }

    /**
     * Creates a new path object which is a copy of the first path object and thus, has the same waypoints.
     *
     * @return the copied path.
     */
    public Path copy() {
        return new Path(this.getWayPoints());
    }

    public List<Edge> getEdges() {
        return getListOfEdges();
    }

    /**
     * Gets a list of edges in the current path.
     *
     * @return a list of edges.
     */
    private List<Edge> getListOfEdges() {
        List<Edge> edges = new ArrayList<>();
        for (int i = 0; i < this.wayPoints.size() - 1; i++) {
            Edge edge = new Edge(this.wayPoints.get(i), this.wayPoints.get(i + 1));
            edges.add(edge);
        }
        return edges;
    }

    /**
     * Gets the number of occurrences of an Edge in the current Path.
     *
     * @param startAction       the start of the edge
     * @param destinationAction the end of the edge
     * @return a number of occurrences of an Edge.
     */
    public int getEdgeCount(Action startAction, Action destinationAction) {
        int count = 0;
        for (Edge edge : getListOfEdges()) {
            if (edge.equals(new Edge(startAction, destinationAction))) {
                count++;
            }
        }
        return count;
    }

    /**
     * Gets the number of occurrences of an Edge in the current Path.
     *
     * @param edge the edge to count in the path
     * @return a number of occurrences of an Edge.
     */
    public int getEdgeCount(Edge edge) {
        return getEdgeCount(edge.getStartAction(), edge.getDestinationAction());
    }
}
