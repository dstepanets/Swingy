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
import unit.swingy.model.characters.HeroClass;
import unit.swingy.view.console.ExplorationCons;
import unit.swingy.view.gui.ExplorationGui;
import unit.swingy.view.gui.TextStyle;

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


//	TODO intro and outro
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
			} else if (grid[ny][nx].getObstacle() == "Ukraine")
				visitUkraine();
			if (guiMode) gui.updateMap();
		}
	}

//	if you visit Ukraine, you loose health :)
	private void visitUkraine() {
		int damage = hero.getHp() / 4;
		hero.takeDamage(damage);
		String msg = "On your visit to Ukraine you have lost " + damage + " HP. It's a harsh place, indeed!";
		if (isGuiMode()) {
			gui.printMessage(msg, TextStyle.red);
			gui.updateHeroPane();
		}
		else  console.printMessage(msg);
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
//		SadCat has 60% chance to escape, instead of 50% for others
		if (hero.getClas() == HeroClass.SadCat) {
			return (new Random().nextInt(10) > 3);
//		Elephants are not agressive, unless attacked
		} else if (enemy.getClas() == EnemyClass.Elephant)
			return true;
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

//		sorceress weakens her enemies before battle
		if (hero.getClas() == HeroClass.Sorceress) {
			enemy.setAttack(enemy.getAttack() - enemy.getAttack() / 6);
			enemy.setDefence(enemy.getDefence() - enemy.getDefence() / 6);
			if (guiMode) gui.printMessage("Sorceress lowered the enemy's attack and defence", TextStyle.blue);
			else console.printMessage("Sorceress lowered the enemy's attack and defence");
		}

		if (guiMode) gui.initBattle();
		else console.initBattle();

	}

	public void battle(final int dice) {

		if (guiMode) {
//			create worker thread to make pauses during battle rounds but not to freeze the GUI
			SwingWorker sw = new SwingWorker() {
				@Override
				protected Void doInBackground() {
					battleCycle(dice);
					return null;
				}
//				this method is called when the background thread finishes execution
				@Override
				protected void done() {
					battleResult();
				}
			};
//			executes the swingworker on worker thread
			sw.execute();
//		if text mode, execute the battle on the same thread
		} else {
			battleCycle(dice);
			battleResult();
		}
	}

	private void battleCycle(final int dice) {
		do {
//			calculate damage
			int eDamage = enemy.takeDamage(hero, dice);
			int hDamage = hero.takeDamage(enemy, 0);
//			update UI
			if (isGuiMode()) gui.battleRound(eDamage, hDamage);
			else console.battleRound(eDamage, hDamage);
//			delay between moves
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while ((hero.getHp() > 0) && (enemy.getHp() > 0));
	}

	private void battleResult() {
		boolean victory = ((enemy.getHp() <= 0) && (hero.getHp() > 0)) ? true : false;
		System.out.println("## Victory: " + victory);

		int expReward = 0;
		if (victory) {

			expReward = hero.gainExp(enemy);
			hero.heal();

//			remove an enemy from the map
			grid[ny][nx].setEnemy(null);

//			update hero in the DataBase
			db.updateHero(hero);

//			move to the new location
			grid[y][x].setHero(null);
			grid[ny][nx].setHero(hero);
			y = ny; x = nx;

//			battle outcome
			if (!guiMode) console.winBattle(expReward);
		} else if (!guiMode) youDie();
//		delay GUI win and die methods until the battle window is closed
		if (guiMode) gui.enableExitBattle(expReward);
	}

	public void levelUp() {
		String msg = "Yo, yo, yo! You have reached " + hero.getLevel() + " level!";
		if (isGuiMode()) gui.printMessage(msg, TextStyle.blue);
		else console.printMessage(msg);
	}

	public void youDie() {

//		reset exp
		hero.setExp(0);

		String msg = "YOU'RE DEAD, LOL :D (And lost your leveling progress)";
		String msg2 = "Unfortunately, you died in the sleep choked with your tongue\nwhile being impossibly intoxicated.";
		if (isGuiMode()){
			gui.youDie(msg, msg2);
		}
		else {
			console.youDie(msg, msg2);
		}

		startGame();
	}

	public AArtifact dropArtifact() {

		AArtifact artifact = null;
		Random rand =  new Random();

		if (enemy.getLevel() >= hero.getLevel() && rand.nextBoolean()) {
			int i = rand.nextInt(3);
			switch (i) {
				case 0:
					artifact = new Weapon(enemy.getLevel());
					break;
				case 1:
					artifact = new Armor(enemy.getLevel());
					break;
				case 2:
					artifact = new Helm(enemy.getLevel());
					break;
			}
		}

//		remove enemy from the left panel
		enemy = null;

		return artifact;
	}

	public String generateArtifactMessage(AArtifact art) {
		AArtifact.ArtifactType type = art.getType();
		StringBuilder msg = new StringBuilder("You have found " + type.toString() + ":\n" + art.getName() + " (");
		String msg2 = null;

		switch (type) {
			case WEAPON:
				msg.append("Attack +" + art.getPower() + ")\n");
				AArtifact w = hero.getWeapon();
				if (w != null)
					msg2 = "You have " + w.getName() + " (Attack +" + w.getPower() + ")";
				break;
			case ARMOR:
				msg.append("Defence +" + art.getPower() + ")\n");
				AArtifact a = hero.getArmor();
				if (a != null)
					msg2 = "You have " + a.getName() + " (Defence +" + a.getPower() + ")";
				break;
			case HELM:
				msg.append("HP +" + (art.getPower() * 10) + ")\n");
				AArtifact h = hero.getHelm();
				if (h != null)
					msg2 = "You have " + h.getName() + " (HP +" + (h.getPower() * 10) + ")";
				break;
		}

		if (msg2 == null)
			msg2 = "You don't have an artifact of this type.";

		msg.append(msg2);

		return msg.toString();
	}

	public void equipArtifact(AArtifact art) {
		hero.equipArtifact(art);
		db.updateHero(hero);
	}



}


