package validators;

import models.Person;
import models.User;

import java.time.LocalDate;

public class PersonValidator extends UserValidator {
    @Override
    public boolean validate(User user) {
        if(!super.validate(user))
            return false;

        Person person = (Person) user;

        if(person.getFirstName() == null || person.getFirstName().isBlank())
            return false;

        if(person.getLastName() == null || person.getLastName().isBlank())
            return false;

        if(person.getDateOfBirth() == null || person.getDateOfBirth().isAfter(LocalDate.now()))
            return false;

        if(person.getEmpathyLevel() <= 0)
            return false;

        return true;
    }
}
