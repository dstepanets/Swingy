package unit.swingy.model;

import lombok.Getter;
import lombok.Setter;
import unit.swingy.model.characters.Enemy;
import unit.swingy.model.characters.Hero;

@Getter @Setter
public class MapTile {

	private String obstacle;
	private boolean explored;
	private Hero hero;
	private Enemy enemy;

}
