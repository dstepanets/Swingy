package unit.swingy.model.artifacts;

import darrylbu.icon.StretchIcon;
import lombok.Getter;
import lombok.Setter;

@Getter
public abstract class AArtifact {

	public enum ArtifactType {
		WEAPON,
		ARMOR,
		HELM
	}


	protected String[][] namesAdr;

	protected ArtifactType type;
	protected String name;
	protected int power;

	protected StretchIcon icon;

}
