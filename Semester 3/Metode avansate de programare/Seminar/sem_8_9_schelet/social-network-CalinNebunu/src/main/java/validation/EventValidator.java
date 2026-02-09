package validation;

import domain.Event;

public class EventValidator implements Validator<Event> {

    @Override
    public void validate(Event event) throws ValidationException {

        // --- Validare ID ---
        if (event.getId() == null || event.getId() <= 0) {
            throw new ValidationException("Event ID must be positive.");
        }

        // --- Validare Descriere ---
        if (event.getDescriere() == null || event.getDescriere().trim().isBlank()) {
            throw new ValidationException("Event description cannot be empty.");
        }

        // --- Validare Tip ---
        if (event.getTip() == null || event.getTip().trim().isBlank()) {
            throw new ValidationException("Event tip cannot be empty.");
        }

        if (!event.getTip().equalsIgnoreCase("race")) {
            throw new ValidationException("Event tip must be race.");
        }
    }
}