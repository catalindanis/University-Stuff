package domain;

import java.util.Objects;

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

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Show getShow() {
        return show;
    }

    public void setShow(Show show) {
        this.show = show;
    }

    public int getNoSeats() {
        return noSeats;
    }

    public void setNoSeats(int noSeats) {
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
