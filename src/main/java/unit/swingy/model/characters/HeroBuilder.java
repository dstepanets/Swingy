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

	public void setClas(HeroClass c) {
		hero.setClas(c);

		switch (c) {
			case REGULAR:
				buildRegular();
				break;
			case BERSERK:
				buildBerserk();
				break;
			case TANK:
				buildTank();
				break;
		}

	}

	private void buildRegular() {
		hero.setHp(100);
		hero.setAttack(10);
		hero.setDefence(5);
	}

	private void buildBerserk() {
		hero.setHp(80);
		hero.setAttack(14);
		hero.setDefence(4);
	}

	private void buildTank() {
		hero.setHp(120);
		hero.setAttack(8);
		hero.setDefence(6);
	}


}
