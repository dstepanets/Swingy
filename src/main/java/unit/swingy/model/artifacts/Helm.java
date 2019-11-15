package unit.swingy.model.artifacts;

import darrylbu.icon.StretchIcon;

public class Helm extends AArtifact {

	public Helm(int pow) {

		this.type = ArtifactType.HELM;

		namesAdr = new String[][] {
				{"helm", "scr/main/resources/img/artifacts/helm"},
				{"helm", ""},
				{"helm", ""},
				{"helm", ""},
				{"helm", ""},
				{"helm", ""},
				{"helm", ""},
				{"helm", ""},
				{"helm", ""},
				{"helm", ""},
		};

		this.name = namesAdr[pow - 1][0];
		this.power = pow;

		icon = new StretchIcon(namesAdr[pow - 1][1]);
	}
}
