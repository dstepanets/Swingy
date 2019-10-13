package unit.swingy.model.characters;

import lombok.Getter;
import lombok.Setter;
import unit.swingy.model.artifacts.Helm;
import unit.swingy.model.artifacts.Weapon;
import unit.swingy.model.artifacts.Armor;

@Getter @Setter
public class Hero extends ACharacter {

	private String name;
	private HeroClass clas;
//	private int level;
	private int experience;

//	private int hp;
//	private int attack;
//	private int defence;

	private Weapon weapon;
	private Armor armor;
	private Helm helm;

	Hero() {
		level = 0;
		experience = 0;
	}
}
