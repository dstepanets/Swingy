package unit.swingy.model.artifacts;

import unit.swingy.model.characters.Enemy;

public class Armor extends AArtifact {

	public Armor(int pow) {

		this.type = ArtifactType.ARMOR;

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
		this.power =pow;

	}

}
