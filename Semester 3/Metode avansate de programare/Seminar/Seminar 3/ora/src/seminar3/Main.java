package seminar3;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class Main {
    public static void main(String[] args) {
        Student s1 = new Student("Dan", 4.5f);
        Student s2 = new Student("Ana", 6.5f);
        Student s3 = new Student("Dan", 4.5f);

        Set<Student> students = new HashSet<>();

        students.add(s1);
        students.add(s2);
        students.add(s3);

//        printSet(students);

//        Set<Student> students2 = new TreeSet<>();
        Set<Student> students2 = new TreeSet<>(
                (o1, o2) -> o1.getName().compareTo(o2.getName()));

        students2.add(s1);
        students2.add(s2);

        printSet(students2);
    }

    private static void printSet(Set<Student> students) {
        for(Student student : students) {
            System.out.println(student);
        }
    }
}
