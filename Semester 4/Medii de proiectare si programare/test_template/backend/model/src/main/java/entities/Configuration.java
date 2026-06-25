//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import java.util.List;
//
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@jakarta.persistence.Entity
//public class Configuration implements Entity<Long> {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(name = "number_of_players")
//    private int numberOfPlayers;
//
//    @ElementCollection(fetch = FetchType.EAGER)
//    @CollectionTable(name = "config_points", joinColumns = @JoinColumn(name = "config_id"))
//    @Column(name = "point")
//    private List<Integer> points;
//}
