package unit.swingy.model.characters;

import lombok.Getter;

@Getter
public enum EnemyClass {
	COCKROACH,
	MUSHROOM,
	MOONSHINE;

	public static final int count = EnemyClass.values().length;

	private String	className;
//	PL - per level
	private double		hpPL;
	private double		attackPL;
	private double		defencePL;

	private String avatar;

	EnemyClass() {
		className = this.toString().substring(0, 1) + this.toString().substring(1).toLowerCase();
		avatar = ""; // path to file

		switch (className) {
			case "Cockroach":
				hpPL = 10.0;
				attackPL = 2.0;
				defencePL = 1.5;
				break;
			case "Mushroom":
				hpPL = 12.0;
				attackPL = 1.5;
				defencePL = 2.0;
				break;
			case "Moonshine":
				hpPL = 20.0;
				attackPL = 8;
				defencePL = 0.0;
				break;
		}
	}

	public String getDescription() {

		String description = null;

		switch (this) {
			case COCKROACH:
				description = "The terrible ancient monster who saw dinosaurs walking on the Earth.";
				break;
			case MUSHROOM:
				description = "It can be tasty or lethal. Or both.";
				break;
			case MOONSHINE:
				description = "Moonshine is very strong. But you must destroy as much of it as possible.";
				break;
		}
		return ("\"" + description + "\"");
	}

}
