package npcDialogue.controller;

import com.queomedia.commons.checks.Check;
import npcDialogue.model.*;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Loads the dialogue between one NPC and the player from a yaml file.
 */
public class DialogueLoader {

    /**
     * Loads the whole data of the dialogue from a yaml file (actions and npcAttributes).
     *
     * @param file the yaml file
     * @return A new NpcDialogueData object.
     * @throws FileNotFoundException in case the path is incorrect
     */
    public NpcDialogueData load(File file) throws FileNotFoundException {
        InputStream inputStream = new FileInputStream(file);
        Yaml yaml = new Yaml();
        LinkedHashMap yamlDataMap = yaml.load(inputStream); //TODO: catch errors if snakeyamls load() does weird stuff.

        NpcAttributes npcAttributes = loadNpcAttributes(yamlDataMap);
        return loadNpcDialogue(yamlDataMap, npcAttributes);
    }

    /**
     * Loads the NPCs attributes from the yaml file.
     *
     * @param yamlContent the whole dialogue data read from a file
     * @return An NpcAttributes object.
     */
    private NpcAttributes loadNpcAttributes(LinkedHashMap yamlContent) {
        NpcAttributes newNpcAttributes = new NpcAttributes();
        LinkedHashMap<String, Object> rawNpcAttributes = (LinkedHashMap) yamlContent.get("npcAttributes"); //TODO: catch null & what if two conditions have the same key?
        for (Map.Entry<String, Object> entry : rawNpcAttributes.entrySet()) {
            newNpcAttributes.addDataEntry(entry.getKey(), entry.getValue());
        }
        return newNpcAttributes;
    }

    /**
     * Loads the dialogue and the attributes of the NPC.
     *
     * @param yamlContent the whole dialogue data read from a file
     * @return An NpcDialogueData object.
     */
    private NpcDialogueData loadNpcDialogue(LinkedHashMap yamlContent, NpcAttributes npcAttributes) {

        //TODO: catch null for the next 6 lines.
        Map<String, Object> rawActionGraph = (LinkedHashMap) yamlContent.get("actionGraph");
        String startActionText = rawActionGraph.get("startAction").toString();
        Map<String, Object> npcActions = (LinkedHashMap) rawActionGraph.get("npcActions");
        Map<String, Object> playerActions = (LinkedHashMap) rawActionGraph.get("playerActions");
        Map<String, String> actionContents = (LinkedHashMap) yamlContent.get("actionContent");
        Map<String, LinkedHashMap> actionConditions = (LinkedHashMap) rawActionGraph.get("actionConditions");

        //Make a map <Key, Action> for the NPC & Player
        Map<String, Action> dialogueMap = new HashMap<>();

        addPlayerActionsToMap(playerActions, actionContents, dialogueMap);

        addNpcActionsToMap(npcActions, actionContents, dialogueMap);

        addTargetActions(npcActions, playerActions, dialogueMap);

        if (!actionConditions.isEmpty()) {
            addActionConditions(actionConditions, dialogueMap);
        }
        return new NpcDialogueData(npcAttributes, dialogueMap.get(startActionText));
    }

    /**
     * Adds all targetActions to npcActions and playerActions in the map.
     *
     * @param npcActions    the npcActions from the yaml file
     * @param playerActions the playerActions from the yaml file
     * @param dialogueMap   a map with actions from Npc and Player
     */
    private void addTargetActions(Map<String, Object> npcActions, Map<String, Object> playerActions, Map<String, Action> dialogueMap) {
        for (Map.Entry<String, Action> entry : dialogueMap.entrySet()) {
            // Add targetActions to npcActions in dialogueMap
            if (npcActions.containsKey(entry.getKey())) {
                ArrayList<String> npcTargetActionList = (ArrayList<String>) npcActions.get(entry.getKey());
                for (String targetActionName : npcTargetActionList) {
                    entry.getValue().addTargetAction(dialogueMap.get(targetActionName));
                }
            }
            // Add targetActions to playerActions in dialogueMap
            if (playerActions.containsKey(entry.getKey())) {
                ArrayList<String> playerTargetActionList = (ArrayList<String>) playerActions.get(entry.getKey());
                for (String targetActionName : playerTargetActionList) {
                    entry.getValue().addTargetAction(dialogueMap.get(targetActionName));
                }
            }
        }
    }

    /**
     * Adds all actionConditions to the npcActions and playerActions.
     *
     * @param actionConditions a map of conditions the use of an action depends on
     * @param dialogueMap      a map with actions from Npc and Player
     */
    private void addActionConditions(Map<String, LinkedHashMap> actionConditions, Map<String, Action> dialogueMap) {
        for (Map.Entry<String, Action> entry : dialogueMap.entrySet()) {
            if (actionConditions.containsKey(entry.getKey())) {
                LinkedHashMap<String, Object> mapOfActionConditions = actionConditions.get(entry.getKey());
                for (Map.Entry<String, Object> actionConditionEntry : mapOfActionConditions.entrySet()) {
                    entry.getValue().addActionCondition(actionConditionEntry.getKey(), actionConditionEntry.getValue());
                }
            }
        }
    }

    /**
     * Adds the npcActions from the yaml file as objects to the map.
     *
     * @param npcActions     the npcActions from the yaml file
     * @param actionContents the actionTexts of the npcActions
     * @param dialogueMap    a map with actions from Npc and Player
     */
    private void addNpcActionsToMap(Map<String, Object> npcActions, Map<String, String> actionContents, Map<String, Action> dialogueMap) {
        for (Map.Entry<String, Object> npcEntry : npcActions.entrySet()) {
            ArrayList<String> targetActions = (ArrayList<String>) npcEntry.getValue();
            if (targetActions.size() > 0) {
                String firstTargetAction = targetActions.get(0);
                if (npcActions.containsKey(firstTargetAction)) {
                    dialogueMap.put(npcEntry.getKey(), new NpcAction(Role.NPC, actionContents.get(npcEntry.getKey()), npcEntry.getKey()));
                } else {
                    dialogueMap.put(npcEntry.getKey(), new NpcAction(Role.PLAYER, actionContents.get(npcEntry.getKey()), npcEntry.getKey()));
                }
            } else { //When this npcAction is one possible ending of the dialogue
                dialogueMap.put(npcEntry.getKey(), new NpcAction(Role.PLAYER, actionContents.get(npcEntry.getKey()), npcEntry.getKey()));
            }
        }
    }

    /**
     * Adds the playerActions from the yaml file as objects to the map.
     *
     * @param playerActions  the playerActions from the yaml file
     * @param actionContents the actionTexts of the playerActions
     * @param dialogueMap    a map with actions from Npc and Player
     */
    private void addPlayerActionsToMap(Map<String, Object> playerActions, Map<String, String> actionContents, Map<String, Action> dialogueMap) {
        for (Map.Entry<String, Object> playerEntry : playerActions.entrySet()) {
            ArrayList<String> targetActions = (ArrayList<String>) playerEntry.getValue();
            if (targetActions.size() > 0) {
                String firstTargetAction = targetActions.get(0);
                if (playerActions.containsKey(firstTargetAction)) {
                    dialogueMap.put(playerEntry.getKey(), new PlayerAction(Role.PLAYER, actionContents.get(playerEntry.getKey()), playerEntry.getKey()));
                } else {
                    dialogueMap.put(playerEntry.getKey(), new PlayerAction(Role.NPC, actionContents.get(playerEntry.getKey()), playerEntry.getKey()));
                }
            } else { //When this playerAction is one possible ending of the dialogue
                dialogueMap.put(playerEntry.getKey(), new PlayerAction(Role.NPC, actionContents.get(playerEntry.getKey()), playerEntry.getKey()));
            }
        }
    }

    /**
     * Gets a file by its name from the classpath.
     *
     * @param fileName the name of the file.
     * @return the file.
     */
    public File getFileFromClassPath(final String fileName) {
        Check.notNullArgument(fileName, "fileName");

        String absoluteFileName;
        if (fileName.startsWith("/")) {
            absoluteFileName = fileName;
        } else {
            absoluteFileName = "/" + fileName;
        }

        java.net.URL fileUrl = this.getClass().getResource(absoluteFileName);
        if (fileUrl == null) {
            throw new RuntimeException("file with name `" + absoluteFileName + "` not found in classpath");
        }
        try {
            Path filePath = Paths.get(fileUrl.toURI());
            return filePath.toFile();
        } catch (URISyntaxException e) {
            throw new RuntimeException("error while loading file `" + absoluteFileName + "` from  classpath");
        }
    }
}
