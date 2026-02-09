package factories;

import exceptions.EventException;
import models.Event;
import models.Lane;
import models.RaceEvent;
import models.SwimmingGroup;
import services.GroupsService;
import validators.RaceEventValidator;

import java.util.List;

public class EventsFactory implements Factory<Event> {
    private static final EventsFactory instance = new EventsFactory();
    private RaceEventValidator raceEventValidator = new RaceEventValidator();

    private EventsFactory() {}

    public static EventsFactory getInstance() { return instance; }

    public Event create(
            long id,
            long swimmingGroupId,
            List<Lane> lanes) {

        if(!(GroupsService.getInstance().getById(swimmingGroupId) instanceof SwimmingGroup))
            throw new EventException("Invalid event");

        RaceEvent event = new RaceEvent(
                id,
                (SwimmingGroup) GroupsService.getInstance().getById(swimmingGroupId),
                lanes
        );

        if(!raceEventValidator.validate(event))
            throw new EventException("Invalid event");

        return event;
    }
}
