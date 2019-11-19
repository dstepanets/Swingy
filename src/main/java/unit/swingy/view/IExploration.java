package unit.swingy.view;

import unit.swingy.model.characters.Enemy;
import unit.swingy.model.characters.Hero;

public interface IExploration {

	void showIntro(String msg);
	void updateMap();
	void fightOrFlee(Enemy enemy);
	void escapeBattle(String msg);
	void initBattle();
	void battleRound(int enemyDamage, int heroDamage);
	void winBattle(int expReward);
	void winMap(String msg, int expReward);
	void youDie(String msg, String msg2);
	public void winGame(String title, String outro);

}
