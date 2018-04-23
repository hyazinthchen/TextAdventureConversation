package npcDialogue.model;

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
}
