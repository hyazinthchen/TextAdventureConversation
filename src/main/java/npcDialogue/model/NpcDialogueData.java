package npcDialogue.model;

import npcDialogue.controller.DialogueNavigator;
import npcDialogue.view.ConsoleInputOutput;

/**
 * Contains the npcData and the startAction of the whole dialogue
 * Later: Save ActionGraph (stateful!) and npcTraits in DB
 */
public class NpcDialogueData {
    private final NpcTraits npcTraits;
    private final Action startAction; //TODO make stateless and save all states to npcTraits ?
    //private DialogueNavigator dialogueNavigator; //TODO keep this between npc conversions ?

    public NpcDialogueData(NpcTraits npcTraits, Action startAction) { //TODO merge with DialogueNavigator ?
        this.npcTraits = npcTraits;
        this.startAction = startAction;
    }

    /**
     * Starts a new DialogueNavigator.
     */
    public void start() {
        new DialogueNavigator(npcTraits, startAction).start(new ConsoleInputOutput());
    }

    public Action getStartAction() {
        return startAction;
    }

    public NpcTraits getNpcTraits() {
        return npcTraits;
    }
}
