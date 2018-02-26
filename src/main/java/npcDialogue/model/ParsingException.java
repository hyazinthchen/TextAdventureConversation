package npcDialogue.model;

public class ParsingException extends Exception {
    public ParsingException(String s) {
        s = "Invalid yaml structure.";
    }
}
