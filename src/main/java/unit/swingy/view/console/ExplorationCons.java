package unit.swingy.view.console;

import unit.swingy.controller.Game;
import unit.swingy.model.Map;
import unit.swingy.model.MapTile;
import unit.swingy.model.characters.Enemy;

import java.util.Scanner;

public class ExplorationCons {

	private Game game;
	private Scanner scan;

	public ExplorationCons() {
		game = Game.getInstance();
		scan = new Scanner(System.in);
	}

	public void printExplorationPage() {

		printMap();
		scanCommands();
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

	public void printMessage(String msg) {
		System.out.println(msg);
	}

//	TODO: Hide unexplored tiles
	public void printMap() {
		Map map = game.getMap();
		MapTile[][] tab = map.getGrid();

		System.out.println("\n[- THE WORLD IS " + map.getSize() + "x" + map.getSize() + " -]");
//		print upper border
		for (int y = 0; y < map.getSize(); y++) {
			System.out.print("--");
		}
		System.out.println("-");
//		print grid
		for (int y = 0; y < map.getSize(); y++) {
			for (int x = 0; x < map.getSize(); x++) {
				if (x == 0) System.out.print("|");
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
		System.out.println("C - show controls");
	}

	public void printControls() {

		System.out.println("\n[- - - - - CONTROLS - - - - -]");
		System.out.println("(Type the command and press Enter)\n");

		System.out.println("  N  ");
		System.out.println("W   E	- move in a corresponding direction");
		System.out.println("  S  \n");

		System.out.println("L	- show map legend");
		System.out.println("H	- show your hero's stats");
		System.out.println("GUI	- switch to graphical interface");
		System.out.println("EXIT	- exit the game");
		System.out.println("[- - - - - - - - - - - - - - -]");

	}

	private void scanCommands() {

		boolean gotIt = false;

		do {
			System.out.print(":> ");
			String ln = scan.nextLine();

			switch (ln.toLowerCase()) {
				case "n":
				case "s":
				case "w":
				case "e":
					game.moveHero(ln.charAt(0));
					gotIt = true;
					break;
				case "c":
					printControls();
					gotIt = true;
					break;
				case "exit":
					System.exit(0);
					break;
			}

		} while (!gotIt);
	}

	public boolean fightOrFlee(Enemy enemy) {
		System.out.println("You encounter a " + enemy.getClas().getClassName() + " (Level " + enemy.getLevel() + ")");
		System.out.println("[HP: " + enemy.getHp() + " | Attack: " + enemy.getAttack() + " | Defence: " + enemy.getDefence() + "]");
		System.out.print("Fight it bravely? (Yes)\n"  +
							"Or try to run away like a coward? (No)\n" +
							"Yes/No:> ");
		return scanYesOrNo();
	}

	public void battle() {

	}

}
