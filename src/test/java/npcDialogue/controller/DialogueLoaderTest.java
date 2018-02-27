package npcDialogue.controller;

import npcDialogue.model.InvalidStateException;
import npcDialogue.model.NpcDialogueData;
import npcDialogue.model.NpcTraits;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;

import static junit.framework.TestCase.assertEquals;

public class DialogueLoaderTest {

    private File getFileFromClassPath(final String fileName) {
        //checknotnull with queo-commons-checks

        String absoluteFileName;
        if (fileName.startsWith("/")) {
            absoluteFileName = fileName;
        } else {
            absoluteFileName = "/" + fileName;
        }
        java.net.URL fileUrl = this.getClass().getResource(absoluteFileName);
        if (fileUrl == null) {
            throw new RuntimeException("file with name `" + absoluteFileName + "` not found in classpath");
        }
        try {
            Path filePath = Paths.get(fileUrl.toURI());
            return filePath.toFile();
        } catch (URISyntaxException e) {
            throw new RuntimeException("error while loading file `" + absoluteFileName + "` from  classpath");
        }
    }

    //TODO: delete this test method?
    @Test
    public void testLoad() throws FileNotFoundException, InvalidStateException {
        DialogueLoader dialogueLoader = new DialogueLoader();
        NpcDialogueData dialogueData = dialogueLoader.load(getFileFromClassPath("merchant1Dialogue.yml"));

        assertEquals(0, dialogueData.getNpcTraits().getTraits().get("bribePaid"));
        assertEquals(50, dialogueData.getNpcTraits().getTraits().get("reputation"));

        assertEquals("Welcome!", dialogueData.getStartAction().getActionText());
    }

    @Test
    public void testLoadNpcTraits() throws FileNotFoundException {
        DialogueLoader dialogueLoader = new DialogueLoader();
        InputStream inputStream = new FileInputStream(new File("src/test/resources/merchant1Dialogue.yml"));
        Yaml yaml = new Yaml();
        LinkedHashMap yamlDataMap = yaml.load(inputStream);

        NpcTraits npcTraits = dialogueLoader.loadNpcTraits(yamlDataMap);

        assertEquals(0, npcTraits.getTraits().get("bribePaid"));
        assertEquals(50, npcTraits.getTraits().get("reputation"));
    }

    @Test
    public void testLoadNpcDialogue() throws InvalidStateException, FileNotFoundException {
        DialogueLoader dialogueLoader = new DialogueLoader();
        InputStream inputStream = new FileInputStream(new File("src/test/resources/merchant1Dialogue.yml"));
        Yaml yaml = new Yaml();
        LinkedHashMap yamlDataMap = yaml.load(inputStream);

        NpcDialogueData npcDialogueData = dialogueLoader.loadNpcDialogue(yamlDataMap, new NpcTraits());

        assertEquals("Welcome!", npcDialogueData.getStartAction().getActionText());
    }
}