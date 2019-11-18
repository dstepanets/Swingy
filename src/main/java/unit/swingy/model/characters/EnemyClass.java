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
	Fuhrer,
	Elephant,
	Cthulhu,
	UFO;

	public static final int count = EnemyClass.values().length;

	private String		className;

	private int		baseHp;
	private int 		baseAttack;
	private int 		baseDefence;

	private ImageIcon 	avatar;
	private ImageIcon 	icon;

	EnemyClass() {
		className = this.toString();

		String avatarPath = null;
		String iconPAth = null;

		switch (className) {
			case "Cockroach":
				baseHp = 50;
				baseAttack = 5;
				baseDefence = 5;
				avatarPath = "src/main/resources/img/enemyAvatars/Cockroach.png";
				iconPAth  = "src/main/resources/img/enemyIcons/Cockroach.png";
				break;
			case "Mushroom":
				baseHp = 70;
				baseAttack = 6;
				baseDefence = 7;
				avatarPath = "src/main/resources/img/enemyAvatars/Mushroom.jpg";
				iconPAth  = "src/main/resources/img/enemyIcons/Mushroom.png";
				break;
			case "Moonshine":
				baseHp = 90;
				baseAttack = 8;
				baseDefence = 0;
				avatarPath = "src/main/resources/img/enemyAvatars/Moonshine.jpg";
				iconPAth  = "src/main/resources/img/enemyIcons/Moonshine.png";
				break;
			case "Java":
				baseHp = 80;
				baseAttack = 8;
				baseDefence = 7;
				avatarPath = "src/main/resources/img/enemyAvatars/Java.jpg";
				iconPAth  = "src/main/resources/img/enemyIcons/Java.png";
				break;
			case "FSM":
				baseHp = 100;
				baseAttack = 8;
				baseDefence = 8;
				className = "Flying Spaghetti Monster";
				avatarPath = "src/main/resources/img/enemyAvatars/FSM.jpg";
				iconPAth  = "src/main/resources/img/enemyIcons/FSM.png";
				break;
			case "Reptiloid":
				baseHp = 90;
				baseAttack = 9;
				baseDefence = 6;
				avatarPath = "src/main/resources/img/enemyAvatars/Reptiloid.jpg";
				iconPAth  = "src/main/resources/img/enemyIcons/Reptiloid.png";
				break;
			case "Fuhrer":
				baseHp = 110;
				baseAttack = 10;
				baseDefence = 6;
				avatarPath = "src/main/resources/img/enemyAvatars/Fuhrer.jpg";
				iconPAth  = "src/main/resources/img/enemyIcons/Fuhrer.png";
				break;
			case "Elephant":
				baseHp = 150;
				baseAttack = 10;
				baseDefence = 12;
				avatarPath = "src/main/resources/img/enemyAvatars/Elephant.jpg";
				iconPAth  = "src/main/resources/img/enemyIcons/Elephant.png";
				break;
			case "Cthulhu":
				baseHp = 120;
				baseAttack = 9;
				baseDefence = 9;
				avatarPath = "src/main/resources/img/enemyAvatars/Cthulhu.jpg";
				iconPAth  = "src/main/resources/img/enemyIcons/Cthulhu.png";
				break;
			case "UFO":
				baseHp = 100;
				baseAttack = 8;
				baseDefence = 10;
				avatarPath = "src/main/resources/img/enemyAvatars/UFO.jpg";
				iconPAth  = "src/main/resources/img/enemyIcons/UFO.png";
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
			case Elephant:
				description = "Normally, nice and friendly creature. If not provoked.";
				break;
			case Cthulhu:
				description = "Ph'nglui mglw'nafh Cthulhu R'lyeh wgah'nagl fhtagn";
				break;
			case UFO:
				description = "They abduct heroes to film for their pervert extraterrestrial porn";
				break;
		}
		return (description);
	}

}
