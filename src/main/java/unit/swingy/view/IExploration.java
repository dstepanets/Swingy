package unit.swingy.view;

import unit.swingy.model.characters.Enemy;

public interface IExploration {

	void updateMap();
	void fightOrFlee(Enemy enemy);
	void escapeBattle(String msg);

	void winBattle(int expReward);
	void winMap(String msg, int expReward);
	void youDie(String msg);

	void printMessage(String msg);

}
