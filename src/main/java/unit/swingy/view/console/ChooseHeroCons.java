package unit.swingy.view.console;

import unit.swingy.model.characters.Hero;
import unit.swingy.model.characters.HeroClass;

import java.util.ArrayList;
import java.util.Scanner;

public class ChooseHeroCons {

	private Scanner scanner = new Scanner(System.in);

	public Hero chooseHero(ArrayList<Hero> heroesList) {

		Hero hero = null;

		System.out.println("Here we go! Choose your hero by number. Enter '0' if you want to create a new one.");
		System.out.println(String.format("%2s  %-20s\t%-10s\t%-5s\t%-5s", "#", "NAME", "CLASS", "LEVEL", "EXP"));
		int i = 0;
		for (Hero h : heroesList) {
			String str = String.format("%2d. %-20s\t%-10s\t%-5d\t%-5d",
					++i, h.getName(), h.getClas().getClassName(), h.getLevel(), h.getExp());
			System.out.println(str);
		}

		return hero;
	}

	public String getNewHeroName() {

		String name;

		do {
			System.out.println("Type your hero's name and press Enter:");
			name = scanner.nextLine();
		} while (name.isEmpty() || name.trim().isEmpty());

		return name;
	}

	public HeroClass getNewHeroClas() {

		HeroClass clas = null;

		do {
			System.out.println("What kind of hero is that?");
			System.out.println("Type a number to learn more about the choice:");

			int n = 0;
			for (HeroClass h : HeroClass.values()) {
				n++;
				System.out.println("\t" + n + ". " + h.getClassName());
			}

			while (!scanner.hasNextInt()) {
				scanner.next();
			}
			int choice = scanner.nextInt();
			scanner.nextLine();
			if (choice >= 1 && choice <= HeroClass.count) {
				clas = HeroClass.values()[choice - 1];
				System.out.println("\n\t" + clas.toString() + "\n" + clas.getDescription());

				while (true) {
					System.out.println("\nDo you want to play this bastard? Yes / No:");
					String answer = scanner.nextLine();
					if (answer.equalsIgnoreCase("Y") || answer.equalsIgnoreCase("Yes")) {
						break;
					} else if (answer.equalsIgnoreCase("N") || answer.equalsIgnoreCase("No")) {
						clas = null;
						break;
					}
				}
			}
		} while (clas == null);

		return clas;
	}

}
