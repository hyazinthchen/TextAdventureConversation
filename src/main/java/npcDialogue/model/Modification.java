package npcDialogue.model;

public class Modification {
    private String npcAttribute;
    private Operator operator;
    private int value;

    public Modification(String npcAttribute, Operator operator, int value) {
        this.npcAttribute = npcAttribute;
        this.operator = operator;
        this.value = value;
    }

    public String getNpcAttribute() {
        return npcAttribute;
    }

    public Operator getOperator() {
        return operator;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
