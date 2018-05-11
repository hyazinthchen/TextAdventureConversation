package npcDialogue.model;

public class Modification {
    private final String npcAttribute;
    private final Operator operator;
    private int value;

    public Modification(final String npcAttribute, final Operator operator, final int value) {
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
