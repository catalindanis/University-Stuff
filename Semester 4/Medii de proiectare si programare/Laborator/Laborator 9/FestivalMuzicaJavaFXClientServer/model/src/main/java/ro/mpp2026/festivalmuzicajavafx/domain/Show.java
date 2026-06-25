package ro.mpp2026.festivalmuzicajavafx.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "shows")
@jakarta.persistence.Entity
public class Show implements Entity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "artist_name")
    private String artistName;
    @Column(name = "date")
    private LocalDate date;
    @Column(name = "location")
    private String location;
    @Column(name = "remaining_seats")
    private int remainingSeats;

    public Show(Long id, String artistName, LocalDate date, String location, int remainingSeats) {
        this.id = id;
        this.artistName = artistName;
        this.date = date;
        this.location = location;
        this.remainingSeats = remainingSeats;
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
        Show show = (Show) o;
        return remainingSeats == show.remainingSeats && Objects.equals(id, show.id) && Objects.equals(artistName, show.artistName) && Objects.equals(date, show.date) && Objects.equals(location, show.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, artistName, date, location, remainingSeats);
    }

    @Override
    public String toString() {
        return "Show{" +
                "id=" + id +
                ", artistName='" + artistName + '\'' +
                ", date=" + date +
                ", location='" + location + '\'' +
                ", remainingSeats=" + remainingSeats +
                '}';
    }
}
