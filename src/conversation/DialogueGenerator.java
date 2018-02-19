package conversation;

public class DialogueGenerator {
    public DialogueState generateDialogue() {

        DialogueState dialogueState1 = new DialogueState("You don't look like you're from around here.");
        DialogueState dialogueState2 = new DialogueState("Oh really? Then you must know Mr. Bowler.");
        DialogueState dialogueState3 = new DialogueState("Newton, eh? I heard there's trouble brewing down there.");
        DialogueState dialogueState4 = new DialogueState("You liar! There ain't no Mr. Bowler. I made him up!");
        DialogueState dialogueState5 = new DialogueState("Don't you worry about it. Say do you have something to eat? I'm starving.");

        dialogueState1.addPlayerOption("I've lived here...", "I've lived here all my life!", dialogueState2, true, false);
        dialogueState1.addPlayerOption("I came here from...", "I came here from Newton.", dialogueState3, true, false);
        dialogueState1.addPlayerOption("What?", "What?", dialogueState1, true, true);

        dialogueState2.addPlayerOption("Mr. Bowler is...", "Mr. Bowler is a good friend of mine!", dialogueState4, true, false);
        dialogueState2.addPlayerOption("Who?", "Who?", dialogueState5, true, false);

        dialogueState3.addPlayerOption("I haven't heard...", "I haven't heard about any trouble.", dialogueState5, true, false);
        dialogueState3.addPlayerOption("Did I saw Newton?", "Did I say Newton? I'm actually from Springville.", dialogueState2, true, false);

        dialogueState4.addPlayerOption("Restart", "Restart", dialogueState1, true, true);

        return dialogueState1;
    }

}
