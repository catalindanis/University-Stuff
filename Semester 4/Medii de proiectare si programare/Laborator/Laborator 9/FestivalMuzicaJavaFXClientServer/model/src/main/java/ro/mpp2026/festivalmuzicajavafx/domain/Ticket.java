package ro.mpp2026.festivalmuzicajavafx.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "tickets")
@jakarta.persistence.Entity
public class Ticket implements Entity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "client_name")
    private String clientName;
    @ManyToOne
    @JoinColumn(name = "show_id")
    private Show show;
    @Column(name = "no_seats")
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
