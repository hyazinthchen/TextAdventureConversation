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
}
