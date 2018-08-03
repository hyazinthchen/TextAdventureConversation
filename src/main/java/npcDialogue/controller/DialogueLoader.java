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

    public NpcDialogueData load(String fileName) throws ParsingException {
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
        try (InputStream in = DialogueLoader.class.getClassLoader().getResourceAsStream(fileName)) {
            Yaml yaml = new Yaml();
            yamlDialogue = yaml.load(in);
        } catch (IOException e) {
            System.out.println("Error loading yaml file. File not found. Detailed message: \n" + e.getMessage());
        }
        return yamlDialogue;
    }

    /**
     * Loads the NPCs attributes from the yaml file.
     *
     * @param yamlDialogue the whole dialogue data read from a file
     * @return An NpcAttributes object.
     */
    @SuppressWarnings("unchecked")
    private NpcAttributes loadNpcAttributes(Map<String, Object> yamlDialogue) throws ParsingException {
        LinkedHashMap<String, Object> rawNpcAttributes = (LinkedHashMap) yamlDialogue.get("npcAttributes");
        if (rawNpcAttributes != null) {
            NpcAttributes newNpcAttributes = new NpcAttributes();
            for (Map.Entry<String, Object> entry : rawNpcAttributes.entrySet()) {
                if (entry.getValue() instanceof Integer) {
                    newNpcAttributes.addAttribute(entry.getKey(), (Integer) entry.getValue());
                }
            }
            return newNpcAttributes;

        } else {
            throw new ParsingException("Error loading yaml file. Couldn't find 'npcAttributes' in yaml file.");
        }
    }

    @SuppressWarnings("unchecked")
    private NpcDialogueData loadActionGraph(Map<String, Object> yamlDialogue, NpcAttributes npcAttributes) throws ParsingException {
        Map<String, TreeNode> loadedDialogue = new HashMap<>();
        Map<String, Object> actionGraph = (LinkedHashMap) yamlDialogue.get("actionGraph");
        if (actionGraph == null) {
            throw new ParsingException("Error loading yaml file. Couldn't find 'actionGraph' in yaml file.");
        }
        String startActionId = actionGraph.get("startAction").toString();
        if (startActionId == null) {
            throw new ParsingException("Error loading yaml file. Couldn't find 'startAction' in yaml file.");
        }
        Map<String, Object> npcActions = (LinkedHashMap) actionGraph.get("npcActions");
        if (npcActions == null) {
            throw new ParsingException("Error loading yaml file. Couldn't find 'npcActions' in yaml file.");
        }
        Map<String, Object> playerActions = (LinkedHashMap) actionGraph.get("playerActions");
        if (playerActions == null) {
            throw new ParsingException("Error loading yaml file. Couldn't find 'playerActions' in yaml file.");
        }
        Map<String, String> actionContents = (LinkedHashMap) yamlDialogue.get("actionContent");
        if (actionContents == null) {
            throw new ParsingException("Error loading yaml file. Couldn't find 'actionContent' in yaml file.");
        }

        addPlayerActionsToLoadedDialogue(playerActions, actionContents, loadedDialogue);
        addNpcActionsToLoadedDialogue(npcActions, actionContents, loadedDialogue);
        addTargetActionsToLoadedDialogue(npcActions, playerActions, loadedDialogue);

        loadOptionalDialogueComponents(actionGraph, loadedDialogue);

        return new NpcDialogueData(npcAttributes, loadedDialogue.get(startActionId));
    }

    /**
     * Loads the optional elements of the yaml file: actionConditions, npcAttributeModification and decisions
     *
     * @param actionGraph
     * @param loadedDialogue
     */
    private void loadOptionalDialogueComponents(Map<String, Object> actionGraph, Map<String, TreeNode> loadedDialogue) {
        loadActionConditions(actionGraph, loadedDialogue);
        loadNpcAttributeModifications(actionGraph, loadedDialogue);
        loadDecisions(actionGraph, loadedDialogue);
    }

    /**
     * Loads the actionConditions (when an action relies on an NpcAttribute)
     *
     * @param actionGraph
     * @param loadedDialogue
     */
    private void loadActionConditions(Map<String, Object> actionGraph, Map<String, TreeNode> loadedDialogue) {
        Map<String, LinkedHashMap> actionConditions = (LinkedHashMap) actionGraph.get("actionConditions");
        if (!actionConditions.isEmpty()) {
            addActionConditionsToLoadedDialogue(actionConditions, loadedDialogue);
        }
    }

    /**
     * Loads the actionNpcAttributeModifications (when an action changes the NPC's attributes)
     *
     * @param actionGraph
     * @param loadedDialogue
     */
    private void loadNpcAttributeModifications(Map<String, Object> actionGraph, Map<String, TreeNode> loadedDialogue) {
        Map<String, LinkedHashMap> npcAttributeModifications = (LinkedHashMap) actionGraph.get("npcAttributeModifications");
        if (!npcAttributeModifications.isEmpty()) {
            addNpcAttributeModificationsToLoadedDialogue(npcAttributeModifications, loadedDialogue);
        }
    }

    /**
     * Loads the decisions in a dialogue between player and an NPC
     *
     * @param actionGraph
     * @param loadedDialogue
     */
    private void loadDecisions(Map<String, Object> actionGraph, Map<String, TreeNode> loadedDialogue) {
        Map<String, LinkedHashMap> decisions = (LinkedHashMap) actionGraph.get("decisions");
        if (!decisions.isEmpty()) {
            addDecisionsToLoadedDialogue(decisions, loadedDialogue);
        }
    }

    /**
     * Adds the playerActions from the yaml file as objects to the map.
     *
     * @param playerActions  the playerActions from the yaml file
     * @param actionContents the actionTexts of the playerActions
     * @param loadedDialogue a map with actions from Npc and Player
     */
    @SuppressWarnings("unchecked")
    private void addPlayerActionsToLoadedDialogue(Map<String, Object> playerActions, Map<String, String> actionContents, Map<String, TreeNode> loadedDialogue) {
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
     * @param loadedDialogue a map with actions from Npc and Player
     */
    @SuppressWarnings("unchecked")
    private void addNpcActionsToLoadedDialogue(Map<String, Object> npcActions, Map<String, String> actionContents, Map<String, TreeNode> loadedDialogue) {
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
     * @param npcActions     the npcActions from the yaml file
     * @param playerActions  the playerActions from the yaml file
     * @param loadedDialogue a map with actions from Npc and Player
     */
    @SuppressWarnings("unchecked")
    private void addTargetActionsToLoadedDialogue(Map<String, Object> npcActions, Map<String, Object> playerActions, Map<String, TreeNode> loadedDialogue) {
        for (Map.Entry<String, TreeNode> entry : loadedDialogue.entrySet()) {
            if (npcActions.containsKey(entry.getKey())) {
                ArrayList<String> npcTargetActionList = (ArrayList<String>) npcActions.get(entry.getKey());
                for (String targetActionName : npcTargetActionList) {
                    entry.getValue().addTargetNode(loadedDialogue.get(targetActionName));
                }
            }
            if (playerActions.containsKey(entry.getKey())) {
                ArrayList<String> playerTargetActionList = (ArrayList<String>) playerActions.get(entry.getKey());
                for (String targetActionName : playerTargetActionList) {
                    entry.getValue().addTargetNode(loadedDialogue.get(targetActionName));
                }
            }
        }
    }

    /**
     * Adds all actionConditions to the npcActions and playerActions.
     *
     * @param actionConditions a map of conditions the use of an action depends on
     * @param loadedDialogue   a map with actions from Npc and Player
     */
    @SuppressWarnings("unchecked")
    private void addActionConditionsToLoadedDialogue(Map<String, LinkedHashMap> actionConditions, Map<String, TreeNode> loadedDialogue) {
        for (Map.Entry<String, TreeNode> entry : loadedDialogue.entrySet()) {
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
     * @param loadedDialogue            a map with actions from Npc and Player
     */
    @SuppressWarnings("unchecked")
    private void addNpcAttributeModificationsToLoadedDialogue(Map<String, LinkedHashMap> npcAttributeModifications, Map<String, TreeNode> loadedDialogue) {
        for (Map.Entry<String, TreeNode> entry : loadedDialogue.entrySet()) {
            if (npcAttributeModifications.containsKey(entry.getKey())) {
                List<LinkedHashMap<String, Object>> listOfActionModifications = (List<LinkedHashMap<String, Object>>) npcAttributeModifications.get(entry.getKey());
                for (LinkedHashMap<String, Object> npcAttributeModification : listOfActionModifications) {
                    entry.getValue().addNpcAttributeModification((String) npcAttributeModification.get("attribute"), (String) npcAttributeModification.get("operator"), (Integer) npcAttributeModification.get("value"));
                }
            }
        }
    }

    /**
     * Adds all decisions as objects to the dialogue.
     *
     * @param decisions
     * @param loadedDialogue
     */
    @SuppressWarnings("unchecked")
    private void addDecisionsToLoadedDialogue(Map<String, LinkedHashMap> decisions, Map<String, TreeNode> loadedDialogue) {
        List<Decision> decisionList = new ArrayList<>();
        for (Map.Entry<String, LinkedHashMap> decision : decisions.entrySet()) {
            List<Condition> conditionsOfDecision = new ArrayList<>();
            for (Map<String, Object> conditionOfDecision : (List<LinkedHashMap>) decision.getValue().get("conditions")) {
                //conditionsOfDecision.add(new Condition((String) conditionOfDecision.get("attribute"), (String) conditionOfDecision.get("operator"), (int) conditionOfDecision.get("value"))); TODO: how to map String to RelationalOperator?
            }
            TreeNode thenAction = loadedDialogue.get(decision.getValue().get("thenAction"));
            TreeNode elseAction = loadedDialogue.get(decision.getValue().get("elseAction"));
            decisionList.add(new Decision(thenAction, elseAction, conditionsOfDecision));
        }
        //TODO: add the decisions in decisionList to the dialogue
    }
}
