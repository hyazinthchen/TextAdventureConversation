package npcDialogue.model;

//TODO: use it or throw it away!
public class ParsingException extends Exception {
    public ParsingException(String s) {
        s = "Invalid yaml structure.";
    }
}
