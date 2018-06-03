package npcDialogue_deprecated.model;

import com.queomedia.commons.exceptions.NotFoundRuntimeException;

import java.util.*;

public class Path {
    private final List<Action> wayPoints;

    private NpcAttributes npcAttributes;

    public Path(final List<Action> wayPoints) {
        this.wayPoints = new ArrayList<>(wayPoints);
    }

    public Path(final List<Action> wayPoints, final NpcAttributes npcAttributes) {
        this.wayPoints = new ArrayList<>(wayPoints);
        this.npcAttributes = new NpcAttributes(npcAttributes.getNpcAttributes());
    }

    public Path(Action... actions) {
        this(Arrays.asList(actions));
    }

    public Path() {
        this(Collections.emptyList());
    }

    public List<Action> getWayPoints() {
        return wayPoints;
    }

    public void setNpcAttributes(NpcAttributes npcAttributes) {
        this.npcAttributes = npcAttributes;
    }

    public NpcAttributes getNpcAttributes() {
        return npcAttributes;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Path)) {
            return false;
        }
        Path other = (Path) obj;
        return Objects.equals(wayPoints, other.wayPoints);
    }

    /**
     * Adds an Action as a wayPoint to the end of the Path.
     *
     * @param action the Action/wayPoint to be added
     */
    public void addWayPoint(Action action) {
        wayPoints.add(action);
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
        if (this.npcAttributes == null) {
            return new Path(this.wayPoints, new NpcAttributes());
        }
        return new Path(this.wayPoints, this.npcAttributes);
    }
}
