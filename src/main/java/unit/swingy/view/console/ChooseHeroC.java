package unit.swingy.view.console;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Scanner;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

public class ChooseHeroC {

	@NotEmpty(message = "Provide a name of your hero Empty")
	@NotBlank(message = "Provide a name of your hero Blank")
	private String name;

	private Scanner scanner = new Scanner(System.in);
	private static Validator validator;

	public static void setUpValidator() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}


	public String getName() {

		setUpValidator();
		Set<ConstraintViolation<String>> errors;

		do {
			System.out.println("Type your hero's name and press Enter:");
			name = scanner.nextLine();
			errors = validator.validate(name);

		} while (name.isEmpty());


		return name;

	}

}
