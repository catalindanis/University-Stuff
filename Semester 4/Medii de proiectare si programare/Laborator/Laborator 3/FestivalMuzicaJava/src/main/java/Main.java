import domain.Show;
import domain.Ticket;
import domain.User;
import repository.*;
import utils.JdbcUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        Properties props = new Properties();

        try {
            props.load(Main.class.getClassLoader().getResourceAsStream("db.properties"));
        } catch (IOException e) {
            System.out.println("Cannot find db.properties " + e);
        }

        JdbcUtils jdbcUtils = new JdbcUtils(props);
        jdbcUtils.getConnection();

        UsersRepository usersRepo = new UsersDBRepository(jdbcUtils);
        usersRepo.size();
        usersRepo.findAll().forEach(System.out::println);

        ShowsRepository showsRepository = new ShowsDBRepository(jdbcUtils);
        TicketsRepository ticketsRepo = new TicketsDBRepository(jdbcUtils);

        showsRepository.save(new Show(-1L, "Artist", LocalDate.now(), "location", 100));
        showsRepository.findAll().forEach(System.out::println);

        ticketsRepo.save(new Ticket(-1L, "Client", showsRepository.findAll().iterator().next(), 5));
        ticketsRepo.findAll().forEach(System.out::println);

        showsRepository.delete(showsRepository.findAll().iterator().next().getId());
    }
}
