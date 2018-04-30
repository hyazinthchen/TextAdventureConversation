package npcDialogue.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Describes the relationship of the NPC to the player. Actions are visible to the player depending on the npcAttributes.
 */
public class NpcAttributes {
    private Map<String, Object> npcAttributes = new HashMap<>();

    public NpcAttributes(Map<String, Object> npcAttributes) {
        this.npcAttributes = new HashMap<>(npcAttributes);
    }

    public NpcAttributes() {
    }

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
        if (npcAttributes.get(key).getClass() == value.getClass() || npcAttributes.get(key).getClass() == Integer.class && value.getClass() == String.class) {
            if (value instanceof String) { //TODO: there is a better way!
                if (((String) value).startsWith("+")) {
                    int summand = Integer.parseInt(((String) value).replace("+", ""));
                    int newValue = (int) npcAttributes.get(key) + summand;
                    npcAttributes.put(key, newValue);
                }
                if (((String) value).startsWith("-")) {
                    int subtrahend = Integer.parseInt(((String) value).replace("-", ""));
                    int newValue = (int) npcAttributes.get(key) - subtrahend;
                    npcAttributes.put(key, newValue);
                }
            } else {
                npcAttributes.put(key, value);
            }
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

    public NpcAttributes copy() {
        return new NpcAttributes(this.npcAttributes);
    }
}
