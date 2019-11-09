package unit.swingy.model.characters;

import darrylbu.icon.StretchIcon;
import lombok.Getter;

import javax.swing.*;

@Getter
public enum HeroClass {
	REGULAR,
	BERSERK,
	TANK;

	public static final int count = HeroClass.values().length;

	private String	className;
	private int 	maxHp;
	private int		attack;
	private int		defence;

	private ImageIcon avatar;
	private ImageIcon icon;

	HeroClass() {
		className = this.toString().substring(0, 1) + this.toString().substring(1).toLowerCase();

		String avatarPath = "src/main/resources/img/heroAvatars/" + className + ".jpg";
		avatar = new ImageIcon(avatarPath);
		String iconPath = "src/main/resources/img/heroIcons/" + className + ".png";
		icon = new StretchIcon(iconPath);

		switch (className) {
			case "Regular":
				maxHp = 100;
				attack = 10;
				defence = 5;
				break;
			case "Berserk":
				maxHp = 80;
				attack = 14;
				defence = 4;
				break;
			case "Tank":
				maxHp = 120;
				attack = 8;
				defence = 6;
				break;
		}
	}

	public String getDescription() {

		String description = null;

		switch (this) {
			case REGULAR:
				description = "* I'm just a regular everyday normal guy\n" +
						"Nothin' special 'bout me, motherfucker * (c) Jon Lajoie\n" +
						"It's hard to notice me in a crowd. Even on an empty street. " +
						"And still harder to remember. But that's OK. Whatever.";
				break;
			case BERSERK:
				description = "Arrrgh! You, filthy pig! Oh, you drive me crazy! I’ll smash your head! " +
						"No, I’ll cut it off and play football with it. " +
						"But first, I will feed you with your own balls, ha-ha! " +
						"If you have those, bitch. Ah, bummer, this is just a mirror…";
				break;
			case TANK:
				description = "I eat a cow, drink a barrel of beer, fuck a horse, and go looking for a good brawl. " +
						"When I find one, I don't rush. Usually, my opponents die exhausted, " +
						"while trying to break my 100-kilos armor with their toy sticks.";
				break;
		}
		return ("\"" + description + "\"");
	}


	public String getStartingStatsInfo() {
		String startingStats = "STARTING STATS (";
		startingStats += "HP: " + maxHp + " || " + "Attack: " + attack + " || " + "Defence: " + defence + ")";
		return (startingStats);
	}

}

