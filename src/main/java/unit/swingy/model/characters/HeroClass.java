package unit.swingy.model.characters;

import lombok.Getter;

@Getter
public enum HeroClass {
	REGULAR,
	BERSERK,
	TANK;

	public static final int count = HeroClass.values().length;

	private String	className;
	private int		hp;
	private int		attack;
	private int		defence;

	private String avatar;

	HeroClass() {
		className = this.toString().substring(0, 1) + this.toString().substring(1).toLowerCase();
		avatar = "src/main/resources/img/avatars/" + className + ".jpg";

		switch (className) {
			case "Regular":
				hp = 100;
				attack = 10;
				defence = 5;
				break;
			case "Berserk":
				hp = 80;
				attack = 14;
				defence = 4;
				break;
			case "Tank":
				hp = 120;
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
		startingStats += "HP: " + hp + " || " + "Attack: " + attack + " || " + "Defence: " + defence + ")";
		return (startingStats);
	}

}

