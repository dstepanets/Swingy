package unit.swingy.model.characters;

import java.util.Random;

public class EnemyBuilder {

	private Enemy enemy;

	public EnemyBuilder() {
		reset();
	}

	public void reset() {
		this.enemy = new Enemy();
	}

	public Enemy getEnemy() {
		Enemy readyEnemy = this.enemy;
		reset();
		return readyEnemy;
	}

	public void buildEnemy() {
//		System.out.println(">> Building an enemy...");
//
//		Random rand = new Random();
////		set random level in range 0-9
//		int lvl = rand.nextInt(10);
//		System.out.println("> Level=" + lvl);
////		set random class from enum constants
//		EnemyClass clas = EnemyClass.values()[rand.nextInt(EnemyClass.count)];
//		System.out.println("> Class=" + clas);
//
//		enemy = new Enemy();

	}

}
