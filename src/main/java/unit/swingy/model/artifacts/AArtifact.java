package unit.swingy.model.artifacts;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class AArtifact {

	public enum ArtifactType {
		WEAPON,
		ARMOR,
		HELM
	}


	protected String[] namesArr;

	protected ArtifactType type;
	protected String name;
	protected int power;

}
