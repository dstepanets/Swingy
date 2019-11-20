package unit.swingy.model.characters;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.validator.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import unit.swingy.controller.Game;
import unit.swingy.model.artifacts.AArtifact;

import java.util.Random;


@Getter @Setter
public class Hero extends ACharacter {
	private int id;

	@Size(min=1, max = 20, message = "Hero's name must be 1-20 character long")
	@NotBlank(message = "Hero's name can't be blank")
	private String name;

	@NotNull(message = "Hero must belong to a class")
	private HeroClass clas;

//	private int level;
	private int exp;
	private int expToLevelUp;

//	private int maxHp;
//	private int hp;
	private int bonusHp;

//	private int attack;
	private int bonusAttack;

//	private int defence;
	private int bonusDefence;

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

	public int takeDamage(ACharacter e, int dice) {

		Enemy enemy = (Enemy) e;
		double attackMultiplier = new Random().nextDouble() + 0.5;	// num in range 0.5 - 1.5

		int damage;
//		Hitler kills the JudeoMason immediately :(
		if (clas == HeroClass.JudeoMason && enemy.getClas() == EnemyClass.Fuhrer)
			damage = hp;
		else {
			damage = (int) (enemy.getAttack() * attackMultiplier) - (defence + bonusDefence);
			if (damage <= 0)
				damage = 1;
		}

		hp -= damage;
		if (hp < 0)
			hp = 0;

		return damage;
	}

//	damage when visiting Ukraine
	public int takeDamage(int damage) {
		hp -= damage;
		if (hp <= 0)
			hp = 1;
		return damage;
	}


	public void heal() {
		hp = maxHp + bonusHp;
	}

	public int gainExp(Enemy enemy) {

		int expReward;

//		if enemy is null it's the end-of-map reward
		if (enemy == null) {
			expReward = (this.clas == HeroClass.Traveler) ? expToLevelUp : (expToLevelUp / 3);
		} else {
			expReward = (expToLevelUp / 5) + (enemy.getAttack() + enemy.getDefence() + enemy.getMaxHp()) * (enemy.getLevel() + 1);
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

		Game.getInstance().levelUp();
	}

	public void equipArtifact(AArtifact art) {

		if (art != null) {
			AArtifact.ArtifactType type = art.getType();
			switch (type) {
				case WEAPON:
					weapon = art;
					bonusAttack = weapon.getPower();
					break;
				case ARMOR:
					armor = art;
					bonusDefence = armor.getPower();
					break;
				case HELM:
					helm = art;
					bonusHp = helm.getPower() * 10;
					heal();
					break;
			}
		}
	}

}
