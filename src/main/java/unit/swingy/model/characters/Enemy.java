package unit.swingy.model.characters;

import unit.swingy.model.artifacts.AArtifact;

import java.util.Random;

public class Enemy extends ACharacter {

//	@Range(min = 0) protected int level;
	EnemyClass clas;
//	protected int hp;
//	protected int attack;
//	protected int defence;

	public Enemy() {

		System.out.println(">> Building an enemy...");

		Random rand = new Random();
//		set random level in range 0-9
		level= rand.nextInt(10);
		System.out.println("> Level=" + level);
//		set random class from enum constants
		clas = EnemyClass.values()[rand.nextInt(EnemyClass.count)];
		System.out.println("> Class=" + clas);

		hp = (int) ((level + 1) * clas.getHpPL());
		attack = (int) ((level + 1) * clas.getAttackPL());
		defence = (int) ((level + 1) * clas.getDefencePL());
		System.out.println("> HP=" + hp + " | At=" + attack + " | Def=" + defence);

	}

}
