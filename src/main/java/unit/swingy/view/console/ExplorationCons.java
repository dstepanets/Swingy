package unit.swingy.view.console;

import unit.swingy.controller.Game;
import unit.swingy.model.Map;
import unit.swingy.model.MapTile;
import unit.swingy.model.artifacts.AArtifact;
import unit.swingy.model.characters.Enemy;
import unit.swingy.model.characters.Hero;
import unit.swingy.view.IExploration;

import java.util.Random;
import java.util.Scanner;

public class ExplorationCons implements IExploration {

	private Game game;
	private Scanner scan;

	public ExplorationCons() {
		game = Game.getInstance();
		scan = new Scanner(System.in);
	}


/*	----------------------------- UTILS ---------------------------- */

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

/*	----------------------------- CONTROLS ---------------------------- */

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


/*	----------------------------- EXPLORATION ---------------------------- */


	public void printExplorationPage() {
		do {
			updateMap();
			scanCommands();
		} while (!game.isGuiMode());
	}

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
						if (grid[y][x].getObstacle() == "Ukraine") System.out.print("U");
						else System.out.print("X");
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

	private void printHero() {
		Hero hero = game.getHero();
		System.out.println("\n* * * * * * * * * * * HERO * * * * * * * * * * * * *");
		System.out.println(hero.getName() + "\t" + hero.getClas().getClassName() +
							"\t(" + hero.getLevel() + " level)\t Exp: " + hero.getExp() + "/" + hero.getExpToLevelUp());
		System.out.println("HP: " + hero.getHp() + "/" + hero.getBaseHp() + "\t" + "Attack: " + hero.getAttack() +
							"\t" + "Defence: " + hero.getDefence());
		System.out.println("Weapon:\t\t" + hero.getWeapon().getName() + " (Attack +" + hero.getBonusAttack() + ")");
		System.out.println("Armor:\t\t" + hero.getArmor().getName() + " (Defence +" + hero.getBonusDefence() + ")");
		System.out.println("Helm:\t\t" + hero.getHelm().getName() + " (HP +" + hero.getBonusHp() + ")");
		System.out.println("* * * * * * * * * * * * *  * * * * * * * * * * * * *");
	}


/*	------------------------------- BATTLE -------------------------------- */

	public void fightOrFlee(Enemy enemy) {

		updateMap();

		System.out.println("\nYou encounter a " + enemy.getClas().getClassName() + " (Level " + enemy.getLevel() + ")");
		System.out.println("[HP: " + enemy.getHp() + " | Attack: " + enemy.getAttack() + " | Defence: " + enemy.getDefence() + "]");
		System.out.println("\"" + enemy.getClas().getDescription() + "\"");
		System.out.print("\nFight it bravely? (Yes)\n"  +
							"Or try to run away like a coward? (No)\n" +
							"Yes/No:> ");

		if (scanYesOrNo()) {
			System.out.println("You rush into the initBattle!");
			game.initBattle();
		} else {
			if (game.tryToFlee()) {
				game.escapeBattle();
			} else {
				System.out.println("Sadly, your running is so sloooow...");
				game.initBattle();
			}
		}
	}

	public void escapeBattle(String msg) {
		System.out.println(msg);
	}

	public void initBattle() {
		System.out.println("Press Enter to roll the dice. Strength of your attacks depends on the result.");

		scan.nextLine();
		int diceNum = new Random().nextInt(6) + 1;
		System.out.print("You rolled " + diceNum);
		if (diceNum > 3)
			System.out.println(". Your attacks are stronger.");
		else
			System.out.println(". Your attacks are weaker.");

		game.battle(diceNum);
	}

	public void battleRound(int eDamage, int hDamage) {
		String msg = "[E] " + game.getEnemy().getClas().getClassName() + " takes " + eDamage + " damage.";
		System.out.println(msg);
		msg = "[@] " + game.getHero().getName() + " takes " + hDamage + " damage.";
		System.out.println(msg);
	}

	public void winBattle(int expReward) {
		System.out.println("Glory to the victor! And " + expReward + " EXP!");
		dropArtifact();
	}

	private void dropArtifact() {

		AArtifact art = game.dropArtifact();

		if (art != null) {

			System.out.println(game.generateArtifactMessage(art));
			System.out.print("Want to equip? Yes/No:>");

			if (scanYesOrNo())
				game.equipArtifact(art);
		}
	}

	public void winMap(String msg, int expReward) {
		System.out.println("The edge of the world! You earned " + expReward + " EXP.");
		System.out.println(msg);
	}

	public void youDie(String msg, String msg2) {

		System.out.println(msg);
		System.out.println(msg2);
	}
}
