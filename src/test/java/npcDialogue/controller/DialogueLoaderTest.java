package npcDialogue.controller;

import npcDialogue.model.NpcDialogueData;
import npcDialogue.model.NpcTraits;

import java.util.LinkedHashMap;

import static junit.framework.TestCase.assertEquals;

public class DialogueLoaderTest {

    public LinkedHashMap generateTestData() {
        LinkedHashMap npcActions = new LinkedHashMap();
        npcActions.put("welcome", "Hello and welcome!");
        npcActions.put("smallTalk", "The weather is nice today.");

        LinkedHashMap playerActions = new LinkedHashMap();
        npcActions.put("greet", "Hello!");
        npcActions.put("agree", "Yes it is.");

        LinkedHashMap testActionGraph = new LinkedHashMap();
        testActionGraph.put("entryPoint", "welcome");
        testActionGraph.put("npcActions", npcActions);
        testActionGraph.put("playerActions", playerActions);

        LinkedHashMap npcData = new LinkedHashMap();
        npcData.put("reputation", 50);
        npcData.put("questCompleted", false);

        LinkedHashMap testData = new LinkedHashMap();
        testData.put("npcData", npcData);
        testData.put("actionGraph", testActionGraph);

        return testData;
    }

    @org.junit.Test
    public void testLoadNpcTraits() {
        LinkedHashMap testData = generateTestData();

        DialogueLoader dialogueLoader = new DialogueLoader();
        NpcTraits npcTraits = dialogueLoader.loadNpcTraits(testData);

        assertEquals(50, npcTraits.getTraits().get("reputation"));
        assertEquals(false, npcTraits.getTraits().get("questCompleted"));
    }

    @org.junit.Test
    public void testLoadNpcDialogue() {
        NpcTraits testNpcTraits = new NpcTraits();
        testNpcTraits.addDataEntry("reputation", 50);
        testNpcTraits.addDataEntry("questCompleted", false);

        LinkedHashMap testData = generateTestData();

        DialogueLoader dialogueLoader = new DialogueLoader();
        NpcDialogueData npcDialogueData = dialogueLoader.loadNpcDialogue(testData, testNpcTraits);

        assertEquals(50, npcDialogueData.getNpcTraits().getTraits().get("reputation"));
        assertEquals(false, npcDialogueData.getNpcTraits().getTraits().get("questCompleted"));

        //TODO: check if action objects match testData
    }
}