package unit.swingy.model.characters;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import javax.validation.constraints.NotNull;

import unit.swingy.model.artifacts.AArtifact;
import unit.swingy.model.artifacts.Helm;
import unit.swingy.model.artifacts.Weapon;
import unit.swingy.model.artifacts.Armor;

import java.util.Random;


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

	private AArtifact weapon;
	private AArtifact armor;
	private AArtifact helm;

	Hero() {
		level = 0;
		exp = 0;
		setExpToLevelUp();
	}

	void setExpToLevelUp() {
		//		formula was given in the task description
		expToLevelUp = (level + 1) * 1000 + level * level * 450;
	}

	public int takeDamage(ACharacter enemy, int dice) {

		double attackMultiplier = new Random().nextDouble() + 0.5;	// num in range 0.5 - 1.5

		int damage = (int) (enemy.getAttack() * attackMultiplier) - defence;
		if (damage < 0)
			damage = 0;

		hp -= damage;
		if (hp < 0)
			hp = 0;

		return damage;
	}

	public void heal() {
		hp = maxHp;
	}

	public int gainExp(Enemy enemy) {

		int expReward = 0;

//		if enemy is null it's the end-of-map reward
		if (enemy == null) {
			expReward += expToLevelUp / 10;
		} else {
			expReward += (enemy.getAttack() + enemy.getDefence()) * enemy.getLevel() + enemy.getMaxHp();
		}

		exp += expReward;
		if (exp >= expToLevelUp){
			levelUp();
		}

		return expReward;
	}

	private void levelUp() {
		exp -= expToLevelUp;
		level++;
		setExpToLevelUp();

		maxHp *= 1.25;
		attack *= 1.25;
		defence *= 1.25;
	}

	public void equipArtifact(AArtifact art) {

		AArtifact.ArtifactType type = art.getType();

		switch (type) {
			case WEAPON:
				weapon = art;
				break;
			case ARMOR:
				armor = art;
				break;
			case HELM:
				helm = art;
				break;
		}

		DataBase.getInstance().updateHero(this);
	}

}
