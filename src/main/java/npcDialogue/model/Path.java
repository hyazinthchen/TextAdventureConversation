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

    public Action getLastAction() {
        if (wayPoints.isEmpty()) {
            throw new NotFoundRuntimeException("Path is empty, therefore there is no last element");
        }
        return wayPoints.get(wayPoints.size() - 1);

        //return wayPoints.stream().reduce(null, (a,b)->b);
    }

    /**
     * Creates a new path object which is a copy of the first path object and thus, has the same waypoints.
     *
     * @return
     */
    public Path copy() {
        return new Path(this.getWayPoints());
    }
}
