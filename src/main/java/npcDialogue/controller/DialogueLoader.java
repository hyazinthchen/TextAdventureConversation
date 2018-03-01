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
     * Loads the whole data of the dialogue from a yaml file (actions and npcTraits).
     *
     * @param file the yaml file
     * @return A new NpcDialogueData object.
     * @throws FileNotFoundException in case the path is incorrect
     */
    public NpcDialogueData load(File file) throws FileNotFoundException, InvalidStateException {
        InputStream inputStream = new FileInputStream(file);
        Yaml yaml = new Yaml();
        LinkedHashMap yamlDataMap = yaml.load(inputStream);

        NpcTraits npcTraits = loadNpcTraits(yamlDataMap);
        NpcDialogueData npcDialogueData = loadNpcDialogue(yamlDataMap, npcTraits);
        return npcDialogueData;
    }

    /**
     * Loads the NPCs traits from the yaml file.
     *
     * @param yamlContent the whole dialogue data read from a file
     * @return An NpcTraits object.
     */
    private NpcTraits loadNpcTraits(LinkedHashMap yamlContent) {
        NpcTraits newNpcTraits = new NpcTraits();
        LinkedHashMap<String, Object> rawNpcTraits = (LinkedHashMap) yamlContent.get("npcData"); //TODO: throw exception in case snakeyamls get method does stupid things
        for (Map.Entry<String, Object> entry : rawNpcTraits.entrySet()) {
            newNpcTraits.addDataEntry(entry.getKey(), entry.getValue());
        }
        return newNpcTraits;
    }

    /**
     * Loads the dialogue and the traits of the NPC.
     *
     * @param yamlContent the whole dialogue data read from a file
     * @return An NpcDialogueData object.
     */
    private NpcDialogueData loadNpcDialogue(LinkedHashMap yamlContent, NpcTraits npcTraits) throws InvalidStateException {
        LinkedHashMap<String, Object> rawActionGraph = (LinkedHashMap) yamlContent.get("actionGraph");
        String startActionText = rawActionGraph.get("startAction").toString();
        LinkedHashMap<String, Object> npcActions = (LinkedHashMap) rawActionGraph.get("npcActions");
        LinkedHashMap<String, Object> playerActions = (LinkedHashMap) rawActionGraph.get("playerActions");
        LinkedHashMap<String, String> actionContents = (LinkedHashMap) yamlContent.get("actionContent");
        LinkedHashMap<String, LinkedHashMap> actionDependencies = (LinkedHashMap) rawActionGraph.get("actionDependencies");

        //Make a map <Key, Action> for the NPC & Player
        Map<String, Action> dialogueMap = new HashMap<>();

        addPlayerActionsToMap(playerActions, actionContents, dialogueMap);

        addNpcActionsToMap(npcActions, actionContents, dialogueMap);

        addTargetActions(npcActions, playerActions, dialogueMap);

        if (!actionDependencies.isEmpty()) {
            addActionDependencies(actionDependencies, dialogueMap);
        }

        NpcDialogueData npcDialogueData = new NpcDialogueData(npcTraits, dialogueMap.get(startActionText));
        return npcDialogueData;
    }

    /**
     * Adds all targetActions to npcActions and playerActions in the map.
     *
     * @param npcActions    the npcActions from the yaml file
     * @param playerActions the playerActions from the yaml file
     * @param dialogueMap
     * @throws InvalidStateException
     */
    private void addTargetActions(LinkedHashMap<String, Object> npcActions, LinkedHashMap<String, Object> playerActions, Map<String, Action> dialogueMap) throws InvalidStateException {
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
     * Adds all actionDependencies to the npcActions and playerActions.
     */
    private void addActionDependencies(LinkedHashMap<String, LinkedHashMap> actionDependencies, Map<String, Action> dialogueMap) {
        for (Map.Entry<String, Action> entry : dialogueMap.entrySet()) {
            if (actionDependencies.containsKey(entry.getKey())) {
                LinkedHashMap<String, Object> mapOfActionDependencies = actionDependencies.get(entry.getKey());
                for (Map.Entry<String, Object> actionDependencyEntry : mapOfActionDependencies.entrySet()) {
                    entry.getValue().addActionDependency(actionDependencyEntry.getKey(), actionDependencyEntry.getValue());
                }
            }
        }
    }

    /**
     * Adds the npcActions from the yaml file as objects to the map.
     *
     * @param npcActions     the npcActions from the yaml file
     * @param actionContents the actionTexts of the npcActions
     * @param dialogueMap
     */
    private void addNpcActionsToMap(LinkedHashMap<String, Object> npcActions, LinkedHashMap<String, String> actionContents, Map<String, Action> dialogueMap) {
        for (Map.Entry<String, Object> npcEntry : npcActions.entrySet()) {
            ArrayList<String> targetActions = (ArrayList<String>) npcEntry.getValue();
            if (targetActions.size() > 0) {
                String firstTargetAction = targetActions.get(0);
                if (npcActions.containsKey(firstTargetAction)) {
                    dialogueMap.put(npcEntry.getKey(), new NpcAction(Role.NPC, actionContents.get(npcEntry.getKey())));
                } else {
                    dialogueMap.put(npcEntry.getKey(), new NpcAction(Role.PLAYER, actionContents.get(npcEntry.getKey())));
                }
            } else { //When this npcAction is one possible ending of the dialogue
                dialogueMap.put(npcEntry.getKey(), new NpcAction(Role.PLAYER, actionContents.get(npcEntry.getKey())));
            }
        }
    }

    /**
     * Adds the playerActions from the yaml file as objects to the map.
     *
     * @param playerActions  the playerActions from the yaml file
     * @param actionContents the actionTexts of the playerActions
     * @param dialogueMap
     */
    private void addPlayerActionsToMap(LinkedHashMap<String, Object> playerActions, LinkedHashMap<String, String> actionContents, Map<String, Action> dialogueMap) {
        for (Map.Entry<String, Object> playerEntry : playerActions.entrySet()) {
            ArrayList<String> targetActions = (ArrayList<String>) playerEntry.getValue();
            if (targetActions.size() > 0) {
                String firstTargetAction = targetActions.get(0);
                if (playerActions.containsKey(firstTargetAction)) {
                    dialogueMap.put(playerEntry.getKey(), new PlayerAction(Role.PLAYER, actionContents.get(playerEntry.getKey())));
                } else {
                    dialogueMap.put(playerEntry.getKey(), new PlayerAction(Role.NPC, actionContents.get(playerEntry.getKey())));
                }
            } else { //When this playerAction is one possible ending of the dialogue
                dialogueMap.put(playerEntry.getKey(), new PlayerAction(Role.NPC, actionContents.get(playerEntry.getKey())));
            }
        }
    }

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
