package npcDialogue.controller;

import npcDialogue.model.Action;
import npcDialogue.model.NpcDialogueData;
import npcDialogue.model.Path;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PathFinder {

    private NpcDialogueData dialogueData;

    public PathFinder(NpcDialogueData dialogueData) {
        this.dialogueData = dialogueData;
    }

    public List<Path> findAllPathsThroughDialogue() {
        List<Path> pathsThroughDialogue = new ArrayList<>(); //new PathList
        Path path = new Path(); //new Path

        path.setNpcAttributes(dialogueData.getNpcAttributes()); //set npcAttributes of new Path

        path.addWayPoint(dialogueData.getStartAction()); //add first WayPoint to new Path

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
                }
            }
        }
        return pathsThroughDialogue;
    }


    private List<Action> getAvailableTargetWayPoints(Path path) {
        List<Action> availableTargetWayPoints = new ArrayList<>();
        for (Action targetWayPoint : path.getLastWayPoint().getTargetActions()) {
            if (path.getNpcAttributes().fulfill(targetWayPoint.getActionConditions())) {
                availableTargetWayPoints.add(targetWayPoint);
            }
        }
        return availableTargetWayPoints;
    }

    private void modifyNpcAttributes(Path path) {
        for (Map.Entry<String, Object> modification : path.getLastWayPoint().getNpcAttributeModifications().entrySet()) {
            path.getNpcAttributes().modifyAttribute(modification.getKey(), modification.getValue());
        }
    }
}
