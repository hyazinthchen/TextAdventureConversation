package npcDialogue.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Describes the Relationship of the NPC to the Player
 */
public class NpcTraits {
    private Map<String, Object> npcTraits = new HashMap<>(); //TODO how to model NPC data? Each NPC can have different data.

    /**
     * Adds a new trait to the NPC.
     *
     * @param key   the name of the trait
     * @param value the value of the trait
     */
    public void addDataEntry(String key, Object value) {
        npcTraits.put(key, value);
    }

    public Map<String, Object> getTraits() {
        return this.npcTraits;
    }
}
