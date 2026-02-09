import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class FunctionsTest {


    public static void main(String[] args) {
        Function<String, Integer> stringToInteger = x -> Integer.valueOf(x);
        Function<String, Integer> stringToIntegerRef = Integer::valueOf;

        System.out.println(stringToInteger.apply("10"));
        System.out.println(stringToIntegerRef.apply("10"));
    }
}
