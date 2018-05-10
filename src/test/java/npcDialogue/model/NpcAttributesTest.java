package npcDialogue.model;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.AbstractMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class NpcAttributesTest {

    /**
     * Generates a simple NpcAttributes object for test purposes with one entry.
     *
     * @param key   the key of the attribute.
     * @param value the value of the attribute.
     * @return an NpcAttributes object.
     */
    private NpcAttributes generateTestNpcAttributes(String key, Integer value) {
        NpcAttributes testNpcAttributes = new NpcAttributes();
        testNpcAttributes.addAttribute(key, value);
        return testNpcAttributes;
    }

    @Test
    public void testModifyEntryByAddition() {
        NpcAttributes attributes = generateTestNpcAttributes("A", 10);
        attributes.modifyAttribute("A", Operator.PLUS, 10);
        assertEquals(new Integer(20), attributes.getNpcAttributes().get("A"));
    }

    @Test
    public void testModifyEntryBySubtraction() {
        NpcAttributes attributes = generateTestNpcAttributes("A", 10);
        attributes.modifyAttribute("A", Operator.MINUS, 10);
        assertEquals(new Integer(0), attributes.getNpcAttributes().get("A"));
    }

    @Test
    public void testSingleMatch() {
        NpcAttributes testNpcAttributes = new NpcAttributes();
        testNpcAttributes.addAttribute("A", 50);
        testNpcAttributes.addAttribute("B", 30);

        NpcAction actionX = new NpcAction(Role.NPC, "X", "X");
        actionX.addCondition("A", "==", 50);

        assertTrue(testNpcAttributes.match(actionX.getConditions()));
    }

    @Test
    public void testNoMatch() {
        NpcAttributes testNpcAttributes = new NpcAttributes();
        testNpcAttributes.addAttribute("A", 50);
        testNpcAttributes.addAttribute("B", 30);

        NpcAction actionX = new NpcAction(Role.NPC, "X", "X");
        actionX.addCondition("A", "==", 50);
        actionX.addCondition("B", "==", 40);

        assertFalse(testNpcAttributes.match(actionX.getConditions()));
    }

    @Test
    public void testCopy() {
        NpcAttributes testNpcAttributes = new NpcAttributes();
        testNpcAttributes.addAttribute("A", 10);

        NpcAttributes clonedNpcAttributes = testNpcAttributes.copy();
        testNpcAttributes.modifyAttribute("A", Operator.PLUS, 10);

        Assert.assertEquals(clonedNpcAttributes.getNpcAttributes().get("A"), new Integer(10));
    }

}