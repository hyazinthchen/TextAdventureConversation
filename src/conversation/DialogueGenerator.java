package conversation;

import java.util.ArrayList;

public class DialogueGenerator {
    public DialogueState generateDialogue() {

        DialogueState dialogueState1 = new DialogueState("You don't look like you're from around here.");
        DialogueState dialogueState2 = new DialogueState("Oh really? Then you must know Mr. Bowler.");
        DialogueState dialogueState3 = new DialogueState("Newton, eh? I heard there's trouble brewing down there.");
        DialogueState dialogueState4 = new DialogueState("You liar! There ain't no Mr. Bowler. I made him up!");
        DialogueState dialogueState5 = new DialogueState(
                "Don't you worry about it. Say do you have something to eat? I'm starving.");

        ArrayList<PlayerOption> playerOptions1 = new ArrayList<PlayerOption>();
        playerOptions1.add(new PlayerOption("I've lived here all my life!", dialogueState1, dialogueState2));
        playerOptions1.add(new PlayerOption("I came here from Newton.", dialogueState1, dialogueState3));
        playerOptions1.add(new PlayerOption("What?", dialogueState1, dialogueState1));
        dialogueState1.addPlayerOptions(playerOptions1);

        ArrayList<PlayerOption> playerOptions2 = new ArrayList<PlayerOption>();
        playerOptions2.add(new PlayerOption("Mr. Bowler is a good friend of mine!", dialogueState2, dialogueState4));
        playerOptions2.add(new PlayerOption("Who?", dialogueState2, dialogueState5));
        dialogueState2.addPlayerOptions(playerOptions2);

        ArrayList<PlayerOption> playerOptions3 = new ArrayList<PlayerOption>();
        playerOptions3.add(new PlayerOption("I haven't heard about any trouble.", dialogueState3, dialogueState5));
        playerOptions3.add(
                new PlayerOption("Did I say Newton? I'm actually from Springville.", dialogueState3, dialogueState2));
        dialogueState3.addPlayerOptions(playerOptions3);

        ArrayList<PlayerOption> playerOptions4 = new ArrayList<PlayerOption>();
        playerOptions4.add(new PlayerOption("Restart", dialogueState5, dialogueState1));
        dialogueState5.addPlayerOptions(playerOptions4);

        return dialogueState1;
    }

}
