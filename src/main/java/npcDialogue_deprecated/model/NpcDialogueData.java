package npcDialogue_deprecated.model;

import npcDialogue_deprecated.controller.DialogueNavigator;
import npcDialogue_deprecated.controller.DialogueValidator_old;
import npcDialogue_deprecated.view.ConsoleReaderWriter;

/**
 * Contains the npcAttributes and the startAction of the whole dialogue.
 * Later: Save graph of actions (stateful!) and npcAttributes in DB
 */
public class NpcDialogueData {
    private NpcAttributes npcAttributes;
    private final Action startAction;
    private DialogueNavigator dialogueNavigator;

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

    public DialogueNavigator getDialogueNavigator() {
        return dialogueNavigator;
    }

    public void setNpcAttributes(NpcAttributes npcAttributes) {
        this.npcAttributes = npcAttributes;
    }

    /**
     * Starts a new DialogueNavigator.
     */
    public void start() {
        if (new DialogueValidator_old(this).isValid()) {
            dialogueNavigator = new DialogueNavigator(npcAttributes, startAction);
            dialogueNavigator.navigate(new ConsoleReaderWriter());
        }
    }
}
