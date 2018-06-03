package npcDialogue.controller;

import npcDialogue.model.*;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Loads the dialogue between one NPC and the player from a yaml file.
 */
public class DialogueLoader {

    public NpcDialogueData load(String fileName){
        Map<String, Object> yamlDialogue = loadFromYamlFile(fileName);

        NpcAttributes npcAttributes = loadNpcAttributes(yamlDialogue);
        loadActionGraph(yamlDialogue, npcAttributes);
        return loadActionGraph(yamlDialogue, npcAttributes);
    }

    /**
     * Loads the whole data of the dialogue from a yaml file (actions and npcAttributes).
     *
     * @param fileName the yaml filename
     * @return A new NpcDialogueData object.
     */
    private Map<String, Object> loadFromYamlFile(String fileName) {
        Map<String, Object> yamlDialogue = new HashMap<>();
        try (InputStream in = DialogueLoader.class.getClassLoader().getResourceAsStream(fileName)){
            Yaml yaml = new Yaml();
            yamlDialogue = yaml.load(in);
        } catch (IOException e) {
            e.printStackTrace(); //TODO file not found etc
        }
        return yamlDialogue;
    }

    /**
     * Loads the NPCs attributes from the yaml file.
     *
     * @param yamlDialogue the whole dialogue data read from a file
     * @return An NpcAttributes object.
     */
    private NpcAttributes loadNpcAttributes(Map<String, Object> yamlDialogue){
        NpcAttributes newNpcAttributes = new NpcAttributes();
        LinkedHashMap<String, Object> rawNpcAttributes = (LinkedHashMap) yamlDialogue.get("npcAttributes");
        for (Map.Entry<String, Object> entry : rawNpcAttributes.entrySet()) {
            if (entry.getValue() instanceof Integer) {
                newNpcAttributes.addAttribute(entry.getKey(), (Integer) entry.getValue());
            }
        }
        return newNpcAttributes;
    }

    private NpcDialogueData loadActionGraph(Map<String, Object> yamlDialogue, NpcAttributes npcAttributes){
        Map<String, Object> actionGraph = (LinkedHashMap) yamlDialogue.get("actionGraph");
        String startActionId = actionGraph.get("startAction").toString();
        Map<String, Object> npcActions = (LinkedHashMap) actionGraph.get("npcActions");
        Map<String, Object> playerActions = (LinkedHashMap) actionGraph.get("playerActions");
        Map<String, LinkedHashMap> actionConditions = (LinkedHashMap) actionGraph.get("actionConditions");
        Map<String, LinkedHashMap> npcAttributeModifications = (LinkedHashMap) actionGraph.get("npcAttributeModifications");
        Map<String, LinkedHashMap> decisions = (LinkedHashMap) actionGraph.get("decisions");
        Map<String, String> actionContents = (LinkedHashMap) yamlDialogue.get("actionContent");
        Map<String, Action> loadedDialogue = new HashMap<>();

        addPlayerActionsToLoadedDialogue(playerActions, actionContents, loadedDialogue);
        addNpcActionsToLoadedDialogue(npcActions, actionContents, loadedDialogue);
        addTargetActionsToLoadedDialogue(npcActions, playerActions, loadedDialogue);

        if (!actionConditions.isEmpty()) {
            addActionConditionsToLoadedDialogue(actionConditions, loadedDialogue);
        }

        if (!npcAttributeModifications.isEmpty()) {
            addNpcAttributeModificationsToLoadedDialogue(npcAttributeModifications, loadedDialogue);
        }

        if (!decisions.isEmpty()) {
            addDecisionsToLoadedDialogue(decisions, loadedDialogue);
        }

        return new NpcDialogueData(npcAttributes, loadedDialogue.get(startActionId));
    }

    /**
     * Adds the playerActions from the yaml file as objects to the map.
     *
     * @param playerActions  the playerActions from the yaml file
     * @param actionContents the actionTexts of the playerActions
     * @param loadedDialogue    a map with actions from Npc and Player
     */
    private void addPlayerActionsToLoadedDialogue(Map<String, Object> playerActions, Map<String, String> actionContents, Map<String, Action> loadedDialogue) {
        for (Map.Entry<String, Object> playerEntry : playerActions.entrySet()) {
            ArrayList<String> targetActions = (ArrayList<String>) playerEntry.getValue();
            if (targetActions.size() > 0) {
                String firstTargetAction = targetActions.get(0);
                if (playerActions.containsKey(firstTargetAction)) {
                    loadedDialogue.put(playerEntry.getKey(), new PlayerAction(Role.PLAYER, actionContents.get(playerEntry.getKey()), playerEntry.getKey()));
                } else {
                    loadedDialogue.put(playerEntry.getKey(), new PlayerAction(Role.NPC, actionContents.get(playerEntry.getKey()), playerEntry.getKey()));
                }
            } else {
                loadedDialogue.put(playerEntry.getKey(), new PlayerAction(Role.NPC, actionContents.get(playerEntry.getKey()), playerEntry.getKey()));
            }
        }
    }

    /**
     * Adds the npcActions from the yaml file as objects to the map.
     *
     * @param npcActions     the npcActions from the yaml file
     * @param actionContents the actionTexts of the npcActions
     * @param loadedDialogue    a map with actions from Npc and Player
     */
    private void addNpcActionsToLoadedDialogue(Map<String, Object> npcActions, Map<String, String> actionContents, Map<String, Action> loadedDialogue) {
        for (Map.Entry<String, Object> npcEntry : npcActions.entrySet()) {
            ArrayList<String> targetActions = (ArrayList<String>) npcEntry.getValue();
            if (targetActions.size() > 0) {
                String firstTargetAction = targetActions.get(0);
                if (npcActions.containsKey(firstTargetAction)) {
                    loadedDialogue.put(npcEntry.getKey(), new NpcAction(Role.NPC, actionContents.get(npcEntry.getKey()), npcEntry.getKey()));
                } else {
                    loadedDialogue.put(npcEntry.getKey(), new NpcAction(Role.PLAYER, actionContents.get(npcEntry.getKey()), npcEntry.getKey()));
                }
            } else {
                loadedDialogue.put(npcEntry.getKey(), new NpcAction(Role.PLAYER, actionContents.get(npcEntry.getKey()), npcEntry.getKey()));
            }
        }
    }

    /**
     * Adds all targetActions to npcActions and playerActions in the map.
     *
     * @param npcActions    the npcActions from the yaml file
     * @param playerActions the playerActions from the yaml file
     * @param loadedDialogue   a map with actions from Npc and Player
     */
    private void addTargetActionsToLoadedDialogue(Map<String, Object> npcActions, Map<String, Object> playerActions, Map<String, Action> loadedDialogue) {
        for (Map.Entry<String, Action> entry : loadedDialogue.entrySet()) {
            if (npcActions.containsKey(entry.getKey())) {
                ArrayList<String> npcTargetActionList = (ArrayList<String>) npcActions.get(entry.getKey());
                for (String targetActionName : npcTargetActionList) {
                    entry.getValue().addTargetAction(loadedDialogue.get(targetActionName));
                }
            }
            if (playerActions.containsKey(entry.getKey())) {
                ArrayList<String> playerTargetActionList = (ArrayList<String>) playerActions.get(entry.getKey());
                for (String targetActionName : playerTargetActionList) {
                    entry.getValue().addTargetAction(loadedDialogue.get(targetActionName));
                }
            }
        }
    }

    /**
     * Adds all actionConditions to the npcActions and playerActions.
     *
     * @param actionConditions a map of conditions the use of an action depends on
     * @param loadedDialogue      a map with actions from Npc and Player
     */
    private void addActionConditionsToLoadedDialogue(Map<String, LinkedHashMap> actionConditions, Map<String, Action> loadedDialogue) {
        for (Map.Entry<String, Action> entry : loadedDialogue.entrySet()) {
            if (actionConditions.containsKey(entry.getKey())) {
                List<LinkedHashMap<String, Object>> listOfActionConditions = (List<LinkedHashMap<String, Object>>) actionConditions.get(entry.getKey());
                for (LinkedHashMap<String, Object> actionCondition : listOfActionConditions) {
                    entry.getValue().addCondition((String) actionCondition.get("attribute"), (String) actionCondition.get("operator"), (Integer) actionCondition.get("value"));
                }
            }
        }
    }

    /**
     * Adds all modifications to the actions. Once an action is currentAction, it can modify the npcAttributes.
     *
     * @param npcAttributeModifications a map of modifications.
     * @param loadedDialogue               a map with actions from Npc and Player
     */
    private void addNpcAttributeModificationsToLoadedDialogue(Map<String, LinkedHashMap> npcAttributeModifications, Map<String, Action> loadedDialogue) {
        for (Map.Entry<String, Action> entry : loadedDialogue.entrySet()) {
            if (npcAttributeModifications.containsKey(entry.getKey())) {
                List<LinkedHashMap<String, Object>> listOfActionModifications = (List<LinkedHashMap<String, Object>>) npcAttributeModifications.get(entry.getKey());
                for (LinkedHashMap<String, Object> npcAttributeModification : listOfActionModifications) {
                    entry.getValue().addNpcAttributeModification((String) npcAttributeModification.get("attribute"), (String) npcAttributeModification.get("operator"), (Integer) npcAttributeModification.get("value"));
                }
            }
        }
    }

    private void addDecisionsToLoadedDialogue(Map<String, LinkedHashMap> decisions, Map<String, Action> loadedDialogue) {
        for (Map.Entry<String, LinkedHashMap> decision : decisions.entrySet()) {
            //TODO: implement
        }
    }
}
