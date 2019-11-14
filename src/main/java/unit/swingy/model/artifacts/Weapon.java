package unit.swingy.model.artifacts;

import unit.swingy.model.characters.Enemy;

public class Weapon extends AArtifact {

//	protected String[] namesArr;
//
//	protected ArtifactType type;
//	protected String name;
//	protected int power;

	public Weapon(int pow) {

		this.type = ArtifactType.WEAPON;

		namesArr = new String[] {
				"Fork",
				"Anti-President Egg",
				"Dildo",
				"Zippo Lighter",
				"Cannabis Joint",
				"Fender Stratocaster",
				"Lightsaber",
				"BM-21 Grad",
				"Hydrogen Bomb",
				"Supermassive Black Hole"
		};

		this.name = namesArr[pow - 1];
		this.power = pow;
	}

}
