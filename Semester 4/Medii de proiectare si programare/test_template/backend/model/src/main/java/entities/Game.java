//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import java.util.List;
//
//@Getter
//@Setter
//@NoArgsConstructor
//@Table(name = "games")
//@jakarta.persistence.Entity
//public class Game implements Entity<Long> {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "config_id")
//    private Configuration configuration;
//
//    @ManyToMany
//    @JoinTable(
//            name="players_games",
//            joinColumns = @JoinColumn(name = "game_id"),
//            inverseJoinColumns = @JoinColumn(name = "player_id")
//    )
//    private List<Player> players;
//
//    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
//    private List<Move> moves;
//}
