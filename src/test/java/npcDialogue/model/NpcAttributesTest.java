package npcDialogue.model;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

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
        testNpcAttributes.addDataEntry(key, value);
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

}