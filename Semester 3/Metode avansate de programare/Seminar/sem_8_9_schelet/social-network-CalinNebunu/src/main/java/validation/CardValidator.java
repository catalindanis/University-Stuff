package validation;

import domain.Card;

public class CardValidator implements Validator<Card> {

    @Override
    public void validate(Card card) throws ValidationException {

        // --- 1. Validare ID ---
        if (card.getId() == null) {
            throw new ValidationException("Card ID cannot be null");
        }
        if (card.getId() <= 0) {
            throw new ValidationException("Card ID must be positive");
        }

        // --- 2. Validare Nume (1-20 caractere) ---
        String nume = card.getNumeCard();

        if (nume == null || nume.trim().isBlank()) {
            throw new ValidationException("Card name is required");
        }

        if (nume.trim().length() > 20) {
            throw new ValidationException("Card name must be 20 characters or less");
        }

        // --- 3. Validare Tip ---
        if (!card.getTipMembri().equalsIgnoreCase("Swimming") &&
            !card.getTipMembri().equalsIgnoreCase("Flying") &&
            !card.getTipMembri().equalsIgnoreCase("Hybrid")) {
            throw new ValidationException("Card tip membri must be one of: Swimming, Flying, Hybrid");
        }
    }
}