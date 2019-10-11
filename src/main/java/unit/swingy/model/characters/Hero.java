package unit.swingy.model.characters;

import lombok.Getter;
import lombok.Setter;
import unit.swingy.model.artifacts.Helm;
import unit.swingy.model.artifacts.Weapon;
import unit.swingy.model.artifacts.Armor;

@Getter @Setter
public class Hero extends ACharacter {

	private String name;
	String clas;
	private int experience;

	private Weapon weapon;
	private Armor armor;
	private Helm helm;

}
