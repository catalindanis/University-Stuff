import java.util.List;

public class FunctionalInterfaceTest {
    public static void main(String[] args) {
        Arie<Circle> circleArea = c -> Math.PI * Math.pow(c.getRadius(), 2);

        Circle c1 = new Circle(3);
        Circle c2 = new Circle(6);

        Arie<Square> squareArea = s -> Math.pow(s.getLength(), 2);

        Square s1 = new Square(2);
        Square s2 = new Square(3);

        var circles = List.of(c1, c2);
        var squares = List.of(s1, s2);

        printArie(circles, circleArea);
        printArie(squares, squareArea);
    }

    private static <E> void printArie(List<E> l, Arie<E> f) {
        l.forEach((E e) -> System.out.println(f.compute(e)));
    }


}
