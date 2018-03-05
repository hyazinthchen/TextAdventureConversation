package npcDialogue.model;

import org.junit.Test;

public class NpcTraitsTest {

    private NpcTraits generateTestNpcTraits(String key, Object value) {
        NpcTraits testNpcTraits = new NpcTraits();
        testNpcTraits.addDataEntry(key, value);
        return testNpcTraits;
    }

    @Test
    public void testModifyEntryByBoolean() {
        NpcTraits traits = generateTestNpcTraits("A", false);
        traits.modifyEntry("A", true);
    }

    @Test
    public void testModifyEntryByInteger() {
        NpcTraits traits = generateTestNpcTraits("A", 50);
        traits.modifyEntry("A", 60);
    }

    @Test
    public void testModifyEntryByString() {
        NpcTraits traits = generateTestNpcTraits("A", "B");
        traits.modifyEntry("A", "C");
    }

}