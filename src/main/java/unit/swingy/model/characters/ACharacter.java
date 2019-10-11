package unit.swingy.model.characters;

import lombok.Getter;
import org.hibernate.validator.constraints.Range;

@Getter
public abstract class ACharacter {

	@Range(min = 0, max = 10) private int level;
	private int hp;
	private int attack;
	private int defence;

}
