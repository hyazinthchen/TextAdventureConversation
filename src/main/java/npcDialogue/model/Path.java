package npcDialogue.model;

import com.queomedia.commons.exceptions.NotFoundRuntimeException;

import java.util.*;

public class Path {
    private List<Action> wayPoints;

    private NpcAttributes npcAttributes;

    public Path(List<Action> wayPoints) {
        this.wayPoints = new ArrayList<>(wayPoints);
    }

    public Path(List<Action> wayPoints, NpcAttributes npcAttributes) {
        this.wayPoints = new ArrayList<>(wayPoints);
        this.npcAttributes = new NpcAttributes(npcAttributes.getNpcAttributes());
    }

    public Path(Action... actions) {
        this(Arrays.asList(actions));
    }

    public Path() {
        this(Collections.emptyList());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Path)) {
            return false;
        }
        Path other = (Path) obj;
        return Objects.equals(wayPoints, other.wayPoints);
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
        if(this.npcAttributes == null){
            return new Path(this.wayPoints, new NpcAttributes());
        }
        return new Path(this.wayPoints, this.npcAttributes);
    }

    public void setNpcAttributes(NpcAttributes npcAttributes) {
        this.npcAttributes = npcAttributes;
    }

    public NpcAttributes getNpcAttributes() {
        return npcAttributes;
    }
}
