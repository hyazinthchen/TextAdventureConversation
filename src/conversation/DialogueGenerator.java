package conversation;

import java.util.ArrayList;

public class DialogueGenerator {
    public DialogueState generateDialogue() {

        DialogueState dialogueState1 = new DialogueState("You don't look like you're from around here.", new ArrayList<>());
        DialogueState dialogueState2 = new DialogueState("Oh really? Then you must know Mr. Bowler.", new ArrayList<>());
        DialogueState dialogueState3 = new DialogueState("Newton, eh? I heard there's trouble brewing down there.", new ArrayList<>());
        DialogueState dialogueState4 = new DialogueState("You liar! There ain't no Mr. Bowler. I made him up!", new ArrayList<>());
        DialogueState dialogueState5 = new DialogueState(
                "Don't you worry about it. Say do you have something to eat? I'm starving.", new ArrayList<>());

        dialogueState1.addPlayerOption("I've lived here all my life!", dialogueState1, dialogueState2, false, false, false);
        dialogueState1.addPlayerOption("I came here from Newton.", dialogueState1, dialogueState3, false, false, false);
        dialogueState1.addPlayerOption("What?", dialogueState1, dialogueState1, false, false, true);

        dialogueState2.addPlayerOption("Mr. Bowler is a good friend of mine!", dialogueState2, dialogueState4, false, false, false);
        dialogueState2.addPlayerOption("Who?", dialogueState2, dialogueState5, false, false, false);

        dialogueState3.addPlayerOption("I haven't heard about any trouble.", dialogueState3, dialogueState5, false, false, false);
        dialogueState3.addPlayerOption("Did I say Newton? I'm actually from Springville.", dialogueState3, dialogueState2, false, false, false);

        dialogueState4.addPlayerOption("Restart", dialogueState5, dialogueState1, false, false, true);

        return dialogueState1;
    }

}
