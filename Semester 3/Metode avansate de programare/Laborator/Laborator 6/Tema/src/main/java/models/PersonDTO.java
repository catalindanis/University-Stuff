package models;

import java.time.LocalDate;

public class PersonDTO {
    public String username;
    public String email;
    public String password;
    public String firstName;
    public String lastName;
    public LocalDate dateOfBirth;
    public String occupation;
    public int empathyLevel;

    public PersonDTO() {}

    public PersonDTO(String username, String email, String password, String firstName, String lastName, LocalDate dateOfBirth, String occupation, int empathyLevel) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.occupation = occupation;
        this.empathyLevel = empathyLevel;
    }
}
