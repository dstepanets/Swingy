package unit.swingy.model.artifacts;

import unit.swingy.model.characters.Enemy;

public class Helm extends AArtifact {

	public Helm(int pow) {

		this.type = ArtifactType.HELM;

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
