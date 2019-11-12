package unit.swingy.view.console;

import unit.swingy.controller.Game;
import unit.swingy.model.Map;
import unit.swingy.model.MapTile;
import unit.swingy.model.characters.Enemy;
import unit.swingy.model.characters.Hero;
import unit.swingy.view.IExploration;

import java.util.Scanner;

public class ExplorationCons implements IExploration {

	private Game game;
	private Scanner scan;

	public ExplorationCons() {
		game = Game.getInstance();
		scan = new Scanner(System.in);
	}

	public void printExplorationPage() {
		do {
			updateMap();
			scanCommands();
		} while (!game.isGuiMode());
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
		public void updateMap() {
		Map map = game.getMap();
		MapTile[][] grid = map.getGrid();

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

				if (grid[y][x].isExplored()) {
					if (grid[y][x].getObstacle() != null) {
						System.out.print("X");
					} else if (grid[y][x].getHero() != null) {
						System.out.print("@");
					} else if (grid[y][x].getEnemy() != null) {
						System.out.print("E");
					} else
						System.out.print(" ");
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

	private void printControls() {

		System.out.println("\n[- - - - - - CONTROLS - - - - - -]");
		System.out.println("(Type the command and press Enter)\n");

		System.out.println("  N  ");
		System.out.println("W   E	- move in a corresponding direction");
		System.out.println("  S  \n");

		System.out.println("L	- show map legend");
		System.out.println("H	- show your hero's stats");
		System.out.println("GUI	- switch to graphical interface");
		System.out.println("EXIT	- exit the game");
		System.out.println("[- - - - - - - -  - - - - - - - - -]");

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
					gotIt = true;
					game.moveHero(ln.charAt(0));
					break;
				case "c":
					gotIt = true;
					printControls();
					break;
				case "h":
					gotIt = true;
					printHero();
					break;
				case "gui":
					gotIt = true;
					game.setGuiMode(true);
					game.switchView();
					break;
				case "exit":
					gotIt = true;
					game.exitGame();
					break;
			}

		} while (!gotIt);
	}

	private void printHero() {
		Hero hero = game.getHero();
		System.out.println("\n* * * * * * * * * * * HERO * * * * * * * * * * * * *");
		System.out.println(hero.getName() + "\t" + hero.getClas().getClassName() +
							"\t(" + hero.getLevel() + " level)\t Exp: " + hero.getExp() + "/" + hero.getExpToLevelUp());
		System.out.println("HP: " + hero.getHp() + "/" + hero.getMaxHp() + "\t" + "Attack: " + hero.getAttack() +
							"\t" + "Defence: " + hero.getDefence());
		System.out.println("Weapon: " + hero.getWeapon());
		System.out.println("Armor: " + hero.getArmor());
		System.out.println("Helm: " + hero.getHelm());
		System.out.println("* * * * * * * * * * * * *  * * * * * * * * * * * * *");
	}

	public void fightOrFlee(Enemy enemy) {

		updateMap();

		System.out.println("You encounter a " + enemy.getClas().getClassName() + " (Level " + enemy.getLevel() + ")");
		System.out.println("[HP: " + enemy.getHp() + " | Attack: " + enemy.getAttack() + " | Defence: " + enemy.getDefence() + "]");
		System.out.print("Fight it bravely? (Yes)\n"  +
							"Or try to run away like a coward? (No)\n" +
							"Yes/No:> ");

		if (scanYesOrNo()) {
			printMessage("You rush into the battle!");
			game.battle(enemy);
		} else {
			if (game.tryToFlee()) {
				game.escapeBattle();
			} else {
				printMessage("Sadly, your running is so sloooow...");
				game.battle(enemy);
			}
		}
	}

	public void escapeBattle(String msg) {
		printMessage(msg);
	}

	public void battle() {

	}

	public void winBattle(int expReward) {
		printMessage("Glory to the victor! And " + expReward + " EXP!");
	}

	public void winMap(String msg, int expReward) {
		printMessage("The edge of the world! You earned " + expReward + " EXP.");
		printMessage(msg);
	}

	public void youDie(String msg) {
		printMessage(msg);
	}
}
