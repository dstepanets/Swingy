package unit.swingy.model.artifacts;

import darrylbu.icon.StretchIcon;

public class Armor extends AArtifact {

	public Armor(int pow) {

		this.type = ArtifactType.ARMOR;

		namesAdr = new String[][] {
				{"armor", "scr/main/resources/img/artifacts/armor"},
				{"armor", ""},
				{"Towel", "scr/main/resources/img/artifacts/armor/Towel.jpg"},
				{"armor", ""},
				{"armor", ""},
				{"armor", ""},
				{"armor", ""},
				{"armor", ""},
				{"armor", ""},
				{"armor", ""},
		};

		this.name = namesAdr[pow - 1][0];
		this.power = pow;

		icon = new StretchIcon(namesAdr[pow - 1][1]);

	}

}
