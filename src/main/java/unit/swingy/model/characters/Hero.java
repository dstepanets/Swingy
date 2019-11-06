package unit.swingy.model.characters;

import lombok.AccessLevel;
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
	private int id;
	@Range(min=1, max = 50, message = "Hero's name must be 1-50 character long")
	@NotBlank(message = "Hero's name can't be blank")
	private String name;
	@NotNull(message = "Hero must belong to a class")
	private HeroClass clas;
//	private int level;
	private int exp;
	private int expToLevelUp;

//	private int maxHp;
//	private int hp;
//	private int attack;
//	private int defence;

	private Weapon weapon;
	private Armor armor;
	private Helm helm;

	Hero() {
		level = 0;
		exp = 0;
		setExpToLevelUp();
	}

	void setExpToLevelUp() {
		//		formula was given in the task description
		expToLevelUp = (level + 1) * 1000 + level * level * 450;
	}

	public String takeDamage(ACharacter enemy) {

		int damage = enemy.getAttack() - defence;
		if (damage < 0)
			damage = 0;
		String log = name + " (" + hp + "/" + maxHp + ") takes " + damage + " damage.";
		hp -= damage;

		return log;
	}

	public void heal() {
		hp = maxHp;
	}

	public void gainExp(Enemy enemy) {

//		if enemy is null it's the end-of-map reward
		if (enemy == null) {
			exp += expToLevelUp / 10;
		} else {
			exp += (enemy.getAttack() + enemy.getDefence()) * enemy.getLevel() + enemy.getMaxHp();
		}

		if (exp >= expToLevelUp){
			levelUp();
		}

	}

	private void levelUp() {
		exp -= expToLevelUp;
		level++;
		setExpToLevelUp();

		maxHp *= 1.25;
		attack *= 1.25;
		defence *= 1.25;
	}


}
