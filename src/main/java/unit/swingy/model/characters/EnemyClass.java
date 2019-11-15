package unit.swingy.model.characters;

import darrylbu.icon.StretchIcon;
import lombok.Getter;

import javax.swing.*;

@Getter
public enum EnemyClass {
	COCKROACH,
	MUSHROOM,
	MOONSHINE;

	public static final int count = EnemyClass.values().length;

	private String		className;
//	PL - per level
	private double		hpPL;
	private double		attackPL;
	private double		defencePL;

	private ImageIcon avatar;
	private ImageIcon icon;

	EnemyClass() {
		className = this.toString().substring(0, 1) + this.toString().substring(1).toLowerCase();

		String avatarPath = null;
		String iconPAth = null;

		switch (className) {
			case "Cockroach":
				hpPL = 5.0;
				attackPL = 1.2;
				defencePL = 0.8;
				avatarPath = "src/main/resources/img/enemyAvatars/Cockroach.png";
				iconPAth  = "src/main/resources/img/enemyIcons/Cockroach.png";
				break;
			case "Mushroom":
				hpPL = 7.0;
				attackPL = 1.5;
				defencePL = 2.0;
				avatarPath = "src/main/resources/img/enemyAvatars/Mushroom.jpg";
				iconPAth  = "src/main/resources/img/enemyIcons/Mushroom.png";
				break;
			case "Moonshine":
				hpPL = 9.0;
				attackPL = 3;
				defencePL = 0.0;
				avatarPath = "src/main/resources/img/enemyAvatars/Moonshine.jpg";
				iconPAth  = "src/main/resources/img/enemyIcons/Moonshine.png";
				break;
		}

		avatar = new ImageIcon(avatarPath);
		icon = new StretchIcon(iconPAth);
	}

	public String getDescription() {

		String description = null;

		switch (this) {
			case COCKROACH:
				description = "The terrible ancient monster who saw dinosaurs walking on Earth.";
				break;
			case MUSHROOM:
				description = "It can be tasty or lethal. Or both.";
				break;
			case MOONSHINE:
				description = "Moonshine is very strong. But you must destroy as much of it as possible.";
				break;
		}
		return (description);
	}

}
