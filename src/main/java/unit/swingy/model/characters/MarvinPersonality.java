package unit.swingy.model.characters;

import java.util.Random;

public class MarvinPersonality {

	static private int savedAttack;

	static private final Random rand = new Random();

	private final static String[] quotes = {
			"Life? Don't talk to me about life.",
			"Here I am, brain the size of a planet, and they tell me to do that nonsense. Call that job satisfaction? 'Cos I don't.",
			"I think you ought to know I'm feeling very depressed.",
			"I won't enjoy it.",
			"It hates me.",
			"It gives me a headache just trying to think down to your level.",
			"You think you've got problems? What are you supposed to do if you are a manically depressed robot?",
			"There's only one life-form as intelligent as me within thirty parsecs of here and that's me.",
			"And then, of course, I've got this terrible pain in all the diodes down my left side.",
			"I have a million ideas. They all point to certain death.",
			"It's part of the shape of the Universe. I only have to talk to somebody and they begin to hate me.",
			"My capacity for happiness, you could fit into a matchbox without taking out the matches first"
	};

	public static String giveMarvinQuote() {
		return quotes[rand.nextInt(quotes.length)];
	}

	public static int getSavedAttack() {
		return savedAttack;
	}

	public static void setSavedAttack(int attack) {
		savedAttack = attack;
	}

}
