package npcDialogue.controller;

import npcDialogue.model.Action;
import npcDialogue.model.NpcTraits;
import npcDialogue.view.ConsoleInputOutput;

public class DialogueNavigator {

    private final Action currentAction;

    public DialogueNavigator(NpcTraits npcTraits, Action startAction) {
        this.currentAction = startAction;
    }

    public void start(ConsoleInputOutput consoleInputOutput) { //TODO use generic IO interface ? switch to GUI easyly

        //TODO while not final state ...
        // - currentAction.onAction()       // for example printing stuff ...consoleInputOutput.print(....) bla
        // - currentAction.getTargetActions()   -> select ONE  (player->pick  // npc->random||conditional)
        // current = selected
    }
}
