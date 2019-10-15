package unit.swingy.model.characters;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import javax.validation.constraints.NotNull;

import unit.swingy.model.artifacts.Helm;
import unit.swingy.model.artifacts.Weapon;
import unit.swingy.model.artifacts.Armor;


@Getter @Setter
public class Hero extends ACharacter {

	@Range(min=1, max = 30, message = "Hero's name must be 1-30 character long")
	@NotBlank(message = "Hero's name can't be blank")
	private String name;
	@NotNull(message = "Hero must belong to a class")
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
