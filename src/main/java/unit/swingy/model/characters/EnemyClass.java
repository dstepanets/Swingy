package unit.swingy.model.characters;

import darrylbu.icon.StretchIcon;
import lombok.Getter;

import javax.swing.*;

// TODO add Elephant very strong and peaceful
@Getter
public enum EnemyClass {
	Cockroach,
	Mushroom,
	Moonshine,
	Java,
	FSM,
	Reptiloid,
	Fuhrer;

	public static final int count = EnemyClass.values().length;

	private String		className;
//	PL - per level
	private double		hpPL;
	private double		attackPL;
	private double		defencePL;

	private ImageIcon avatar;
	private ImageIcon icon;

	EnemyClass() {
		className = this.toString();

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
				attackPL = 1.7;
				defencePL = 2.0;
				avatarPath = "src/main/resources/img/enemyAvatars/Mushroom.jpg";
				iconPAth  = "src/main/resources/img/enemyIcons/Mushroom.png";
				break;
			case "Moonshine":
				hpPL = 8.0;
				attackPL = 2.5;
				defencePL = 0.0;
				avatarPath = "src/main/resources/img/enemyAvatars/Moonshine.jpg";
				iconPAth  = "src/main/resources/img/enemyIcons/Moonshine.png";
				break;
			case "Java":
				hpPL = 8.0;
				attackPL = 3.0;
				defencePL = 2.5;
				avatarPath = "src/main/resources/img/enemyAvatars/Java.jpg";
				iconPAth  = "src/main/resources/img/enemyIcons/Java.png";
				break;
			case "FSM":
				hpPL = 9.0;
				attackPL = 3.5;
				defencePL = 2.0;
				className = "Flying Spaghetti Monster";
				avatarPath = "src/main/resources/img/enemyAvatars/FSM.jpg";
				iconPAth  = "src/main/resources/img/enemyIcons/FSM.png";
				break;
			case "Reptiloid":
				hpPL = 10.0;
				attackPL = 4.0;
				defencePL = 3.0;
				avatarPath = "src/main/resources/img/enemyAvatars/Reptiloid.jpg";
				iconPAth  = "src/main/resources/img/enemyIcons/Reptiloid.png";
				break;
			case "Fuhrer":
				hpPL = 12.0;
				attackPL = 4.0;
				defencePL = 2.0;
				avatarPath = "src/main/resources/img/enemyAvatars/Fuhrer.jpg";
				iconPAth  = "src/main/resources/img/enemyIcons/Fuhrer.png";
				break;
		}

		avatar = new ImageIcon(avatarPath);
		icon = new StretchIcon(iconPAth);
	}

	public String getDescription() {

		String description = null;

		switch (this) {
			case Cockroach:
				description = "The terrible ancient monster who saw dinosaurs walking on Earth.";
				break;
			case Mushroom:
				description = "It can be tasty or lethal. Or both.";
				break;
			case Moonshine:
				description = "Moonshine is very strong. But you must destroy as much of it as possible.";
				break;
			case Java:
				description = "Java is the best programming language. Why would you fight it?";
				break;
			case FSM:
				description = "There is only one God. And that is His Macaroni Holiness FSM.";
				break;
			case Reptiloid:
				description = "True ruler of the world. Makes conspiracy to kill you.";
				break;
			case Fuhrer:
				description = "Deutschland uber alles. Has the Final Solution to the Jewish Question";
				break;
		}
		return (description);
	}

}
