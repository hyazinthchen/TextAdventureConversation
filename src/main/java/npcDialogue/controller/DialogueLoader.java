package npcDialogue.controller;

import npcDialogue.model.*;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Loads the dialogue for one NPC from a Yaml File.
 */
public class DialogueLoader {

    /**
     * Loads the data of the dialogue from a yaml file.
     *
     * @param path the path of the yaml file
     * @return returns a new NpcDialogueData object
     * @throws FileNotFoundException in case the path is incorrect
     */
    public NpcDialogueData load(String path) throws FileNotFoundException {
        InputStream inputStream = new FileInputStream(new File(path));
        Yaml yaml = new Yaml();
        LinkedHashMap testData = yaml.load(inputStream);

        NpcTraits newNpcTraits = loadNpcTraits(testData);
        NpcDialogueData npcDialogueData = loadNpcDialogue(testData, newNpcTraits);

        /*TODO
        reading data from this NPCs YAML file, creating:
        - new NpcTraits with initial values
        - the Action graph, represented by the root action
         */
        return npcDialogueData; //dummy data !
    }

    /**
     * Loads the NPCs traits from the yaml file.
     *
     * @param yamlContent the whole dialogue data
     * @return returns an NpcTraits object
     */
    public NpcTraits loadNpcTraits(LinkedHashMap yamlContent) {
        NpcTraits newNpcTraits = new NpcTraits();
        LinkedHashMap<String, Object> rawNpcTraits = getMapOrThrow(yamlContent, "npcData");
        for (Map.Entry<String, Object> entry : rawNpcTraits.entrySet()) {
            newNpcTraits.addDataEntry(entry.getKey(), entry.getValue());
        }
        return newNpcTraits;
    }

    //TODO: throw own parsing exception if get(key) is null or doesnt return a map, surround wih try catch, catch own exception
    private LinkedHashMap<String, Object> getMapOrThrow(LinkedHashMap yamlContent, String key) {
            return (LinkedHashMap) yamlContent.get(key);
    }

    /**
     * Loads the dialogue and the traits of the NPC.
     *
     * @param testData the whole dialogue data read from a file
     * @return returns an NpcDialogueData object
     */
    public NpcDialogueData loadNpcDialogue(LinkedHashMap testData, NpcTraits npcTraits) {
        LinkedHashMap<String, Object> rawActionGraph = (LinkedHashMap) testData.get("actionGraph");
        String startActionText = rawActionGraph.get("entryPoint").toString();
        LinkedHashMap<String, Object> npcActions = (LinkedHashMap) rawActionGraph.get("npcActions");
        LinkedHashMap<String, Object> playerActions = (LinkedHashMap) rawActionGraph.get("playerActions");
        LinkedHashMap<String, Object> actionContents = (LinkedHashMap) rawActionGraph.get("actionContent");
        NpcDialogueData newNpcDialogueData = new NpcDialogueData(npcTraits, new NpcAction(ActorType.PLAYER, startActionText)); //TODO how to get type of start action and targetactortype of next actions?

        getNpcActions(npcActions, actionContents);
        getPlayerActions(playerActions, actionContents);
        return newNpcDialogueData; //TODO: alle actions miteinander verketten und hier nur startaction zurückgeben
    }

    /**
     * Loads only the playerAction Objects from the input data
     *
     * @param npcActions
     * @param actionContents
     */
    private void getNpcActions(LinkedHashMap<String, Object> npcActions, LinkedHashMap<String, Object> actionContents) {
        for (Map.Entry<String, Object> actionEntry : npcActions.entrySet()) {
            for (Map.Entry<String, Object> contentEntry : actionContents.entrySet()) {
                if (actionEntry.getKey() == contentEntry.getKey()) {
                    String actionText = contentEntry.getValue().toString();
                    NpcAction action = new NpcAction(ActorType.NPC, actionText);
                    // TODO: NpcAction.setMessage((String) contentEntry.getValue());
                }
            }
        }
    }

    /**
     * Loads only the playerAction Objects from the input data
     *
     * @param playerActions
     */
    private void getPlayerActions(LinkedHashMap<String, Object> playerActions, LinkedHashMap<String, Object> actionContents) {
        for (Map.Entry<String, Object> actionEntry : playerActions.entrySet()) {
            for (Map.Entry<String, Object> contentEntry : actionContents.entrySet()) {
                if (actionEntry.getKey() == contentEntry.getKey()) {
                    String actionText = contentEntry.getValue().toString();
                    PlayerAction action = new PlayerAction(ActorType.PLAYER, actionText);
                    //TODO: PlayerAction.setMessage((String) contentEntry.getValue());
                }
            }
        }
    }
}
