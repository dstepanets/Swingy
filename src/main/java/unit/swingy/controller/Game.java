package unit.swingy.controller;

import lombok.AccessLevel;
import lombok.Setter;
import lombok.Getter;

import unit.swingy.model.characters.Hero;

@Getter @Setter
public class Game {

	private static Game instance = null;
	@Setter(AccessLevel.NONE) private boolean guiMode;
	private Hero hero;


	private Game() {
		guiMode = false;
		hero = null;
	}

	public static Game getInstance() {
		if (instance == null)
			instance = new Game();
		return instance;
	}

	public void switchGameMode() {
		guiMode = guiMode ? false : true;
	}

	public void setUpHero() {


	}
}


