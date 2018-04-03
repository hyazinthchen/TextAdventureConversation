package npcDialogue.model;

import java.util.ArrayList;
import java.util.List;

public class Path {
    private List<Action> wayPoints;

    public Path() {
        this.wayPoints = new ArrayList<>();
    }

    public void addWayPoint(Action action) {
        wayPoints.add(action);
    }

    public List<Action> getWayPoints() {
        return wayPoints;
    }

    public void removeWayPoint(Action action) {
        wayPoints.remove(action);
    }

    public Action getLastAction(){
        return wayPoints.get(wayPoints.size()-1); //TODO use or throw away
    }

    /**
     * Creates a new path object which is a copy of the first path object and thus, has the same waypoints.
     *
     * @return
     */
    public Path copy() {
        Path copiedPath = new Path();
        for (Action wayPoint : getWayPoints()) {
            copiedPath.addWayPoint(wayPoint);
        }
        return copiedPath;
    }
}
