package unit.swingy.model.artifacts;

import lombok.Getter;

import javax.swing.*;

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

	protected String iconAddr;

}
