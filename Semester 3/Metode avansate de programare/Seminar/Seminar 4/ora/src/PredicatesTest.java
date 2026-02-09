import java.util.List;
import java.util.function.Predicate;

public class PredicatesTest {
    private static <E> void printList(List<E> list, Predicate<E> predicate) {
        list.forEach(e -> {
            if(predicate.test(e))
                System.out.println(e);
        });
    }

    public static void main(String[] args) {
        Square s1 = new Square(2);
        Square s2 = new Square(3);

        var squares = List.of(s1, s2);

        printList(squares, s -> s.getLength() > 2);
    }
}
