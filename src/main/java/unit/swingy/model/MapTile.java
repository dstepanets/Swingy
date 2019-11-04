package unit.swingy.model;

import lombok.Getter;
import lombok.Setter;
import unit.swingy.model.characters.Enemy;
import unit.swingy.model.characters.Hero;

@Getter @Setter
public class MapTile {

	private String terrain;
	private boolean obstacle;
	private boolean explored;
	private Hero hero;
	private Enemy enemy;

//	MapTile() {
//		terrain = "Default";
//		obstacle = false;
//		explored = false;
//		hero = null;
//		enemy = null;
//	}

}
