package npcDialogue.model;

import java.util.Objects;

/**
 * Represents an edge in the dialogue graph between two Actions.
 */
public class Edge {
    private Action startAction;

    private Action destinationAction;

    public Edge(Action startAction, Action destinationAction) {
        this.startAction = startAction;
        this.destinationAction = destinationAction;
    }

    public Action getStartAction() {
        return startAction;
    }

    public Action getDestinationAction() {
        return destinationAction;
    }

    @Override
    public String toString() {
        return this.startAction.getActionName() + this.destinationAction.getActionName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return Objects.equals(startAction, edge.startAction) &&
                Objects.equals(destinationAction, edge.destinationAction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startAction, destinationAction);
    }
}
