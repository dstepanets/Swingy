package unit.swingy.model.artifacts;

import darrylbu.icon.StretchIcon;

public class Armor extends AArtifact {

	public Armor(int lvl) {

		this.type = ArtifactType.ARMOR;

		namesAdr = new String[][] {
				{"Armour of the Brave", "scr/main/resources/img/artifacts/armor/bikini.png"},
				{"Super-Pijama", "scr/main/resources/img/artifacts/armor/pijama.png"},
				{"Towel", "scr/main/resources/img/artifacts/armor/towel.jpg"},
				{"armor", "scr/main/resources/img/artifacts//armor/pijama.png"},
				{"armor", ""},
				{"armor", ""},
				{"armor", ""},
				{"armor", ""},
				{"armor", ""},
				{"armor", ""},
		};

		this.name = namesAdr[lvl][0];
		this.power = pow;

		icon = new StretchIcon(namesAdr[lvl][1]);

	}

}
