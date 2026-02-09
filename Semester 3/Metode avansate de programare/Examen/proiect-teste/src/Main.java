import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class EmailSender {
    private String message;
    public EmailSender(String s) { message=s;}
    public String run() {
        System.out.print(message+" ");
        return "done";
    }
}
class G30 {

    static int test() {
        try {
            System.out.print("A");
            return 1;
        } catch (RuntimeException e) {
            System.out.print("B");
            return 2;
        } finally {
            System.out.print("C");
        }
    }
    public static void main(String[] args) {
        List<String> names = List.of("ana", "bob", "ana");

        System.out.println(
                names.stream().filter(name -> name.equals("ana")).distinct().findFirst().orElse("")
        );
    }
}

