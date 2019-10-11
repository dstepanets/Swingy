package unit.swingy.model.characters;

import lombok.Setter;

public class HeroBuilder {

	@Setter private Hero hero;

	public void reset() {
		this.hero = new Hero();
	}

	public Hero getHero() {
		Hero readyHero = this.hero;
		reset();
		return readyHero;
	}

	public void setName(String name) {
		hero.setName(name);
	}

	public void setClas(String c) {
		hero.setClas(c);
	}


}
