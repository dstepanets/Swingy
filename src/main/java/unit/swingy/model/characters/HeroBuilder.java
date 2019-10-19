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

	public void setUpNewHero(String name, HeroClass c) {
		hero.setName(name);
		hero.setClas(c);
		hero.setHp(c.getHp());
		hero.setAttack(c.getAttack());
		hero.setDefence(c.getDefence());
	}


}
