package npcDialogue.model;

import npcDialogue.controller.DialogueNavigator;
import npcDialogue.view.ConsoleInputOutput;

/**
 * Later: Save ActionGraph (stateful!) and npcData in DB
 */
public class NpcDialogueData {
    private final NpcData npcData;
    private final Action startAction; //TODO make stateless and save all states to npcData ?
    //private DialogueNavigator dialogueNavigator; //TODO keep this between npc conversions ?

    public NpcDialogueData(NpcData npcData, Action startAction) { //TODO merge with DialogueNavigator ?
        this.npcData = npcData;
        this.startAction = startAction;
    }

    public void start() {
        new DialogueNavigator(npcData, startAction).start(new ConsoleInputOutput());
    }
}
