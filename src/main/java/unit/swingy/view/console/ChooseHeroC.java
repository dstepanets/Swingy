package unit.swingy.view.console;

import unit.swingy.model.characters.HeroClass;

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
