package unit.swingy.controller;

import unit.swingy.model.characters.DataBase;
import unit.swingy.model.characters.Hero;
import unit.swingy.model.characters.HeroBuilder;
import unit.swingy.view.console.ChooseHeroCons;
import unit.swingy.view.gui.ChooseHeroGui;

import java.util.ArrayList;

public class ChooseHeroDirector {

	private Game game;
	private HeroBuilder builder;
	private DataBase db;
	ChooseHeroCons console;
	ChooseHeroGui gui;

	public ChooseHeroDirector() {
		game = Game.getInstance();
		builder = new HeroBuilder();
		builder.reset();
		db = DataBase.getInstance();
		db.connectToDB();
		console = new ChooseHeroCons(db, builder);
//		gui = new ChooseHeroGui();
	}

	public void chooseHero() {


			Hero hero = null;

			if (game.isGuiMode()) {
				gui = new ChooseHeroGui();
				gui.chooseHero();
				while (game.getHero() == null) {}
//				temp
			} else {
				do {
					hero = console.chooseHero();
					if (hero != null) {
						game.setHero(hero);
					} else {
						hero = newHero();
						db.addHero(hero);
					}
				} while (game.getHero() == null);
			}



		db.closeConnection();
	}

	private Hero newHero() {

		if (game.isGuiMode()) {

		} else {
			builder.setUpNewHero(console.getNewHeroName(), console.getNewHeroClas());
		}

		return builder.getHero();
	}

}
