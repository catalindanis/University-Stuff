package ro.mpp2026.festivalmuzicajavafx.repository;


import ro.mpp2026.festivalmuzicajavafx.domain.Show;
import ro.mpp2026.festivalmuzicajavafx.domain.ShowFilter;

public interface ShowsRepository extends Repository<Long, Show>, FilterableRepository<Show, ShowFilter> {
}
