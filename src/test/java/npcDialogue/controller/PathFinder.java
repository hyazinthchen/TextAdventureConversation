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
                if (!pathContainsSuccessiveSequence1(path)) {
                    Path newPath = path.copy();
                    newPath.addWayPoint(availableTargetWayPoint);
                    getAllPathsThroughDialogueByDFS(pathsThroughDialogue, newPath);
                }
            }
        }
        return pathsThroughDialogue;
    }

    public boolean pathContainsSuccessiveSequence1(Path path) {
        Action firstElement = path.getWayPoints().get(0);
        Action secondElement = path.getWayPoints().get(1);
        Action thirdElement = path.getWayPoints().get(2);
        Action fourthElement = path.getWayPoints().get(3);
        Action fifthElement = path.getWayPoints().get(4);
        Action sixthElement = path.getWayPoints().get(5);
        if (firstElement.equals(secondElement)) {
            return true;
        }
        if (firstElement.equals(thirdElement) && secondElement.equals(fourthElement)) {
            return true;
        }
        if (firstElement.equals(fourthElement) && secondElement.equals(fifthElement) && thirdElement.equals(sixthElement)) {
            return true;
        }
        if (secondElement.equals(thirdElement)) {
            return true;
        }
        if (secondElement.equals(fourthElement) && thirdElement.equals(fifthElement)) {
            return true;
        }
        if (thirdElement.equals(fourthElement)) {
            return true;
        }
        if (thirdElement.equals(fifthElement) && fourthElement.equals(sixthElement)) {
            return true;
        }
        if (fourthElement.equals(fifthElement)) {
            return true;
        }
        if (fifthElement.equals(sixthElement)) {
            return true;
        }
        return false;
    }

    public List<Action> getSequenceToSearchFor(Path path) {
        List<Action> sequence = new ArrayList<>();
        int x = path.getWayPoints().size() / 3;
        for (int i = 0; i <= x - 1; i++) {
            sequence.add(path.getWayPoints().get(i));
        }
        return sequence;
    }

    private List<Action> getAvailableTargetWayPoints(Path path) {
        List<Action> availableTargetWayPoints = new ArrayList<>();
        for (Action targetWaypoint : path.getLastWayPoint().getTargetActions()) {
            if (path.getNpcAttributes().fulfill(targetWaypoint.getActionConditions())) {
                availableTargetWayPoints.add(targetWaypoint);
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
