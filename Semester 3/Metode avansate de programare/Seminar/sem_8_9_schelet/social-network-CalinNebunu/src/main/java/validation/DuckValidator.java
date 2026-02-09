package validation;

import domain.Duck;

public class DuckValidator implements Validator<Duck> {

    @Override
    public void validate(Duck object) throws ValidationException {

        // --- Validare Viteza ---
        Double viteza = object.getViteza();
        if (viteza == null) {
            throw new ValidationException("Duck's viteza is required");
        }
        if (viteza <= 0) {
            throw new ValidationException("Duck's viteza must be a positive value");
        }

        // --- Validare Rezistenta ---
        Double rezistenta = object.getRezistenta();
        if (rezistenta == null) {
            throw new ValidationException("Duck's rezistenta is required");
        }
        if (rezistenta <= 0) {
            throw new ValidationException("Duck's rezistenta must be a positive value");
        }

    }
}