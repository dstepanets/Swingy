package unit.swingy.controller;

import lombok.Setter;
import lombok.Getter;

import javax.swing.*;
import javax.validation.constraints.NotNull;

import unit.swingy.model.Map;
import unit.swingy.model.MapTile;
import unit.swingy.model.artifacts.AArtifact;
import unit.swingy.model.artifacts.Armor;
import unit.swingy.model.artifacts.Helm;
import unit.swingy.model.artifacts.Weapon;
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
		System.out.println(">> Exiting the game...");
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

	public void battle(final int dice) {

//		create worker thread in order to make pauses during battle rounds but don't freeze the GUI
		SwingWorker sw = new SwingWorker() {

			@Override
			protected Void doInBackground() throws Exception {
				do {
//					calculate damage
					int eDamage = enemy.takeDamage(hero, dice);
					int hDamage = hero.takeDamage(enemy, 0);
//					update UI
					if (guiMode) gui.battleRound(eDamage, hDamage);
					else  console.battleRound(eDamage, hDamage);
//					delay between moves
					Thread.sleep(500);
				} while ((hero.getHp() > 0) && (enemy.getHp() > 0));
				return null;
			}

//			this method is called when the background thread finishes execution
			@Override
			protected void done() {

				boolean victory = ((enemy.getHp() <= 0) && (hero.getHp() > 0)) ? true : false;
				System.out.println("## Victory: " + victory);

				int expReward = 0;
				if (victory) {

					expReward = hero.gainExp(enemy);
					hero.heal();

//					remove an enemy from the map
					grid[ny][nx].setEnemy(null);

//					update hero in the DataBase
					db.updateHero(hero);

//					move to the new location
					grid[y][x].setHero(null);
					grid[ny][nx].setHero(hero);
					y = ny; x = nx;

//					battle outcome
					if (!guiMode) console.winBattle(expReward);
				} else if (!guiMode) youDie();
//				delay GUI win and die methods until the battle window is closed
				if (guiMode) gui.enableExitBattle(expReward);
			}
		};
//		executes the swingworker on worker thread
		sw.execute();
	}


	public void youDie() {

		String msg = "Unfortunately, you died in the sleep choked with your tongue\nwhile being impossibly intoxicated.";

		if (isGuiMode()) gui.youDie(msg);
		else console.youDie(msg);

		startGame();
	}

	public AArtifact dropArtifact() {

		AArtifact artifact = null;
		Random rand =  new Random();

		if (enemy.getLevel() >= hero.getLevel() && rand.nextBoolean()) {
			int i = rand.nextInt(3);
			switch (i) {
				case 0:
					artifact = new Weapon(enemy.getLevel() + 1);
					break;
				case 1:
					artifact = new Armor(enemy.getLevel() + 1);
					break;
				case 2:
					artifact = new Helm(enemy.getLevel() + 1);
					break;
			}
		}

//		remove enemy from the left panel
		enemy = null;

		return artifact;
	}



}


