package unit.swingy.controller;


public class Main {

	private static void printUsage() {
		System.out.println("Choose the game mode by providing an argument.");
		System.out.println("\t$java -jar swingy-1.0-jar-with-dependencies.jar console\nor");
		System.out.println("\t$java -jar swingy-1.0-jar-with-dependencies.jar gui");
		System.exit(1);
	}

	public static void main(String[] args) {

//		Chose game mode
		boolean guiMode = false;
		if (args.length == 1) {
			switch (args[0]) {
				case "gui":
					guiMode = true;
					break;
				case "console":
					break;
				default:
					printUsage();
			}
		} else {
			printUsage();
		}

		Game game = Game.getInstance();
		game.setGuiMode(guiMode);

//		set up the hero
		ChooseHeroDirector director = new ChooseHeroDirector();
		director.chooseHero();

//		validate annotation constrains
		AnnoValidation validate = new AnnoValidation();
		if (validate.validateHero(game.getHero())) {
			game.newMap(true);
		}

	}


}


