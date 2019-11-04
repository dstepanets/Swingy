package unit.swingy.model;

import lombok.Getter;
import unit.swingy.controller.Game;
import unit.swingy.model.characters.Enemy;
import unit.swingy.model.characters.Hero;

import java.util.Random;

public class Map {
	@Getter	private int size;
	@Getter	private MapTile[][] tab;

	private int maxEnemies;
	private int enemies;
	private int maxObstacles;
	private int obstacles;

	public Map(Hero hero) {
		int lvl = hero.getLevel();
		size = (lvl - 1) * 5 + 10 - (lvl % 2);
		System.out.println(">> Map size: " + size);
		tab = new MapTile[size][size];

		initMap();
	}

	private void initMap() {

		for (int y = 0; y < size; y++) {
			for (int x = 0; x < size; x++) {
				tab[y][x] = new MapTile();
			}
		}

		maxEnemies = size * size / 3;
		enemies = 0;
		maxObstacles = size * size / 4;
		obstacles = 0;

//		TODO: set terrain
//		TODO: set obstacles
		putHero();
		putEnemies();

	}

	private void putHero() {
//		put the hero in the center of the tab
		int pos = (size - 1) / 2;
		Game g = Game.getInstance();
		g.setY(pos);
		g.setX(pos);
		tab[pos][pos].setHero(g.getHero());
		tab[pos][pos].setExplored(true);
	}


	private void putEnemies() {

		Random rand = new Random();

//		generate enemies on random free tiles
		while (enemies < maxEnemies) {
			int index = rand.nextInt(size * size);
			int x = index % size;
			int y = index / size;
			if (!tab[y][x].isObstacle() && tab[y][x].getEnemy() == null && tab[y][x].getHero() == null) {
				tab[y][x].setEnemy(new Enemy());
				enemies++;
			}
		}

	}



//	for debugging
	public void printMapTiles() {

		for (int y = 0; y < size; y++) {
			for (int x = 0; x < size; x++) {
				System.out.println("Y: " + y + "; X: " + x);
				System.out.println("terrain=" + tab[y][x].getTerrain());
				System.out.println("obstacle=" + tab[y][x].isObstacle());
				System.out.println("explored=" + tab[y][x].isExplored());
				System.out.println("hero=" + tab[y][x].getHero());
				System.out.println("enemy=" + tab[y][x].getEnemy());
				System.out.println("----------");
			}
		}

	}



}
