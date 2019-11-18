package unit.swingy.model.characters;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.validator.constraints.Range;
import javax.validation.constraints.Min;

@Getter @Setter
public abstract class ACharacter {

	@Range(min = 0, max = 10, message = "Character's level must be in range 0-10 inclusive")
	@Min(value=0)
	protected int level;
	@Min(value=1, message = "Invalid base HP")
	protected int maxHp;
	@Min(value=1, message = "Invalid HP")
	protected int hp;
	@Min(value=0, message = "Invalid attack value ")
	protected int attack;
	@Min(value=0,	message = "Invalid defence value ")
	protected int defence;

	public abstract int takeDamage(ACharacter foe, int dice);

}
