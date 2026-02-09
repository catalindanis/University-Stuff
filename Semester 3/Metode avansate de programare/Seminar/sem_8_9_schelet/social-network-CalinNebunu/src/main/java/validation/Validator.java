package validation;

public interface Validator<T> {
    /**
     * Valideaza un obiect.
     * @param object - obiectul de validat
     * @throws ValidationException - daca obiectul nu e valid
     */
    void validate(T object) throws ValidationException;
}
