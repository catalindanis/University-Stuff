package entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Table(name = "players")
@NoArgsConstructor
@jakarta.persistence.Entity
public class Player implements Entity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;

    public Player(Long id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }

    @ManyToMany(mappedBy = "players", fetch = FetchType.EAGER)
    private List<Game> games;
}
