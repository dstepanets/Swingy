package unit.swingy.model.artifacts;

import darrylbu.icon.StretchIcon;

public class Weapon extends AArtifact {

//	protected String[] namesAdr;
//
//	protected ArtifactType type;
//	protected String name;
//	protected int power;

	public Weapon(int pow) {

		this.type = ArtifactType.WEAPON;

		namesAdr = new String[][] {
				{"Fork", "src/main/resources/img/artifacts/weapon/fork.png"},
				{"Anti-President Eggs", "src/main/resources/img/artifacts/weapon/eggs.png"},
				{"Dildo", "src/main/resources/img/artifacts/weapon/dildo.png"},
				{"Zippo Lighter", ""},
				{"Cannabis Joint", ""},
				{"Fender Stratocaster", ""},
				{"Lightsaber", ""},
				{"BM-21 Grad", ""},
				{"Hydrogen Bomb", ""},
				{"Supermassive Black Hole", ""},
		};

		this.name = namesAdr[pow - 1][0];
		this.power = pow;

		icon = new StretchIcon(namesAdr[pow - 1][1]);
	}

}
