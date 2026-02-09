import models.Grade;
import models.Homework;
import models.Student;

import java.util.AbstractMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Seminar5 {
    private static void report1(List<Grade> grades, String s) {
        Map<Student, List<Grade>> studentsByGrade = grades.stream()
                .collect(Collectors.groupingBy(Grade::getStudent));

        studentsByGrade.entrySet().stream()
                .filter(e -> e.getKey().getName().contains(s))
                .sorted((e1, e2) -> {
                    double average1 = getAverage(e1.getValue());
                    double average2 = getAverage(e2.getValue());

                    return -Double.compare(average1, average2);
                })
                .forEach(e -> System.out.println(
                        e.getKey().getName() + "; Average: " + getAverage(e.getValue())));
    }

    private static void report1Better(List<Grade> grades, String s) {
        Map<Student, Double> studentsByGrade = grades.stream()
                .collect(Collectors.groupingBy(Grade::getStudent, Collectors.averagingDouble(Grade::getValue)));

        studentsByGrade.entrySet().stream()
                .filter(e -> e.getKey().getName().contains(s))
                .sorted(Map.Entry.<Student, Double>comparingByValue().reversed())
                .forEach(e -> System.out.println(
                        e.getKey().getName() + "; Average: " + e.getValue()));
    }

    private static void report2(List<Grade> grades, String s) {

        Map<String, Double> collect = grades.stream()
                .filter(e -> e.getProfessor().contains(s))
                .collect(Collectors.groupingBy(Grade::getProfessor, Collectors.averagingDouble(Grade::getValue)))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (v1, v2) -> v1, LinkedHashMap::new));

        System.out.println(collect);;
    }

    private static void report3(List<Grade> grades, int group) {
        grades.stream()
                .filter(g -> g.getStudent().getGroup() == group)
                .collect(Collectors.groupingBy(Grade::getStudent, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<Student, Long>comparingByValue().reversed())
                .forEach(e -> System.out.println(e.getKey().getName() + "; Count: " + e.getValue()));
    }

    private static void report4(List<Grade> grades, int group) {
        grades.stream()
                .filter(g -> Integer.toString(g.getStudent().getGroup()).startsWith(Integer.toString(group)))
                .collect(Collectors.groupingBy(grade -> grade.getStudent().getGroup(), Collectors.averagingDouble(Grade::getValue)))
                .entrySet().stream()
                .sorted(Map.Entry.<Integer, Double>comparingByValue().reversed())
                .forEach(x -> System.out.println(x.getKey() + "; Average: " + x.getValue()));
    }

    private static void report5(List<Grade> grades) {
        grades.stream()
                .collect(Collectors.groupingBy(g -> g.getStudent().getGroup()))
                .entrySet().stream()
                .map(e -> {
                    var _group = e.getKey();
                    var _grades = e.getValue();

                    double gpa = _grades.stream().mapToDouble(Grade::getValue).average().orElse(0);
                    long studCount = _grades.stream().map(g -> g.getStudent().getName()).distinct().count();

                    double res = studCount == 0 ? 0 : gpa / studCount;

                    return new AbstractMap.SimpleEntry<>(_group, res);
                })
                .forEach(x -> System.out.println(x.getKey() + "; Average: " + x.getValue()));
    }

    private static double getAverage(List<Grade> grades) {
        double sum = grades.stream().mapToDouble(Grade::getValue).sum();
        return sum / grades.size();
    }

    public static void main(String[] args) {
        var grades = getGrades(getStudents(), getHomeworks());

//        report1(grades, "");
//        report1Better(grades, "");
//        report2(grades, "");
//        report3(grades, 221);
//        report4(grades, 2);
        report5(grades);
    }

    private static List<Student> getStudents() {
        return List.of(
            new Student(1, "Andrei", 221),
            new Student(2, "Marius", 221),
            new Student(3, "Ana", 221),
            new Student(4, "Matei", 222),
            new Student(5, "Alexandru", 221),
            new Student(6, "Vlad", 221),
            new Student(7, "Andrada", 222)
        );
    }

    private static List<Homework> getHomeworks() {
        return List.of(
            new Homework("T1", "D1"),
            new Homework("T2", "D2"),
            new Homework("T3", "D3"),
            new Homework("T4", "D4")
        );
    }

    private static List<Grade> getGrades(List<Student> students, List<Homework> homeworks) {
        return List.of(
                new Grade(students.get(0), homeworks.get(0), 10d, "Prof1"),
                new Grade(students.get(1), homeworks.get(0), 8.5, "Prof2"),
                new Grade(students.get(0), homeworks.get(2), 9.0, "Prof1"),
                new Grade(students.get(2), homeworks.get(0), 10d, "Prof2"),
                new Grade(students.get(1), homeworks.get(1), 8d, "Prof1"),
                new Grade(students.get(2), homeworks.get(2), 5d, "Prof2"),
                new Grade(students.get(1), homeworks.get(2), 6.4, "Prof1"),
                new Grade(students.get(0), homeworks.get(1), 7.5, "Prof1")
        );
    }
}
