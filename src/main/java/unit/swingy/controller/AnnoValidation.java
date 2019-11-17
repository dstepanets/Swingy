package unit.swingy.controller;

import unit.swingy.model.characters.Hero;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

class AnnoValidation {


	//	Create ValidatorFactory which returns validator
	ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	//	It validates bean instances
	Validator validator = factory.getValidator();

	boolean validateHero(Hero hero) {


		//	Validate bean
		Set<ConstraintViolation<Hero>> constraintViolations = validator.validate(hero);

		//	Show errors
		if (constraintViolations.size() > 0) {
			for (ConstraintViolation<Hero> violation : constraintViolations) {
				System.err.println(violation.getMessage());
			}
			return false;
		}
		return true;
	}

}
