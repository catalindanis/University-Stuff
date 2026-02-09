package seminar3;

import java.util.*;

public class MyMap {
    Map<Integer, List<Student>> studentsByGrade;

    public MyMap() {
        this.studentsByGrade = new TreeMap<>(new StudentGradeComparator());
    }

    private static class StudentGradeComparator implements Comparator<Integer> {
        @Override
        public int compare(Integer o1, Integer o2) {
            return o1.compareTo(o2);
        }
    }

    public void addStudent(Student s) {
        int grade = Math.round(s.getGrade());

        var students = this.studentsByGrade.get(grade);
        if (students == null) {
            students = new ArrayList<>();
            this.studentsByGrade.put(grade, students);
        }
//        var students = this.studentsByGrade.computeIfAbsent(grade, k -> new ArrayList<>());

        students.add(s);
    }
}
