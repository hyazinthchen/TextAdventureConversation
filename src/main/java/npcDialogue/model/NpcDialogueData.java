package npcDialogue.model;

import npcDialogue.controller.DialogueNavigator;
import npcDialogue.controller.DialogueValidator;
import npcDialogue.view.ConsoleReaderWriter;

/**
 * Contains the npcAttributes and the startAction of the whole dialogue.
 * Later: Save ActionGraph (stateful!) and npcAttributes in DB
 */
public class NpcDialogueData {
    private NpcAttributes npcAttributes;
    private final Action startAction; //TODO make stateless and save all states to npcAttributes ?
    private DialogueNavigator dialogueNavigator; //TODO keep this between npc conversions ?

    public NpcDialogueData(NpcAttributes npcAttributes, Action startAction) {
        this.npcAttributes = npcAttributes;
        this.startAction = startAction;
    }

    /**
     * Starts a new DialogueNavigator.
     */
    public void start() throws CloneNotSupportedException {
        if (new DialogueValidator(this).isValid()) {
            dialogueNavigator = new DialogueNavigator(npcAttributes, startAction);
            dialogueNavigator.navigate(new ConsoleReaderWriter());
        }
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
}
