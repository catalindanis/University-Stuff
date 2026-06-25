package ro.mpp2026.festivalmuzicajavafx.mappers;

import ro.mpp2026.festivalmuzicajavafx.domain.Show;
import ro.mpp2026.festivalmuzicajavafx.dto.ShowResponseDTO;

public class ShowMapper implements Mapper<Show, ShowResponseDTO> {
    @Override
    public ShowResponseDTO convert(Show show) {
        return new ShowResponseDTO(
                show.getId(),
                show.getArtistName(),
                show.getDate(),
                show.getLocation(),
                show.getRemainingSeats()
        );
    }
}
