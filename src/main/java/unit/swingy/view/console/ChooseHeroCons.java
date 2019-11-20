package unit.swingy.view.console;

import unit.swingy.model.characters.DataBase;
import unit.swingy.model.characters.Hero;
import unit.swingy.model.characters.HeroBuilder;
import unit.swingy.model.characters.HeroClass;

import java.util.ArrayList;
import java.util.Scanner;

public class ChooseHeroCons {

	private DataBase db;
	private HeroBuilder hb;
	private Scanner scanner;

	public ChooseHeroCons(DataBase dataBase, HeroBuilder builder) {
		db = dataBase;
		hb = builder;
		scanner  = new Scanner(System.in);
	}


	private boolean scanYesOrNo() {
		while (true) {
			String answer = scanner.nextLine();
			if (answer.equalsIgnoreCase("Y") || answer.equalsIgnoreCase("Yes")) {
				return true;
			} else if (answer.equalsIgnoreCase("N") || answer.equalsIgnoreCase("No")) {
				return false;
			}
		}
	}

	public Hero chooseHero() {

		Hero hero = null;
		ArrayList<Hero> heroesList = db.getHeroesList(hb);

		System.out.println("Here we go!");
		do {
			// print list
			int rows = 0;
			System.out.println("Choose your hero by number.");
			System.out.println("Enter '0' if you want to CREATE a new one.");
			System.out.println("Enter negative number if you want to DELETE a hero from this world. FOREVER.");
			System.out.printf("%s  %-20s\t%-10s\t%-5s\t%-5s\n", "##", "NAME", "CLASS", "LEVEL", "EXP");
			for (Hero h : heroesList) {
				printShortStats(h, ++rows);
			}
			System.out.print(":> ");

			// get int choice
			// init i outside of loop condition
			int i = rows + 1;
			do {
				while (!scanner.hasNextInt()) {
					scanner.next();
				}
				i = scanner.nextInt();
			} while (i < -rows || i > rows);
			scanner.nextLine();

			// break out of the loop and go to hero creation
			if (i == 0) {
				break;
			}

			// print detailed stats and ask for confirmation
			if (i > 0) {
				printShortStats(heroesList.get(i - 1), i);
				printMoreStats(heroesList.get(i - 1));
				System.out.println("\nDo you want to play this bastard? Yes / No:");
				System.out.print(":> ");
				if (scanYesOrNo() == true) {
					hero = heroesList.get(i - 1);
				}
			//	delete hero if a negative index is entered
			} else {
				System.out.println("Are you sure you want to eliminate this jerk FOREVER AND EVER, with no going back?");
				System.out.print("Yes/No:> ");
				if (scanYesOrNo() == true) {
					db.removeHero(heroesList.get(-i - 1).getId());
					heroesList = db.getHeroesList(hb);
				}
			}

		} while (hero == null);
		// if null is returned, go to hero creation
		return hero;
	}

	public void printShortStats(Hero h, int num) {
		System.out.printf("%2d. %-20s\t%-10s\t%-5d\t%-5d",
							num, h.getName(), h.getClas().getClassName(), h.getLevel(), h.getExp());
		System.out.println();
	}

	public void printMoreStats(Hero h) {
		System.out.println("HP:\t\t" + h.getHp());
		System.out.println("Attack:\t\t" + h.getAttack());
		System.out.println("Defence:\t" + h.getDefence());

		if (h.getWeapon() == null) System.out.println("Weapon:\t\tnone");
		else System.out.println("Weapon:\t\t" + h.getWeapon().getName() + " (Attack +" + h.getBonusAttack() + ")");

		if (h.getArmor() == null) System.out.println("Armor:\t\tnone");
		else System.out.println("Armor:\t\t" + h.getArmor().getName() + " (Defence +" + h.getBonusDefence() + ")");

		if (h.getHelm() == null) System.out.println("Helm:\t\tnone");
		else System.out.println("Helm:\t\t" + h.getHelm().getName() + " (HP +" + h.getBonusHp() + ")");

		System.out.println(h.getClas().getDescription());
	}

	public String getNewHeroName() {

		String name;

		do {
			System.out.println("Type your hero's name and press Enter:");
			System.out.print(":> ");
			name = scanner.nextLine();
			name = name.trim();
			if (name.length() > 20)
				System.out.println("Name must be no more then 20 characters long!");
		} while (name.isEmpty() || name.length() > 20);

		return name;
	}

	public HeroClass getNewHeroClas() {

		HeroClass clas = null;

		do {
			System.out.println("What kind of hero is that?");
			System.out.println("Type a number to learn more about the choice:");

			// list heroes
			int n = 0;
			for (HeroClass h : HeroClass.values()) {
				n++;
				System.out.println("\t" + n + ". " + h.getClassName());
			}

			// get number
			while (!scanner.hasNextInt()) {
				System.out.print(":> ");
				scanner.next();
			}
			int choice = scanner.nextInt();
			scanner.nextLine();

			// print info about a class and confirm the choice
			if (choice >= 1 && choice <= HeroClass.count) {
				clas = HeroClass.values()[choice - 1];
				System.out.println("\n\t" + clas.toString() + "\n" + clas.getDescription());
				System.out.println(clas.getStartingStatsInfo());
				System.out.println("\nDo you want to add this bastard to the list? Yes / No:");
				System.out.print(":> ");
				if (scanYesOrNo() == false) {
					clas = null;
				}
			}
		} while (clas == null);

		return clas;
	}

}
