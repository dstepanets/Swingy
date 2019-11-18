package unit.swingy.model.artifacts;

import darrylbu.icon.StretchIcon;

public class Weapon extends AArtifact {

//	protected String[] namesAdr;
//
//	protected ArtifactType type;
//	protected String name;
//	protected int power;

	public Weapon(int lvl) {

		this.type = ArtifactType.WEAPON;

		namesAdr = new String[][] {
				{"Trident", "src/main/resources/img/artifacts/weapon/fork.png"},
				{"Anti-President Eggs", "src/main/resources/img/artifacts/weapon/eggs.png"},
				{"Flamethrower", "src/main/resources/img/artifacts/weapon/zippo.png"},
				{"Black Hammer", "src/main/resources/img/artifacts/weapon/dildo.png"},
				{"Chemical Weapon", "src/main/resources/img/artifacts/weapon/bong.png"},
				{"Axe of Thunder", "src/main/resources/img/artifacts/weapon/axe.png"},
				{"Lightsaber", "src/main/resources/img/artifacts/weapon/lightsaber.png"},
				{"BM-21 Grad", "src/main/resources/img/artifacts/weapon/grad.jpg"},
				{"Hydrogen Bomb", "src/main/resources/img/artifacts/weapon/bomb.png"},
				{"Supermassive Black Hole", "src/main/resources/img/artifacts/weapon/black-hole.png"},
		};

		this.name = namesAdr[lvl][0];
		this.power = lvl + 1;

		icon = new StretchIcon(namesAdr[lvl][1]);
	}

}
