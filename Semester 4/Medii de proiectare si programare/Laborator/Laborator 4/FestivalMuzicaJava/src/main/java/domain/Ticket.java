package domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class Ticket implements Entity<Long> {
    private Long id;
    private String clientName;
    private Show show;
    private int noSeats;

    public Ticket(Long id, String clientName, Show show, int noSeats) {
        this.id = id;
        this.clientName = clientName;
        this.show = show;
        this.noSeats = noSeats;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return noSeats == ticket.noSeats && Objects.equals(id, ticket.id) && Objects.equals(clientName, ticket.clientName) && Objects.equals(show, ticket.show);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, clientName, show, noSeats);
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", clientName='" + clientName + '\'' +
                ", show=" + show +
                ", noSeats=" + noSeats +
                '}';
    }
}
