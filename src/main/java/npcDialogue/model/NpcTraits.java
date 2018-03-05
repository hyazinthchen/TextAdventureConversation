package npcDialogue.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Describes the relationship of the NPC to the player. Actions are visible to the player depending on the npcTraits.
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

    public Map<String, Object> getNpcTraits() {
        return npcTraits;
    }

    public void modifyEntry(String a, Object b) { //TODO: implement!!!

    }
}
