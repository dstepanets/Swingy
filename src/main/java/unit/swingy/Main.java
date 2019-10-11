package unit.swingy;

public class Main {

	static boolean guiMode;

	public static void printUsage(){
		System.out.println("Choose the game mode by providing an argument.");
		System.out.println("\t$java -jar swingy.jar console\nor\n\t$java -jar swingy.jar gui");
	}

	public static void main(String[] args) {

		if (args.length == 1) {
			switch (args[0]) {
				case "gui":
					guiMode = true;
					break;
				case "console":
					guiMode = false;
					break;
				default:
					printUsage();
			}
		} else {
			printUsage();
		}

		System.out.println(guiMode);
	}


}
