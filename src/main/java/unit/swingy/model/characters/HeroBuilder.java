package unit.swingy.model.characters;

import lombok.Setter;
import unit.swingy.model.artifacts.AArtifact;
import unit.swingy.model.artifacts.Armor;
import unit.swingy.model.artifacts.Helm;
import unit.swingy.model.artifacts.Weapon;

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
		this.setHp(c.getBaseHp());
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
		HeroClass c = HeroClass.valueOf(classStr);
		hero.setClas(c);
	}

	public void setLevel(int l) {
		hero.setLevel(l);
		hero.setExpToLevelUp();
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

	public void setWeapon(int power) {
		AArtifact w;
		w = (power == 0) ? null : new Weapon(power - 1);
		hero.equipArtifact(w);
	}

	public void setArmor(int power) {
		AArtifact a;
		a = (power == 0) ? null : new Armor(power - 1);
		hero.equipArtifact(a);
	}

	public void setHelm(int power) {
		AArtifact h;
		h = (power == 0) ? null : new Helm(power - 1);
		hero.equipArtifact(h);
	}

}
