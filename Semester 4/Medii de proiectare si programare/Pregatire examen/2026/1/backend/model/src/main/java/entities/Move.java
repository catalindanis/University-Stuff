package entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Table(name = "moves")
@NoArgsConstructor
@jakarta.persistence.Entity
public class Move implements Entity<Long> {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id")
    private Player player;

    @Column(name = "generated_number")
    private int generatedNumber;
    private int round;
    private int position;
    private int points;
}
