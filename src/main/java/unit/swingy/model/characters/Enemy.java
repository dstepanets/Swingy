package unit.swingy.model.characters;

import lombok.Getter;
import unit.swingy.controller.Game;

import java.util.Random;

@Getter
public class Enemy extends ACharacter {

//	@Range(min = 0) protected int level;
	EnemyClass clas;
//	private int baseHp;
//	protected int hp;
//	protected int attack;
//	protected int defence;

	public Enemy() {

		System.out.println(">> Building an enemy...");

		int maxLvl = Game.getInstance().getHero().getLevel() + 5;
		if (maxLvl > 10) maxLvl = 10;

		Random rand = new Random();
//		set random level in range 0 to (maxLvl - 1)
		level = rand.nextInt(maxLvl);
		System.out.println("> Level=" + level);
//		set random class from enum constants
		clas = EnemyClass.values()[rand.nextInt(EnemyClass.count)];
		System.out.println("> Class=" + clas);

		hp = baseHp = (int) ((level + 1) * clas.getHpPL());
		attack = (int) ((level + 1) * clas.getAttackPL());
		defence = (int) ((level + 1) * clas.getDefencePL());
		System.out.println("> HP=" + baseHp + " | At=" + attack + " | Def=" + defence);

	}

	public int takeDamage(ACharacter foe, int dice) {

		double d = (dice - 1)  / 10;	// num in range 0.0 - 0.5
		double attackMultiplier = new Random().nextDouble() + 0.5;	// num in range 0.5 - 1.5
		attackMultiplier += d; // num in range 0.5 - 2.0

		Hero hero = (Hero) foe;
		int damage = (int) ((hero.getAttack() + hero.getBonusAttack()) * attackMultiplier) - defence;
		if (damage < 0)
			damage = 0;

		hp -= damage;
		if (hp < 0)
			hp = 0;

		return damage;
	}

}
