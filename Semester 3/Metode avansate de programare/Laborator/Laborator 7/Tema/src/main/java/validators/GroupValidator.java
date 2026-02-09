package validators;

import models.Group;

public class GroupValidator implements Validator<Group> {
    @Override
    public boolean validate(Group group) {
        if(group == null)
            return false;

//        if(group.getId() <= 0)
//            return false;

        if(group.getName() == null || group.getName().isBlank())
            return false;

        if(group.getDucks().stream().anyMatch(duck -> duck.getType() != group.getDucksType()))
            return false;

        return true;
    }
}
