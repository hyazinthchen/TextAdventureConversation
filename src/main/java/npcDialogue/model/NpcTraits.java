package npcDialogue.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Describes the Relationship of the NPC to the Player. Actions can be chosen depending on the npcTraits.
 */
public class NpcTraits {
    private Map<String, Object> npcTraits = new HashMap<>();

    /**
     * Adds a new trait to the NPC.
     *
     * @param key   the name of the trait
     * @param value the value of the trait
     */
    public void addDataEntry(String key, Object value) {
        npcTraits.put(key, value);
    }

    /**
     * Gets the traits of an NPC as a Map <String, Object>
     *
     * @return A the Map of NpcTraits.
     */
    public Map<String, Object> getTraits() {
        return this.npcTraits;
    }
}
