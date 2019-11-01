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
		console = new ChooseHeroCons(db, builder);
//		gui = new ChooseHeroGui();
	}

	public void chooseHero() {

			Hero hero = null;

			if (game.isGuiMode()) {
				gui = new ChooseHeroGui();
				gui.chooseHero();
				
//				TODO Try multithreading tools like wait() and notify() instead of the loop
//				wait till user has chosen a hero
				while (game.getHero() == null) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			} else {
				do {
					hero = console.chooseHero();
					if (hero != null) {
						game.setHero(hero);
					} else {
						builder.setUpNewHero(console.getNewHeroName(), console.getNewHeroClas());
						db.addHero(builder.getHero());
					}
				} while (game.getHero() == null);
			}

		db.closeConnection();
	}
}
