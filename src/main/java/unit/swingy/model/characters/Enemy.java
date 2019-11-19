package unit.swingy.model.characters;

import lombok.Getter;
import unit.swingy.controller.Game;

import java.util.Random;

@Getter
public class Enemy extends ACharacter {

//	@Range(min = 0) protected int level;
	EnemyClass clas;
//	private int maxHp;
//	protected int hp;
//	protected int attack;
//	protected int defence;

	public Enemy() {

		System.out.println(">> Building an enemy...");

		int maxLvl = Game.getInstance().getHero().getLevel() + 2;
		if (maxLvl > 10) maxLvl = 10;
		int minLvl = Game.getInstance().getHero().getLevel() - 2;
		if (minLvl < 0) minLvl = 0;

		Random rand = new Random();
//		set random level in range of 2 levels from the hero's level
		level = rand.nextInt(maxLvl - minLvl + 1) + minLvl;
		System.out.println("> Level=" + level);
//		get random class from enum constants
		clas = EnemyClass.Cthulhu;
//		clas = EnemyClass.values()[rand.nextInt(EnemyClass.count)];
		System.out.println("> Class=" + clas);

//		init stats
		hp = maxHp = clas.getBaseHp();
		attack = clas.getBaseAttack();
		defence =  clas.getBaseDefence();
//		level up in the same pace as hero
		for (int i = 0; i < level; i++) {
			hp = maxHp *= 1.25;
			attack *= 1.25;
			defence *= 1.25;
		}
		System.out.println("> HP=" + maxHp + " | At=" + attack + " | Def=" + defence);
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
