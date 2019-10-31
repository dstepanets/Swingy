package unit.swingy.controller;

import lombok.AccessLevel;
import lombok.Setter;
import lombok.Getter;
import javax.validation.constraints.NotNull;

import unit.swingy.model.characters.Hero;

@Getter @Setter
public class Game {

	private static Game instance;
	@Setter(AccessLevel.NONE) private boolean guiMode;
	@NotNull private Hero hero = null;

	public static Game getInstance() {
		if (instance == null)
			instance = new Game();
		return instance;
	}

	public void switchGameMode() {
		guiMode = guiMode ? false : true;
	}

}


