package unit.swingy.controller;

import lombok.AccessLevel;
import lombok.Setter;
import lombok.Getter;

import javax.swing.*;
import javax.validation.constraints.NotNull;

import unit.swingy.model.Map;
import unit.swingy.model.MapTile;
import unit.swingy.model.characters.DataBase;
import unit.swingy.model.characters.Enemy;
import unit.swingy.model.characters.Hero;
import unit.swingy.view.console.ExplorationCons;
import unit.swingy.view.gui.ExplorationGui;

import java.lang.reflect.InvocationTargetException;
import java.util.Random;

@Getter @Setter
	public class Game {

	private static Game instance;
	private DataBase db = DataBase.getInstance();
	private boolean guiMode;
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
//	TODO: maybe no need in them
	private boolean won;
	private boolean lost;


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
		won = false;
		lost = false;
		hero.heal();
		map = new Map(hero);
		grid = map.getGrid();
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


		if (nx < 0 || ny < 0 || nx >= map.getSize() || ny >= map.getSize()) {
			//	if an argument is null gives end-of-map exp reward
			hero.gainExp(null);
			won = true;
			startGame();
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
// 		TODO create better map update


	}

	private void fightOrFlee() {

		final Enemy enemy = grid[ny][nx].getEnemy();

		if (isGuiMode()) {
			gui.fightOrFlee(enemy);
		} else {
			console.fightOrFlee(enemy);
		}


	}

	public boolean tryToFlee() {
//		TODO: add greater chance for Normal Guy
		return new Random().nextBoolean();
	}

	public void escapeBattle() {
		ny = y; nx = x;
	}

	public void battle(Enemy enemy) {

		System.out.println("## You entered a battle.");
		boolean victory = false;

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

			if (guiMode) {
				gui.updateMap();
				gui.updateHeroPane();
			}
		} else {
			lost = true;
			startGame();
		}


	}



}


