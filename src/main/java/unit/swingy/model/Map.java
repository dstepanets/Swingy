package unit.swingy.model;

import lombok.Getter;
import unit.swingy.controller.Game;
import unit.swingy.model.characters.Enemy;
import unit.swingy.model.characters.Hero;

import java.util.Random;

public class Map {

	@Getter	private int size;
	@Getter	private MapTile[][] grid;

	private int maxEnemies;
	private int enemies;
	private int maxObstacles;
	private int obstacles;

	Random rand;

	public Map(Hero hero) {
		int lvl = hero.getLevel();
		size = (lvl - 1) * 5 + 10 - (lvl % 2);
		System.out.println(">> Map size: " + size);
		grid = new MapTile[size][size];

		rand = new Random();

		initMap();
	}

	private void initMap() {

		for (int y = 0; y < size; y++) {
			for (int x = 0; x < size; x++) {
				grid[y][x] = new MapTile();
			}
		}

		maxEnemies = size * size / 3;
		enemies = 0;
		maxObstacles = size * size / 4;
		obstacles = 0;

//		TODO: set obstacles
		putHero();
		putObstacles();
		putEnemies();

	}

	private void putHero() {
//		put the hero in the center of the grid
		int pos = (size - 1) / 2;
		Game g = Game.getInstance();
		g.setY(pos);
		g.setX(pos);
		grid[pos][pos].setHero(g.getHero());
		grid[pos][pos].setExplored(true);
	}

	private void putObstacles() {

		String[] terrains = {"mountains", "tree", "water", "Ukraine"};

		while (obstacles < maxObstacles) {
			int index = rand.nextInt(size * size);
			int x = index % size;
			int y = index / size;
			if (grid[y][x].getObstacle() == null && grid[y][x].getHero() == null) {
//				put random string from array
				grid[y][x].setObstacle(terrains[rand.nextInt(terrains.length)]);
				obstacles++;
			}
		}
	}


	private void putEnemies() {

//		generate enemies on random free tiles
		while (enemies < maxEnemies) {
			int index = rand.nextInt(size * size);
			int x = index % size;
			int y = index / size;
			if (grid[y][x].getObstacle() == null && grid[y][x].getEnemy() == null && grid[y][x].getHero() == null) {
				grid[y][x].setEnemy(new Enemy());
				enemies++;
			}
		}

	}



//	for debugging
	public void printMapTiles() {

		for (int y = 0; y < size; y++) {
			for (int x = 0; x < size; x++) {
				System.out.println("Y: " + y + "; X: " + x);
				System.out.println("obstacle=" + grid[y][x].getObstacle());
				System.out.println("explored=" + grid[y][x].isExplored());
				System.out.println("hero=" + grid[y][x].getHero());
				System.out.println("enemy=" + grid[y][x].getEnemy());
				System.out.println("----------");
			}
		}

	}



}
