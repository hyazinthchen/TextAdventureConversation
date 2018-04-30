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
    private NpcAttributes generateTestNpcAttributes(String key, Object value) {
        NpcAttributes testNpcAttributes = new NpcAttributes();
        testNpcAttributes.addAttribute(key, value);
        return testNpcAttributes;
    }

    @Test
    public void testModifyEntryByBoolean() {
        NpcAttributes attributes = generateTestNpcAttributes("A", false);
        attributes.modifyAttribute("A", true);
        assertEquals(true, attributes.getNpcAttributes().get("A"));
    }

    @Test
    public void testModifyEntryByInteger() {
        NpcAttributes attributes = generateTestNpcAttributes("A", 50);
        attributes.modifyAttribute("A", 60);
        assertEquals(60, attributes.getNpcAttributes().get("A"));
    }

    @Test
    public void testModifyEntryByString() {
        NpcAttributes attributes = generateTestNpcAttributes("A", "B");
        attributes.modifyAttribute("A", "C");
        assertEquals("C", attributes.getNpcAttributes().get("A"));
    }

    @Test
    public void testModifyEntryByAddition() {
        NpcAttributes attributes = generateTestNpcAttributes("A", 50);
        attributes.modifyAttribute("A", "+10");
        assertEquals(60, attributes.getNpcAttributes().get("A"));
    }

    @Test
    public void testModifyEntryBySubtraction() {
        NpcAttributes attributes = generateTestNpcAttributes("A", 50);
        attributes.modifyAttribute("A", "-10");
        assertEquals(40, attributes.getNpcAttributes().get("A"));
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testModifyEntryByMismatchingTypes() throws IllegalArgumentException {
        NpcAttributes attributes = generateTestNpcAttributes("A", 50);

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Attribute A is of type class java.lang.Integer and can't be changed to class java.lang.Boolean");
        attributes.modifyAttribute("A", false);
    }

    @Test
    public void testContain() {
        NpcAttributes testNpcAttributes = new NpcAttributes();
        testNpcAttributes.addAttribute("A", 50);
        testNpcAttributes.addAttribute("B", false);

        NpcAction actionX = new NpcAction(Role.NPC, "X", "X");
        actionX.addActionCondition("A", 50);

        assertTrue(testNpcAttributes.fulfill(actionX.getActionConditions().entrySet()));
    }

    @Test
    public void testContain_singleMatch() {
        NpcAttributes testNpcAttributes = new NpcAttributes();
        testNpcAttributes.addAttribute("A", 10);

        Set<Map.Entry<String, Object>> conditionSet = new HashSet<>();
        conditionSet.add(new AbstractMap.SimpleEntry<>("A", 10));

        assertTrue(testNpcAttributes.fulfill(conditionSet));
    }

    @Test
    public void testContain_singleMismatch() {
        NpcAttributes testNpcAttributes = new NpcAttributes();
        testNpcAttributes.addAttribute("A", 10);

        Set<Map.Entry<String, Object>> conditionSet = new HashSet<>();
        conditionSet.add(new AbstractMap.SimpleEntry<>("A", 5));

        assertFalse(testNpcAttributes.fulfill(conditionSet));
    }

    @Test
    public void testCopy() {
        NpcAttributes testNpcAttributes = new NpcAttributes();
        testNpcAttributes.addAttribute("A", 10);

        NpcAttributes clonedNpcAttributes = testNpcAttributes.copy();
        testNpcAttributes.modifyAttribute("A", 20);

        Assert.assertEquals(clonedNpcAttributes.getNpcAttributes().get("A"), 10);
    }

}