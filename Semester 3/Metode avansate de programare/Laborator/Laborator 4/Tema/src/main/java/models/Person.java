package models;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

public class Person extends User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String occupation;
    private int empathyLevel;

    public Person(long id, String username, String email, String password, String firstName, String lastName, LocalDate dateOfBirth, String occupation, int empathyLevel) {
        super(id, username, email, password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.occupation = occupation;
        this.empathyLevel = empathyLevel;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public int getEmpathyLevel() {
        return empathyLevel;
    }

    public void setEmpathyLevel(int empathyLevel) {
        this.empathyLevel = empathyLevel;
    }

    @Override
    public String toString() {
        return "Persoana" + ", " +
                super.toString() + ", " +
                "prenume='" + firstName + '\'' +
                ", nume='" + lastName + '\'' +
                ", zi de nastere=" + dateOfBirth +
                ", ocupatie='" + occupation + '\'' +
                ", nivel de empatie=" + empathyLevel;
    }


}
