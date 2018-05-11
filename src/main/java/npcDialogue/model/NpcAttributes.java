package npcDialogue.model;

import npcDialogue.view.ConsoleReaderWriter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Describes the relationship of the NPC to the player. Actions are visible to the player depending on the npcAttributes.
 */
public class NpcAttributes {
    private Map<String, Integer> npcAttributes = new HashMap<>();

    public NpcAttributes(final Map<String, Integer> npcAttributes) {
        this.npcAttributes = new HashMap<>(npcAttributes);
    }

    public NpcAttributes() {
    }

    public Map<String, Integer> getNpcAttributes() {
        return npcAttributes;
    }

    /**
     * Adds a new attribute to the NPC.
     *
     * @param key   the name of the attribute.
     * @param value the value of the attribute.
     */
    public void addAttribute(String key, Integer value) {
        npcAttributes.put(key, value);
    }

    /**
     * Changes the value of one npcAttribute.
     *
     * @param npcAttribute the attribute that will be changed
     * @param operator     the operator which determines the value change
     * @param value        the value by which the value will change, can be "+" or "-"
     */
    public void modifyAttribute(String npcAttribute, Operator operator, int value) {
        if (npcAttributes.containsKey(npcAttribute)) {
            int oldAttributeValue = npcAttributes.get(npcAttribute);
            int newAttributeValue;
            if (operator == Operator.MINUS) {
                newAttributeValue = oldAttributeValue - value;
            } else {
                newAttributeValue = oldAttributeValue + value;
            }
            npcAttributes.put(npcAttribute, newAttributeValue);
        } else {
            new ConsoleReaderWriter().printErrorMessage("Can't modify attribute " + npcAttribute + "because it doesn't exist.");
        }
    }

    /**
     * Evaluates if a set of conditions matches the set of npcAttributes.
     *
     * @param conditions the conditions of an action.
     * @return true if the conditions are fulfilled.
     */
    public boolean match(List<Condition> conditions) {
        for (Condition condition : conditions) {
            if (!condition.fulfills(npcAttributes)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Copies the npcAttributes.
     *
     * @return a copy of the npcAttributes
     */
    public NpcAttributes copy() {
        return new NpcAttributes(this.npcAttributes);
    }
}
