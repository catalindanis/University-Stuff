package sm.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "subscriptions")
public class Subscription {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "author_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User author;

    @JoinColumn(name = "subscription_category_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private SubscriptionCategory subscriptionCategory;

    @Column(name = "start_date")
    private LocalDate startDate;
}
