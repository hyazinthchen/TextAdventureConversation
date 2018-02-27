package npcDialogue.controller;

import npcDialogue.model.*;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Loads the dialogue for one NPC from a Yaml File.
 */
public class DialogueLoader {

    /**
     * Loads the whole data of the dialogue from a yaml file (actions and npcTraits.
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
     * Loads the whole data of the dialogue from a yaml file (actions and npcTraits.
     *
     * @param path the path of the yaml file
     * @return A new NpcDialogueData object.
     * @throws FileNotFoundException in case the path is incorrect
     */
    public NpcDialogueData load(String path) throws FileNotFoundException, InvalidStateException {
        return load(new File(path));
    }

    /**
     * Loads the NPCs traits from the yaml file.
     *
     * @param yamlContent the whole dialogue data read from a file
     * @return An NpcTraits object.
     */
    public NpcTraits loadNpcTraits(LinkedHashMap yamlContent) {
        NpcTraits newNpcTraits = new NpcTraits();
        LinkedHashMap<String, Object> rawNpcTraits = (LinkedHashMap) yamlContent.get("npcData");
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
    public NpcDialogueData loadNpcDialogue(LinkedHashMap yamlContent, NpcTraits npcTraits) throws InvalidStateException {
        LinkedHashMap<String, Object> rawActionGraph = (LinkedHashMap) yamlContent.get("actionGraph");
        String startActionText = rawActionGraph.get("entryPoint").toString();
        LinkedHashMap<String, Object> npcActions = (LinkedHashMap) rawActionGraph.get("npcActions");
        LinkedHashMap<String, Object> playerActions = (LinkedHashMap) rawActionGraph.get("playerActions");
        LinkedHashMap<String, String> actionContents = (LinkedHashMap) yamlContent.get("actionContent");

        //Make a map <Key, Action> for the NPC & Player
        Map<String, Action> dialogueMap = new HashMap<>();

        addPlayerActionsToMap(playerActions, actionContents, dialogueMap);

        addNpcActionsToMap(npcActions, actionContents, dialogueMap);

        addTargetActions(npcActions, playerActions, dialogueMap);

        NpcDialogueData npcDialogueData = new NpcDialogueData(npcTraits, dialogueMap.get(startActionText));
        return npcDialogueData;
    }

    /**
     * Adds all targetActions to npcActions and playerActions in the map
     *
     * @param npcActions    the npcActions from the yaml file
     * @param playerActions the playerActions from the yaml file
     * @param dialogueMap
     * @throws InvalidStateException
     */
    private void addTargetActions(LinkedHashMap<String, Object> npcActions, LinkedHashMap<String, Object> playerActions, Map<String, Action> dialogueMap) throws InvalidStateException {
        for (Map.Entry<String, Action> action : dialogueMap.entrySet()) {
            // Add targetActions to npcActions in dialogueMap
            if (npcActions.containsKey(action.getKey())) {
                ArrayList<String> npcTargetActionList = (ArrayList<String>) npcActions.get(action.getKey());
                for (String targetActionName : npcTargetActionList) {
                    action.getValue().addTargetAction(dialogueMap.get(targetActionName));
                }
            }
            // Add targetActions to playerActions in dialogueMap
            if (playerActions.containsKey(action.getKey())) {
                ArrayList<String> playerTargetActionList = (ArrayList<String>) playerActions.get(action.getKey());
                for (String targetActionName : playerTargetActionList) {
                    action.getValue().addTargetAction(dialogueMap.get(targetActionName));
                }
            }
        }
    }

    /**
     * Adds the npcActions from the yaml file as objects to the map
     *
     * @param npcActions     the npcActions from the yaml file
     * @param actionContents the actionTexts of the npcActions
     * @param dialogueMap
     */
    private void addNpcActionsToMap(LinkedHashMap<String, Object> npcActions, LinkedHashMap<String, String> actionContents, Map<String, Action> dialogueMap) {
        for (Map.Entry<String, Object> npcAction : npcActions.entrySet()) {
            ArrayList<String> targetActions = (ArrayList<String>) npcAction.getValue();
            if (targetActions.size() > 0) {
                String firstTargetAction = targetActions.get(0);
                if (npcActions.containsKey(firstTargetAction)) {
                    dialogueMap.put(npcAction.getKey(), new NpcAction(ActorType.NPC, actionContents.get(npcAction.getKey())));
                } else {
                    dialogueMap.put(npcAction.getKey(), new NpcAction(ActorType.PLAYER, actionContents.get(npcAction.getKey())));
                }
            } else { //When this npcAction is one possible ending of the dialogue
                dialogueMap.put(npcAction.getKey(), new NpcAction(ActorType.PLAYER, actionContents.get(npcAction.getKey())));
            }
        }
    }

    /**
     * Adds the playerActions from the yaml file as objects to the map
     *
     * @param playerActions  the playerActions from the yaml file
     * @param actionContents the actionTexts of the playerActions
     * @param dialogueMap
     */
    private void addPlayerActionsToMap(LinkedHashMap<String, Object> playerActions, LinkedHashMap<String, String> actionContents, Map<String, Action> dialogueMap) {
        for (Map.Entry<String, Object> playerAction : playerActions.entrySet()) {
            ArrayList<String> targetActions = (ArrayList<String>) playerAction.getValue();
            if (targetActions.size() > 0) {
                String firstTargetAction = targetActions.get(0);
                if (playerActions.containsKey(firstTargetAction)) {
                    dialogueMap.put(playerAction.getKey(), new PlayerAction(ActorType.PLAYER, actionContents.get(playerAction.getKey())));
                } else {
                    dialogueMap.put(playerAction.getKey(), new PlayerAction(ActorType.NPC, actionContents.get(playerAction.getKey())));
                }
            } else { //When this playerAction is one possible ending of the dialogue
                dialogueMap.put(playerAction.getKey(), new PlayerAction(ActorType.NPC, actionContents.get(playerAction.getKey())));
            }
        }
    }

}
