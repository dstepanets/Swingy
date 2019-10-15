package unit.swingy.controller;

import unit.swingy.model.characters.HeroBuilder;
import unit.swingy.view.console.ChooseHeroC;

public class CreateHero {

	private Game game = Game.getInstance();

	public void setUpHero() {

		HeroBuilder builder = new HeroBuilder();
		builder.reset();

		if (game.isGuiMode()) {

		} else {
			ChooseHeroC console = new ChooseHeroC();
			builder.setName(console.getName());
			builder.setClas(console.getClas());

		}

		game.setHero(builder.getHero());
	}

}
