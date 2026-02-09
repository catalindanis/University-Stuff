import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class StreamsTest {
    private static <E> List<E> filterGeneric(List<E> l, Predicate<E> p) {
        return l.stream().filter(p).toList();
    }

    private static <E> List<E> filterGeneric(List<E> l, Predicate<E> p, Comparator<E> c) {
        return l.stream().filter(p).sorted(c).toList();
    }

    public static void stringStream() {
        List<String> names = List.of("John", "Vasila", "Bianca", "Dorel", "Elefant");

        var list = names   .stream()
                .filter(e -> e.contains("a"))
                .map(e -> e.toUpperCase())
                .collect(Collectors.toList());

        System.out.println(list);
    }

    public static void main(String[] args) {
        List<Integer> integers = List.of(2, 3, 5, 4, 1);
        System.out.println(filterGeneric(integers, i -> i % 2 == 1));
        System.out.println(filterGeneric(integers, i -> i % 2 == 1, Integer::compare));
        stringStream();
    }
}
