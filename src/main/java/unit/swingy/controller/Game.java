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
import unit.swingy.model.characters.*;
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
	Random rand = new Random();

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

	public void switchView(boolean intro) {
		if (guiMode) {
			if (gui == null) gui = new ExplorationGui();
			gui.updateMap();
			gui.updateHeroPane();
			gui.updateEnemyPane();
			gui.getFrame().setVisible(true);
			if (intro) showIntro();
		} else {
			if (console == null) console = new ExplorationCons();
			if (gui != null) gui.getFrame().setVisible(false);
			if (intro) showIntro();
			console.printExplorationPage();
		}
	}


	void newMap(boolean intro) {

		System.out.println(">> newMap Thr: " + Thread.currentThread().getName());

//		new map
		hero.heal();
		map = new Map(hero);
		grid = map.getGrid();
		enemy = null;
//		map.printMapTiles();

		switchView(intro);

	}

	private void showIntro() {
		String msg = "You've been partying too hard that night. And got to a weird place. Try to explore it.";
		if (isGuiMode()) gui.showIntro(msg);
		else console.showIntro(msg);
	}


	public void exitGame() {
		System.out.println(">> Exiting the game...");
		db.closeConnection();
		System.exit(0);
	}

	public void moveHero(char direction) {

////		temp test CHEAT
//		while (hero.getLevel() < 10) {
//			hero.gainExp(null);
//		}

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
//				UFO has 50% chance to teleport hero to random location and damage
				if (grid[ny][nx].getEnemy().getClas() == EnemyClass.UFO && rand.nextBoolean())
					alienAbduction();
				else
					fightOrFlee();
			} else if (grid[ny][nx].getObstacle() == null) {
//				move to new location if no obstacle is there
				moveToNewCoords();
			} else if (grid[ny][nx].getObstacle() == "Ukraine")
				visitUkraine();
			if (guiMode) gui.updateMap();
		}
	}

	private void moveToNewCoords() {
		grid[y][x].setHero(null);
		grid[ny][nx].setHero(hero);
		y = ny; x = nx;

//		ParanoidAdroid occasionally gives depressed quotes
		if ((hero.getClas() == HeroClass.ParanoidAndroid) && (rand.nextInt(3) == 0)) {
			String msg = hero.getName() + ": \'" + MarvinPersonality.giveMarvinQuote() + "\'";
			if (guiMode) gui.printMessage(msg, TextStyle.italic);
			else console.printMessage("\n" + msg);
		}
	}

	private void alienAbduction() {
//		show UFO on map before teleportation
		if (isGuiMode()) gui.updateMap();
//		make sure new coords are free
		while (!(grid[ny][nx].getEnemy() == null && grid[ny][nx].getObstacle() == null)) {
			ny = rand.nextInt(map.getSize());
			nx = rand.nextInt(map.getSize());
		}
		grid[ny][nx].setExplored(true);
		int damage = hero.takeDamage(hero.getHp() / 2);
		moveToNewCoords();

//		display messages
		String msg = "You was abducted by aliens!\nYou're ashamed to speak about the ugly experiments " +
				"that they conducted on you...";
		String msg2 = "You lost " + damage + " HP and was thrown out in the unknown place.";
		if (isGuiMode()) {
			gui.showMessageDialog("ALIENS!!!", msg, new ImageIcon("src/main/resources/img/enemyIcons/UFO.png"));
			gui.printMessage(msg2, TextStyle.red);
			gui.updateHeroPane();
		}
		else {
			console.printMessage(msg);
			console.printMessage(msg2);
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
		db.updateHero(hero);

		String msg = "You have gracefully escaped this crazy nightmare!\nNow, will you finally wake up?";
		if (isGuiMode()) gui.winMap(msg, expReward);
		else console.winMap(msg, expReward);
		newMap(false);
	}

	private void fightOrFlee() {
		enemy = grid[ny][nx].getEnemy();

		if (isGuiMode()) gui.fightOrFlee(enemy);
		else console.fightOrFlee(enemy);
	}

	public boolean tryToFlee() {
//		Elephants are not agressive, unless attacked
		if (enemy.getClas() == EnemyClass.Elephant)
			return true;
//		SadCat has 70% chance to escape, instead of 50% for others
		if (hero.getClas() == HeroClass.SadCat)
			return (new Random().nextInt(10) > 2);
		return new Random().nextBoolean();
	}

	public void escapeBattle() {
		ny = y; nx = x;
		enemy = null;

		String msg = "You heroically escaped that filthy beast!";
		if (guiMode) gui.escapeBattle(msg);
		else console.escapeBattle(msg);
	}

	public void initBattle() {

//		sorceress weakens her enemies before battle
		if (hero.getClas() == HeroClass.Sorceress) {
			enemy.setAttack(enemy.getAttack() - enemy.getAttack() / 6);
			enemy.setDefence(enemy.getDefence() - enemy.getDefence() / 6);
			if (guiMode) gui.printMessage("Sorceress lowered the enemy's attack and defence", TextStyle.blue);
			else console.printMessage("Sorceress lowered the enemy's attack and defence");

//		Paranoid Android has 20% chance to have weaker attack in battle because of depression
		} else if (hero.getClas() == HeroClass.ParanoidAndroid) {
			MarvinPersonality.setSavedAttack(hero.getAttack());
			if (rand.nextInt(5) == 0) {
				int penalty = hero.getAttack() / 5;
				hero.setAttack(hero.getAttack() - penalty);
				String msg = "Paranoid Android is very depressed. Attack -" + penalty + " :(";
				if (guiMode) {
					gui.printMessage(msg, TextStyle.red);
					gui.updateHeroPane();
				}
				else console.printMessage(msg);
			}
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
			if (guiMode) gui.battleRound(eDamage, hDamage);
			else console.battleRound(eDamage, hDamage);

//			delay between moves lower if the damage is low to make battle faster
			int delay = ((eDamage < 3) || (hDamage < 3)) ? 100 : 300;
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while ((hero.getHp() > 0) && (enemy.getHp() > 0));
	}

	private void battleResult() {
//		restore Paranoid Android attack after penalty
		if (hero.getClas() == HeroClass.ParanoidAndroid) {
			hero.setAttack(MarvinPersonality.getSavedAttack());
		}

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
		if (guiMode) gui.printMessage(msg, TextStyle.blue);
		else console.printMessage(msg);

		if (hero.getLevel() >= 10) {
			winGame();
		}
	}

	public void youDie() {

//		reset exp
		hero.setExp(0);
		db.updateHero(hero);

		String msg = "YOU'RE DEAD, LOL :D (And lost your leveling progress)";
		String msg2 = "Unfortunately, you died in the sleep choked with your tongue" +
				"\nwhile being impossibly intoxicated.\nBut you shall finally rest in peace...";
		if (guiMode){
			gui.youDie(msg, msg2);
		}
		else {
			console.youDie(msg, msg2);
		}

		newMap(false);
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


	private void winGame() {
		String title = "Good morning, psychonaut!";
		String outro = "You wake up with a terrible nausea and headache.\n" +
				"You barely can think. And the thoughts are:\n" +
				"\'NO MORE DRUGS! Except for that cool little pills from the bartender...\n" +
				"They WERE fun!\'";
		if (guiMode) gui.winGame(title, outro);
		else console.winGame(title, outro);
	}


}


