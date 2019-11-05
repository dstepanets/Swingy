package unit.swingy.model.characters;

import lombok.Setter;

public class HeroBuilder {

	@Setter
	private Hero hero;

	public HeroBuilder() {
		reset();
	}

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
		hero.setHp(c.getMaxHp());
		hero.setAttack(c.getAttack());
		hero.setDefence(c.getDefence());
	}

	public void setId(int id) {
		hero.setId(id);
	}

	public void setName(String name) {
		hero.setName(name);
	}

	public void setClas(String classStr) {
		HeroClass c = HeroClass.valueOf(classStr.toUpperCase());
		hero.setClas(c);
	}

	public void setLevel(int l) {
		hero.setLevel(l);
	}

	public void setExp(int exp) {
		hero.setExp(exp);
	}

	public void setHp(int maxHp) {
		hero.setMaxHp(maxHp);
		hero.heal();
	}

	public void setAttack(int a) {
		hero.setAttack(a);
	}

	public void setDefence(int d) {
		hero.setDefence(d);
	}

	public void setWeapon(String w) {

	}

	public void setArmor(String a) {

	}

	public void setHelm(String h) {

	}

}
