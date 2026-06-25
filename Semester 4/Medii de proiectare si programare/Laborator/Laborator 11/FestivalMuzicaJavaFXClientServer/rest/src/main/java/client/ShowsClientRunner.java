package client;

import ro.mpp2026.festivalmuzicajavafx.domain.Show;
import ro.mpp2026.festivalmuzicajavafx.domain.ShowFilter;

import java.time.LocalDate;
import java.util.Arrays;

public class ShowsClientRunner {
    public static void main(String[] args) {
        ShowsClient client = new ShowsClient();

        Show newShow = new Show(null, "Test Artist", LocalDate.now().plusDays(2), "Test Hall", 100);
        Long id = client.save(newShow);
        System.out.println("Created show id: " + id);

        Show created = client.findById(id);
        System.out.println("Fetched: " + created);

        created.setRemainingSeats(80);
        client.update(id, created);
        System.out.println("Updated show id: " + id);

        Show[] all = client.findAll();
        System.out.println("All shows: " + Arrays.toString(all));

        ShowFilter filter = ShowFilter.builder()
                .artistName("Test Artist")
                .build();
        Show[] filtered = client.findAllFiltered(filter);
        System.out.println("Filtered shows: " + Arrays.toString(filtered));

        client.delete(id);
        System.out.println("Deleted show id: " + id);
    }
}

