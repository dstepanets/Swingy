package unit.swingy.controller;

import unit.swingy.model.characters.DataBase;
import unit.swingy.model.characters.Hero;
import unit.swingy.model.characters.HeroBuilder;
import unit.swingy.view.console.ChooseHeroCons;

import java.util.ArrayList;

public class ChooseHeroDirector {

	private Game game;
	private HeroBuilder builder;
	private DataBase db;
	ChooseHeroCons console;

	public ChooseHeroDirector() {
		game = Game.getInstance();
		builder = new HeroBuilder();
		builder.reset();
		db = new DataBase();

		console = new ChooseHeroCons();
	}

	public void chooseHero() {
		db.connectToDB();

		ArrayList<Hero> heroesList = db.getHeroesList(builder);
		Hero hero = null;

		if (game.isGuiMode()) {

		} else {
			hero = console.chooseHero(heroesList);
		}

		if (hero == null) {
			hero = newHero();
		}

		db.closeConnection();
		game.setHero(hero);
	}

	private Hero newHero() {

		if (game.isGuiMode()) {

		} else {
			builder.setUpNewHero(console.getNewHeroName(), console.getNewHeroClas());
		}

		return builder.getHero();
	}

}
