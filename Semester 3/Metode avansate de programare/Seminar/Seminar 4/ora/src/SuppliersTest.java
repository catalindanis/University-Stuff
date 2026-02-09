import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.function.Supplier;

public class SuppliersTest {
    public static void main(String[] args) {
        Supplier<ArrayList<?>> arrayListSupplier = ArrayList::new;
        Supplier<ArrayList<String>> arrayListSupplier1 = ArrayList::new;
        Supplier<LocalDateTime> dateTimeSupplier = LocalDateTime::now;
        Supplier<LocalDateTime> dateTimeSupplier1 = () -> LocalDateTime.now();

        System.out.println(arrayListSupplier.get());
        System.out.println(dateTimeSupplier.get());
    }
}
