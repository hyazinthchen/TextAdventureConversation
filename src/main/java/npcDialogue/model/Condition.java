package npcDialogue.model;

import java.util.Map;

public class Condition {
    private String npcAttribute;
    private RelationalOperator relationalOperator;
    private int value;

    public Condition(String npcAttribute, RelationalOperator relationalOperator, int value) {
        this.npcAttribute = npcAttribute;
        this.relationalOperator = relationalOperator;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean fulfills(Map<String, Integer> npcAttributes) {
        switch (this.relationalOperator){
            case LESS:
                return npcAttributes.get(npcAttribute) < value;
            case GREATER:
                return npcAttributes.get(npcAttribute) > value;
            case LESSEQUAL:
                return npcAttributes.get(npcAttribute) <= value;
            case GREATEREQUAL:
                return npcAttributes.get(npcAttribute) <= value;
            case EQUAL:
                return npcAttributes.get(npcAttribute) == value;
            case NOTEQUAL:
                return npcAttributes.get(npcAttribute) != value;
        }
        throw new IllegalArgumentException();
    }
}
