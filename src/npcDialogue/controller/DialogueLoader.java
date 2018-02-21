package npcDialogue.controller;

import npcDialogue.model.*;

/**
 * Loads Dialogue from Text File per NPC
 */
public class DialogueLoader {
    public NpcDialogueData load(String path) {

        /*TODO
        reading data from this NPCs YAML file, creating:
        - new NpcData with initial values
        - the Action graph, represented by the root action
         */
        return new NpcDialogueData(new NpcData(), new NpcAction(ActorType.PLAYER)); //dummy data !
    }
}
