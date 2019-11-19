package unit.swingy.controller;


public class Main {

	private static void printUsage() {
		System.out.println("Choose the game mode by providing an argument.");
		System.out.println("\t$java -jar swingy.jar console\nor\n\t$java -jar swingy.jar gui");
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
		System.out.println(">> GUI mode: " + game.isGuiMode());


//		set up the hero
		ChooseHeroDirector director = new ChooseHeroDirector();
		director.chooseHero();

		if (game.getHero() != null) {
			System.out.println(">> Hero's Name: " + game.getHero().getName());
			System.out.println(">> Hero's Class: " + game.getHero().getClas().getClassName());
		} else {
			System.out.println(">> No hero is selected.");
		}



//		TODO hide intro message from hibernate
//		HeroBuilder hb = new HeroBuilder();
//			hb.reset();
//			game.setHero(hb.getHero());


		AnnoValidation validate = new AnnoValidation();
		if (validate.validateHero(game.getHero())) {
			game.newMap(true);
		}

	}


}


