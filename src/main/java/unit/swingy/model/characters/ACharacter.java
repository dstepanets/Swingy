package unit.swingy.model.characters;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Getter @Setter
public abstract class ACharacter {

	@Range(min = 0) protected int level;
	protected int hp;
	protected int attack;
	protected int defence;

}
