package validators;

import models.Duck;
import models.User;

public class DuckValidator extends UserValidator {
    @Override
    public boolean validate(User user) {
        if(!super.validate(user))
            return false;

        Duck duck = (Duck) user;

        if(duck.getType() == null)
            return false;

        if(duck.getSpeed() <= 0)
            return false;

        if(duck.getResistance() <= 0)
            return false;

//        if(duck.getGroup() == null)
//            return false;

        return true;
    }
}
