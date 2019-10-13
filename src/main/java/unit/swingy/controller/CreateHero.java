package unit.swingy.controller;

import unit.swingy.model.characters.HeroBuilder;
import unit.swingy.view.console.ChooseHeroC;

public class CreateHero {

	public void setUpHero() {

		Game game = Game.getInstance();

		HeroBuilder builder = new HeroBuilder();
		builder.reset();

		if (game.isGuiMode()) {

		} else {
			ChooseHeroC console = new ChooseHeroC();
			builder.setName(console.getName());

		}

		game.setHero(builder.getHero());
	}

}
