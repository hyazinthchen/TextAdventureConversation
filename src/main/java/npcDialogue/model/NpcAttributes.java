package npcDialogue.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Describes the relationship of the NPC to the player. Actions are visible to the player depending on the npcAttributes.
 */
public class NpcAttributes {
    private Map<String, Object> npcAttributes = new HashMap<>();

    /**
     * Adds a new attribute to the NPC.
     *
     * @param key   the name of the attribute.
     * @param value the value of the attribute.
     */
    public void addAttribute(String key, Object value) {
        npcAttributes.put(key, value);
    }

    public Map<String, Object> getNpcAttributes() {
        return npcAttributes;
    }

    /**
     * Changes the value of one npcAttribute.
     *
     * @param key   the name of the attribute.
     * @param value the new value of the attribute.
     */
    public void modifyAttribute(String key, Object value) { //TODO: What if the value of an attribute is boolean and I change it to int? Should that be possible?
        npcAttributes.put(key, value);
    }

    /**
     * Evaluates if the npcAttributes contain an actions conditions.
     *
     * @param conditionSet the conditions of an action.
     * @return true if the conditions are fulfilled.
     */
    public boolean contain(Set<Map.Entry<String, Object>> conditionSet) {
        return getNpcAttributes().entrySet().containsAll(conditionSet);
    }
}
