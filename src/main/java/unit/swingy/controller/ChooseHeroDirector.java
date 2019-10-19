package unit.swingy.controller;

import unit.swingy.model.characters.HeroBuilder;
import unit.swingy.view.console.ChooseHeroCons;

public class ChooseHeroDirector {

	private Game game;
	HeroBuilder builder;

	public ChooseHeroDirector() {
		game = Game.getInstance();
		builder = new HeroBuilder();
		builder.reset();
	}

	public void setUpHero() {

		if (game.isGuiMode()) {

		} else {
			ChooseHeroCons console = new ChooseHeroCons();
			builder.setUpNewHero(console.getNewHeroName(), console.getNewHeroClas());

		}

		game.setHero(builder.getHero());
	}

}
