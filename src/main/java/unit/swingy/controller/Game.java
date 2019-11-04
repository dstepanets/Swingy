package unit.swingy.controller;

import lombok.AccessLevel;
import lombok.Setter;
import lombok.Getter;
import javax.validation.constraints.NotNull;

import unit.swingy.model.Map;
import unit.swingy.model.MapTile;
import unit.swingy.model.characters.Hero;
import unit.swingy.view.console.ExplorationCons;

@Getter @Setter
public class Game {

	private static Game instance;
	@Setter(AccessLevel.NONE) private boolean guiMode;
	private ExplorationCons console;

	@NotNull private Hero hero;
	@NotNull private Map map;
//	player's coordinates on the map
	private int y;
	private int x;
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
		map = new Map(hero);
		map.printMapTiles();
		console = new ExplorationCons();

		do {
			if (guiMode) {

			} else {
				console.printExplorationPage();
			}
		} while (!won && !lost);

	}

	public void moveHero(char direction) {

		MapTile tab[][] = map.getTab();
//		new coordinates
		int nx = x, ny = y;

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
			won = true;
		} else {
			tab[ny][nx].setExplored(true);
			if (!tab[ny][nx].isObstacle()) {
//				move to new location if no obstacle is there
				tab[y][x].setHero(null);
				tab[ny][nx].setHero(hero);
//				if there is an enemy we need to remember old coordinates in case the player escapes
//				TODO: UNFINISHED


			}
		}



	}

}


