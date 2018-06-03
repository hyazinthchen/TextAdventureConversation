package npcDialogue.model;

public class NpcDialogueData {
    private NpcAttributes npcAttributes;
    private final Action startAction;

    public NpcDialogueData(final NpcAttributes npcAttributes, final Action startAction) {
        this.npcAttributes = npcAttributes;
        this.startAction = startAction;
    }

    public Action getStartAction() {
        return startAction;
    }

    public NpcAttributes getNpcAttributes() {
        return npcAttributes;
    }

    public void setNpcAttributes(NpcAttributes npcAttributes) {
        this.npcAttributes = npcAttributes;
    }

}
