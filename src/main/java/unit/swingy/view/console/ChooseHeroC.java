package unit.swingy.view.console;

import unit.swingy.model.characters.HeroClass;

import java.util.HashMap;
import java.util.Scanner;

public class ChooseHeroC {

	private Scanner scanner = new Scanner(System.in);


	public String getName() {

		String name;

		do {
			System.out.println("Type your hero's name and press Enter:");
			name = scanner.nextLine();
		} while (name.isEmpty() || name.trim().isEmpty());

		return name;
	}

	public HeroClass getClas () {

		HeroClass clas = null;

		HashMap<Integer, String> choices = new HashMap<>();
		choices.put(1, "r");
		choices.put(2, "b");
		choices.put(3, "t");

		int choice;
		do {
			System.out.println("What kind of hero is that?");
			System.out.println("Type a number to learn more about the choice:");

			int n = 0;
			for(HeroClass h : HeroClass.values()) {
				n++;
				System.out.println("\t" + n + ". " + h.toString().substring(0, 1) + h.toString().substring(1).toLowerCase());
			}

			choice = scanner.nextInt();

			if (choice >= 0 && choice <= HeroClass.count) {
				System.out.println(choices.get(choice));
				clas = HeroClass.values()[choice - 1];
			}
		} while (clas == null);

		return clas;

	}



}
