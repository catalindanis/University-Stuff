package models;

public class Grade {
    private Student student;
    private Homework homework;
    private double value;
    private String professor;

    public Grade(Student student, Homework homework, double value, String professor) {
        this.student = student;
        this.homework = homework;
        this.value = value;
        this.professor = professor;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Homework getHomework() {
        return homework;
    }

    public void setHomework(Homework homework) {
        this.homework = homework;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }
}
