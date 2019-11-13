package unit.swingy.controller;

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
	private boolean guiMode;
	private ExplorationCons console;
	private ExplorationGui gui;

	@NotNull private Hero hero;
	private Enemy enemy;
	@NotNull private Map map;
	MapTile grid[][];
//	player's current coordinates on the map
	private int x, y;
//	new coordinates that player tries to move on
	private int nx, ny;

	public static Game getInstance() {
		if (instance == null)
			instance = new Game();
		return instance;
	}

	public void switchView() {
		if (guiMode) {
			if (gui == null) gui = new ExplorationGui();
			gui.updateMap();
			gui.updateHeroPane();
			gui.updateEnemyPane();
			gui.getFrame().setVisible(true);
		} else {
			if (console == null) console = new ExplorationCons();
			if (gui != null) gui.getFrame().setVisible(false);
			console.printExplorationPage();
		}
	}

	public void startGame() {

		System.out.println(">> startGame Thr: " + Thread.currentThread().getName());

		resetMap();
		switchView();

	}

	private void resetMap() {
		hero.heal();
		map = new Map(hero);
		grid = map.getGrid();
		enemy = null;
//		map.printMapTiles();
	}

//	TODO: Call this when GUI is closed
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

//		win the map if you have reached the end of it
		if (nx < 0 || ny < 0 || nx >= map.getSize() || ny >= map.getSize()) {
			winMap();
		} else {
			grid[ny][nx].setExplored(true);
			if (grid[ny][nx].getEnemy() != null) {
				fightOrFlee();
			} else if (grid[ny][nx].getObstacle() == null) {
//				move to new location if no obstacle is there
				grid[y][x].setHero(null);
				grid[ny][nx].setHero(hero);
				y = ny; x = nx;
			}
			if (guiMode) gui.updateMap();
		}
	}

	private void winMap() {
		//	if an argument is null gives end-of-map exp reward
		int expReward = hero.gainExp(null);

		String msg = "You have gracefully escaped this crazy nightmare!\nNow, will you finally wake up?";
		if (isGuiMode()) gui.winMap(msg, expReward);
		else console.winMap(msg, expReward);
		startGame();
	}

	private void fightOrFlee() {
		enemy = grid[ny][nx].getEnemy();

		if (isGuiMode()) gui.fightOrFlee(enemy);
		else console.fightOrFlee(enemy);
	}

	public boolean tryToFlee() {
//		TODO: add greater chance for Normal Guy
		return new Random().nextBoolean();
	}

	public void escapeBattle() {
		ny = y; nx = x;
		enemy = null;

		String msg = "You heroically escaped that filthy beast!";
		if (isGuiMode()) gui.escapeBattle(msg);
		else console.escapeBattle(msg);
	}

	public void initBattle() {

		if (guiMode) gui.initBattle();
		else console.initBattle();


	}

	public void battle(int dice) {

		boolean victory = false;

		do {
			int eDamage = enemy.takeDamage(hero);
			int hDamage = hero.takeDamage(enemy);

			if (guiMode) gui.battleRound(hDamage, eDamage);
			else  console.battleRound(hDamage, eDamage);

//			try {
//				Thread.sleep(500);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
		} while ((hero.getHp() > 0) && (enemy.getHp() > 0));

		victory = ((enemy.getHp() <= 0) && (hero.getHp() > 0)) ? true : false;
		System.out.println("## Victory: " + victory);

		if (victory) {
			int expReward = hero.gainExp(enemy);
			hero.heal();
//			remove an enemy from the map
			grid[ny][nx].setEnemy(null);
			enemy = null;

//			update hero in the DataBase
			db.updateHero(hero);

//			move to the new location
			grid[y][x].setHero(null);
			grid[ny][nx].setHero(hero);
			y = ny; x = nx;

			if (guiMode) gui.winBattle(expReward);
			else console.winBattle(expReward);

		} else youDie();
	}


	private void youDie() {

		String msg = "Unfortunately, you died in the sleep \nchoked with your tongue while being impossibly intoxicated.";

		if (isGuiMode()) gui.youDie(msg);
		else console.youDie(msg);

		startGame();
	}



}


