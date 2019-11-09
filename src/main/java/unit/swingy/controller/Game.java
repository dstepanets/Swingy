package unit.swingy.controller;

import lombok.AccessLevel;
import lombok.Setter;
import lombok.Getter;
import javax.validation.constraints.NotNull;

import unit.swingy.model.Map;
import unit.swingy.model.MapTile;
import unit.swingy.model.characters.DataBase;
import unit.swingy.model.characters.Enemy;
import unit.swingy.model.characters.Hero;
import unit.swingy.view.console.ExplorationCons;
import unit.swingy.view.gui.ExplorationGui;

import java.util.Random;

@Getter @Setter
public class Game {

	private static Game instance;
	private DataBase db = DataBase.getInstance();
	@Setter(AccessLevel.NONE) private boolean guiMode;
	private ExplorationCons console;
	private ExplorationGui gui;

	@NotNull private Hero hero;
	@NotNull private Map map;
	MapTile grid[][];
//	player's current coordinates on the map
	private int x, y;
//	new coordinates that player tries to move on
	private int nx, ny;

//	flags that control the game flow
	private boolean won;
	private boolean lost;


	public static Game getInstance() {
		if (instance == null)
			instance = new Game();
		return instance;
	}

	public void switchGameMode() {
		guiMode = guiMode ? false : true;
	}


	public void startGame() {

		resetMap();

		console = new ExplorationCons();
		gui = new ExplorationGui();


		while (true) {

			if (guiMode) {

			} else {
				console.printExplorationPage();
			}

			if (won || lost) {
				resetMap();
			}
		}
	}

	private void resetMap() {
		won = false;
		lost = false;
		hero.heal();
		map = new Map(hero);
		grid = map.getGrid();
//		map.printMapTiles();
	}

	public void exitGame() {
		db.closeConnection();
		System.exit(0);
	}

	public void moveHero(char direction) {

		System.out.println(">> You are moving to: " + direction);

		nx = x;
		ny = y;

		switch (direction) {
			case 'n':
				ny--;
				break;
			case 's':
				ny++;
				break;
			case 'w':
				nx--;
				break;
			case 'e':
				nx++;
				break;

		}

		if (nx < 0 || ny < 0 || nx >= map.getSize() || ny >= map.getSize()) {
			//	if an argument is null gives end-of-map exp reward
			hero.gainExp(null);
			won = true;
		} else {
			grid[ny][nx].setExplored(true);
			if (grid[ny][nx].getEnemy() != null) {
				fightOrFlee();
			}
			else if (grid[ny][nx].getObstacle() == null) {
//				move to new location if no obstacle is there
				grid[y][x].setHero(null);
				grid[ny][nx].setHero(hero);
				y = ny; x = nx;
			}
		}
// 		TODO create better map update
		if (guiMode) {
			gui.updateMap(x, y, nx, ny);
		}
	}

	private void fightOrFlee() {

		Enemy enemy = grid[ny][nx].getEnemy();
		boolean willFight = false;

		if (isGuiMode()) {

		} else {
			willFight = console.fightOrFlee(enemy);
		}

		if (!willFight) {
			if (tryToFlee())
				escapeBattle();
			else
				battle(true, enemy);
		} else
			battle(false, enemy);

	}

	private boolean tryToFlee() {
//		TODO: add greater chance for Normal Guy
		return new Random().nextBoolean();
	}

	private void escapeBattle() {
		if (guiMode) {

		} else {
			console.printMessage("You heroically escaped that filthy beast!");
		}
		ny = y; nx = x;
	}

	private void battle(boolean forced, Enemy enemy) {

		System.out.println("## You entered a battle.");
		boolean victory = false;

		if (guiMode) {

		} else {
			if (forced) console.printMessage("Sadly, you run so sloooowly...");
//			console.battle();
		}

		do {
			String s = enemy.takeDamage(hero);
				System.out.println("## " + s);
			s = hero.takeDamage(enemy);
				System.out.println("## " + s);
		} while ((hero.getHp() > 0) && (enemy.getHp() > 0));

		victory = (enemy.getHp() <= 0) ? true : false;
		System.out.println("## Victory: " + victory);

		if (victory) {
			hero.gainExp(enemy);
			hero.heal();
//			remove an enemy from the map
			grid[ny][nx].setEnemy(null);

//			update hero in the DataBase
			db.updateHero(hero);

//			move to the new location
			grid[y][x].setHero(null);
			grid[ny][nx].setHero(hero);
			y = ny; x = nx;
		} else {
			lost = true;
		}
	}



}


