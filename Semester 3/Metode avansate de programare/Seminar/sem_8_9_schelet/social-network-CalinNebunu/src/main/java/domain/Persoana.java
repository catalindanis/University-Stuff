package domain;

import java.time.LocalDate;

public class Persoana extends User {
    private String nume;
    private String prenume;
    private LocalDate dataNasterii;
    private String ocupatie;
    private Integer nivelEmpatie;

    // Constructor
    public Persoana(String username, String email, String password, String nume, String prenume, LocalDate dataNasterii, String ocupatie, Integer nivelEmpatie) {
        // Constructor din User
        super(username, email, password);

        this.nume = nume;
        this.prenume = prenume;
        this.dataNasterii = dataNasterii;
        this.ocupatie = ocupatie;
        this.nivelEmpatie = nivelEmpatie;
    }

    // --- Getters/Setters ---

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public LocalDate getDataNasterii() {
        return dataNasterii;
    }

    public void setDataNasterii(LocalDate dataNasterii) {
        this.dataNasterii = dataNasterii;
    }

    public String getOcupatie() {
        return ocupatie;
    }

    public void setOcupatie(String ocupatie) {
        this.ocupatie = ocupatie;
    }

    public Integer getNivelEmpatie() {
        return nivelEmpatie;
    }

    public void setNivelEmpatie(Integer nivelEmpatie) {
        this.nivelEmpatie = nivelEmpatie;
    }

    @Override
    public String toString() {
        return String.format("%s Persoana {ID=%d, First name='%s', Last name='%s', Dob=%s, Occupation=%s, Empathy=%d",
                super.toString(),
                id,
                prenume,
                nume,
                dataNasterii,
                ocupatie,
                nivelEmpatie
                );
    }

}
