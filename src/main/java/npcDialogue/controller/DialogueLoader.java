package main.java.npcDialogue.controller;

import main.java.npcDialogue.model.ActorType;
import main.java.npcDialogue.model.NpcAction;
import main.java.npcDialogue.model.NpcData;
import main.java.npcDialogue.model.NpcDialogueData;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.LinkedHashMap;

/**
 * Loads Dialogue from Text File per NPC
 */
public class DialogueLoader {
    public NpcDialogueData load(String path) throws FileNotFoundException {
        InputStream inputStream = new FileInputStream(new File(path));
        Yaml yaml = new Yaml();
        LinkedHashMap testData = yaml.load(inputStream);

        NpcData npcData = new NpcData();
        Iterable<Object> rawNpcData = (Iterable<Object>) testData.get("npcData");
        System.out.println(rawNpcData);


        /*TODO
        reading data from this NPCs YAML file, creating:
        - new NpcData with initial values
        - the Action graph, represented by the root action
         */
        return new NpcDialogueData(npcData, new NpcAction(ActorType.PLAYER)); //dummy data !
    }
}
