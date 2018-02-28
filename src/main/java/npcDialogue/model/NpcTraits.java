package npcDialogue.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Describes the relationship of the NPC to the player. Actions are visible to the player depending on the npcTraits.
 */
public class NpcTraits { //TODO: implement method that changes a npcTrait in the map when an action has been chosen
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
     * Gets the traits of an NPC as a Map <String, Object>.
     *
     * @return A the Map of NpcTraits.
     */
    public Map<String, Object> getTraits() {
        return this.npcTraits;
    }
}
