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
    public void modifyAttribute(String key, Object value) { //TODO: maybe wrong place for error detection, better put it in the DialogueValidator
        if (npcAttributes.get(key).getClass() == value.getClass()) {
            npcAttributes.put(key, value);
        } else {
            throw new IllegalArgumentException("Attribute " + key + " is of type " + npcAttributes.get(key).getClass() + " and can't be changed to " + value.getClass());
        }
    }

    /**
     * Evaluates if a set of requirements matches the set of npcAttributes.
     *
     * @param requirementSet the conditions of an action.
     * @return true if the conditions are fulfilled.
     */
    public boolean fulfill(Set<Map.Entry<String, Object>> requirementSet) {
        return getNpcAttributes().entrySet().containsAll(requirementSet);
    }

    /**
     * Evaluates if a map of requirements matches the npcAttributes.
     *
     * @param requirements the conditions of an action.
     * @return true if the conditions are fulfilled.
     */
    public boolean fulfill(Map<String, Object> requirements) {
        return fulfill(requirements.entrySet());
    }
}
