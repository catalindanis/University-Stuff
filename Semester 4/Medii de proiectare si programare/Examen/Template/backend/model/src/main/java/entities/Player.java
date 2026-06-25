package entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "players")
@jakarta.persistence.Entity
public class Player implements Entity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;

    private int age;

    public Player(Long id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }

    @ManyToMany(mappedBy = "players", fetch = FetchType.EAGER)
    private List<Game> games;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "player")
    private List<Move> moves;
}
