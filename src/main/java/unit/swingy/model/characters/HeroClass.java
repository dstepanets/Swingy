package unit.swingy.model.characters;

import darrylbu.icon.StretchIcon;
import lombok.Getter;

import javax.swing.*;

@Getter
public enum HeroClass {
	SadCat,
	ParanoidAndroid,
	Traveler,
	JudeoMason,
	Tank,
	AngryBird,
	Sorceress;

	public static final int count = HeroClass.values().length;

	private String	className;
	private int 	baseHp;
	private int		attack;
	private int		defence;

	private ImageIcon avatar;
	private ImageIcon icon;

	HeroClass() {
		className = this.toString();

		String avatarPath = "src/main/resources/img/heroAvatars/" + className + ".jpg";
		avatar = new ImageIcon(avatarPath);
		String iconPath = "src/main/resources/img/heroIcons/" + className + ".png";
		icon = new StretchIcon(iconPath);

		switch (className) {
			case "SadCat":
				baseHp = 90;
				attack = 8;
				defence = 6;
				break;
			case "ParanoidAndroid":
				baseHp = 110;
				attack = 8;
				defence = 7;
				break;
			case "Traveler":
				baseHp = 70;
				attack = 6;
				defence = 8;
				break;
			case "JudeoMason":
				baseHp = 100;
				attack = 9;
				defence = 7;
				break;
			case "Tank":
				baseHp = 120;
				attack = 7;
				defence = 10;
				break;
			case "AngryBird":
				baseHp = 80;
				attack = 12;
				defence = 6;
				break;
			case "Sorceress":
				baseHp = 90;
				attack = 6;
				defence = 6;
				break;
		}
	}

	public String getDescription() {

		String description = null;

		switch (this) {
			case SadCat:
				description = "Only a total asshole would attack you. (Higher chance to escape battle)";
				break;
			case ParanoidAndroid:
				description = "You have a brain the size of a planet and constant depression.";
				break;
			case Traveler:
				description = "You level up when reach the Edge of the World.";
				break;
			case JudeoMason:
				description = "You control the world. But avoid the Fuhrer!";
				break;
			case Tank:
				description = "I eat a cow, drink a barrel of beer, fuck a horse, and go looking for a good brawl. " +
						"In my 100-kilos armor.";
				break;
			case AngryBird:
				description = "Hits strong, but weak in defence. For reasons unknown, it has complicated relationships with green pigs.";
				break;
			case Sorceress:
				description = "Weakens her enemies with powerful blowjob magic";
				break;
		}
		return (description);
	}


	public String getStartingStatsInfo() {
		String startingStats = "STARTING STATS (";
		startingStats += "HP: " + baseHp + " || " + "Attack: " + attack + " || " + "Defence: " + defence + ")";
		return (startingStats);
	}

}

