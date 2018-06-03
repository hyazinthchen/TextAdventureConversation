package npcDialogue_deprecated.controller;

import npcDialogue_deprecated.model.Action;
import npcDialogue_deprecated.model.Modification;
import npcDialogue_deprecated.model.NpcDialogueData;
import npcDialogue_deprecated.model.Path;
import npcDialogue_deprecated.view.ConsoleReaderWriter;

import java.util.ArrayList;
import java.util.List;

public class DialogueValidator {

    private final NpcDialogueData dialogueData;

    public DialogueValidator(final NpcDialogueData dialogueData) {
        this.dialogueData = dialogueData;
    }

    public List<Path> findAllPathsThroughDialogue() {
        List<Path> pathsThroughDialogue = new ArrayList<>();
        Path path = new Path();
        path.setNpcAttributes(dialogueData.getNpcAttributes());
        path.addWayPoint(dialogueData.getStartAction());
        return getAllPathsThroughDialogueByDFS(pathsThroughDialogue, path);
    }

    private List<Path> getAllPathsThroughDialogueByDFS(List<Path> pathsThroughDialogue, Path path) {
        modifyNpcAttributes(path);
        if (path.getLastWayPoint().getTargetActions().isEmpty()) {
            pathsThroughDialogue.add(path);
        } else {
            for (Action availableTargetWayPoint : getAvailableTargetWayPoints(path)) {
                if (path.getWayPoints().size() < 500) {
                    Path newPath = path.copy();
                    newPath.addWayPoint(availableTargetWayPoint);
                    getAllPathsThroughDialogueByDFS(pathsThroughDialogue, newPath);
                } else {
                    new ConsoleReaderWriter().printErrorMessage("Dialogue is invalid because it contains a path longer than 500 waypoints.");
                }
            }
        }
        return pathsThroughDialogue;
    }

    private List<Action> getAvailableTargetWayPoints(Path path) {
        List<Action> availableTargetWayPoints = new ArrayList<>();
        for (Action targetWayPoint : path.getLastWayPoint().getTargetActions()) {
            if (path.getNpcAttributes().match(targetWayPoint.getConditions())) {
                availableTargetWayPoints.add(targetWayPoint);
            }
        }
        return availableTargetWayPoints;
    }

    private void modifyNpcAttributes(Path path) {
        for (Modification modification : path.getLastWayPoint().getNpcAttributeModifications()) {
            path.getNpcAttributes().modifyAttribute(modification.getNpcAttribute(), modification.getOperator(), modification.getValue());
        }
    }
}
