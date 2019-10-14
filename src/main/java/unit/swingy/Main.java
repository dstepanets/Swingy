package unit.swingy;

import unit.swingy.controller.CreateHero;
import unit.swingy.controller.Game;

public class Main {

	private static void printUsage() {
		System.out.println("Choose the game mode by providing an argument.");
		System.out.println("\t$java -jar swingy.jar console\nor\n\t$java -jar swingy.jar gui");
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
		if (guiMode)
			game.switchGameMode();
		System.out.println("GUI mode: " + game.isGuiMode());



		CreateHero director = new CreateHero();
		director.setUpHero();

		System.out.println("Hero's Name: " + game.getHero().getName());





//		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
//		Set<ConstraintViolation<String>> errors;
//		errors = validator.validate();
//		System.out.println(errors.toString());

	}


}
