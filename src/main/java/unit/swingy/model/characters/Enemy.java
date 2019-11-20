package unit.swingy.model.characters;

import lombok.Getter;
import unit.swingy.controller.Game;

import java.util.Random;

@Getter
public class Enemy extends ACharacter {

	EnemyClass clas;

	public Enemy() {

		int maxLvl = Game.getInstance().getHero().getLevel() + 2;
		if (maxLvl > 10) maxLvl = 10;
		int minLvl = Game.getInstance().getHero().getLevel() - 2;
		if (minLvl < 0) minLvl = 0;

		Random rand = new Random();
//		set random level in range of 2 levels from the hero's level
		level = rand.nextInt(maxLvl - minLvl + 1) + minLvl;
//		get random class from enum constants
		clas = EnemyClass.values()[rand.nextInt(EnemyClass.count)];

//		init stats
		hp = maxHp = clas.getBaseHp();
		attack = clas.getBaseAttack();
		defence =  clas.getBaseDefence();
//		level up in the same pace as hero
		for (int i = 0; i < level; i++) {
			hp = maxHp = (int) (maxHp * 1.25);
			attack = (int) (attack * 1.25);
			defence = (int) (defence * 1.25);
		}
//		correction to compensate for hero's artifacts
		hp = maxHp += level * 10;
		attack += level;
		defence += level;
	}

	public int takeDamage(ACharacter foe, int dice) {

		double d = (dice - 1)  / 10;	// num in range 0.0 - 0.5
		double attackMultiplier = new Random().nextDouble() + 0.5;	// num in range 0.5 - 1.5
		attackMultiplier += d; // num in range 0.5 - 2.0

		Hero hero = (Hero) foe;
		int damage = (int) ((hero.getAttack() + hero.getBonusAttack()) * attackMultiplier) - defence;
		if (damage <= 0)
			damage = 1;

		hp -= damage;
		if (hp < 0)
			hp = 0;

		return damage;
	}

}
