package validation;

import domain.Persoana;
import java.time.LocalDate;

public class PersoanaValidator implements Validator<Persoana> {

    // Definim limitele o singura data, ca sa fie usor de modificat
    private static final LocalDate MIN_BIRTH_DATE = LocalDate.of(1960, 1, 1);
    private static final LocalDate MAX_BIRTH_DATE = LocalDate.of(2007, 1, 1);

    @Override
    public void validate(Persoana object) throws ValidationException {

        // --- Validare Nume ---
        String nume = object.getNume();
        if (nume == null || nume.trim().isEmpty()) {
            throw new ValidationException("Person's nume is required");
        }
        String trimmedNume = nume.trim();
        if (trimmedNume.length() < 2) {
            throw new ValidationException("Person's nume length cannot be less than 5");
        }

        // --- Validare Prenume ---
        String prenume = object.getPrenume();
        if (prenume == null || prenume.trim().isEmpty()) {
            throw new ValidationException("Person's prenume is required");
        }
        String trimmedPrenume = prenume.trim();
        if (trimmedPrenume.length() < 2) {
            throw new ValidationException("Person's prenume length cannot be less than 5");
        }

        // --- Validare Ocupatie ---
        String ocupatie = object.getOcupatie();
        if (ocupatie == null || ocupatie.trim().isEmpty()) {
            throw new ValidationException("Person's ocupatie is required");
        }
        String trimmedOcupatie = ocupatie.trim();
        if (trimmedOcupatie.length() < 2) {
            throw new ValidationException("Person's ocupatie length cannot be less than 5");
        }

        // --- Validare Data Nasterii ---
        LocalDate dataNasterii = object.getDataNasterii();
        if (dataNasterii == null) {
            throw new ValidationException("Person's dataNasterii is required");
        }
        if (dataNasterii.isAfter(MAX_BIRTH_DATE) || dataNasterii.isBefore(MIN_BIRTH_DATE)) {
            throw new ValidationException("Person's dataNasterii must be between " + MIN_BIRTH_DATE + " and " + MAX_BIRTH_DATE);
        }

        // --- Validare Nivel Empatie ---
        Integer empatie = object.getNivelEmpatie();
        if (empatie == null) {
            throw new ValidationException("Person's nivel empatie is required");
        }
        if (empatie < 0 || empatie > 10) {
            throw new ValidationException("Person's nivel empatie must be between 0 and 10");
        }
    }
}