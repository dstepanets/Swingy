package unit.swingy.view.console;

import unit.swingy.controller.Game;
import unit.swingy.model.Map;
import unit.swingy.model.MapTile;

import java.util.Scanner;

public class ExplorationCons {

	private Game game;
	private Scanner scan;

	public ExplorationCons() {
		game = Game.getInstance();
		scan = new Scanner(System.in);
	}

	public void printExplorationPage() {

		printControls();
		printMap();
		scanInput();
	}



//	TODO: Hide unexplored tiles
	public void printMap() {
		Map map = game.getMap();
		MapTile[][] tab = map.getTab();

//		print upper border
		for (int y = 0; y < map.getSize(); y++) {
			System.out.print("--");
		}
		System.out.println("-");
//		print grid
		for (int y = 0; y < map.getSize(); y++) {
			for (int x = 0; x < map.getSize(); x++) {
				if (x == 0) {
					System.out.print("|");
				}
				if (tab[y][x].getHero() != null) {
					System.out.print("H");
				} else if (tab[y][x].getEnemy() != null) {
					System.out.print("E");
				} else
					System.out.print(".");
				System.out.print("|");
			}
			System.out.println();
		}

//		print lower border
		for (int y = 0; y < map.getSize(); y++) {
			System.out.print("--");
		}
		System.out.println("-");

	}

	public void printControls() {

		System.out.println("CONTROLS:");
		System.out.println("(Type the command and press Enter)\n");

		System.out.println("  W  ");
		System.out.println("A   D	- move in a corresponding direction");
		System.out.println("  S  \n");

		System.out.println("L		- show map legend");
		System.out.println("H		- show your hero's stats");
		System.out.println("GUI		- switch to graphical interface");
		System.out.println("EXIT	- exit the game");

	}

	private void scanInput() {

		do {
			String ln = scan.nextLine();

			switch (ln.toLowerCase()) {
				case "exit":
					System.exit(0);
					break;
			}

		} while (true);

	}

	private boolean scanYesOrNo() {
		while (true) {
			String answer = scan.nextLine();
			if (answer.equalsIgnoreCase("Y") || answer.equalsIgnoreCase("Yes")) {
				return true;
			} else if (answer.equalsIgnoreCase("N") || answer.equalsIgnoreCase("No")) {
				return false;
			}
		}
	}
}
