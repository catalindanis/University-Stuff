package ro.mpp2026.festivalmuzicajavafx.network;

import ro.mpp2026.festivalmuzicajavafx.domain.ShowFilter;

public record FindAllShowsRequest(ShowFilter showFilter) implements Request {
}
