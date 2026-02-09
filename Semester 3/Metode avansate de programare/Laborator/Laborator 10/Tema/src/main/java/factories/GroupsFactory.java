package factories;

import exceptions.GroupException;
import models.*;
import services.UsersService;
import validators.GroupValidator;
import validators.Validator;

import java.util.ArrayList;
import java.util.List;

public class GroupsFactory implements Factory<Group> {
    private static final GroupsFactory instance = new GroupsFactory();
    private final Validator<Group> groupValidator;

    private GroupsFactory() {
        groupValidator = new GroupValidator();
    }

    public static GroupsFactory getInstance() {
        return instance;
    }

    public Group create(long id,
                           String name,
                           DuckType ducksType,
                           List<Long> ducksIds) {

//        if(ducksIds == null)
//            throw new GroupException("Invalid group");

        List<Duck> ducks = new ArrayList<>(UsersService
                .getInstance()
                .getDucks()
                .stream()
                .filter(duck -> ducksIds.contains(duck.getId()))
                .toList()
        );

        if(ducks.size() != ducksIds.size())
            throw new GroupException("Invalid group");

        Group group = switch (ducksType) {
            case FLYING -> new FlyingGroup(id, name, ducks);
            case SWIMMING -> new SwimmingGroup(id, name, ducks);
            case FLYING_AND_SWIMMING -> new FlyingSwimmingGroup(id, name, ducks);
        };

        if(!groupValidator.validate(group))
            throw new GroupException("Invalid group");

        return group;
    }

}
