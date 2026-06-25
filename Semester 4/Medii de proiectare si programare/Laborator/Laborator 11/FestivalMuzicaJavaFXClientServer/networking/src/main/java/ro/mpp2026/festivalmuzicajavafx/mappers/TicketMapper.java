package ro.mpp2026.festivalmuzicajavafx.mappers;

import ro.mpp2026.festivalmuzicajavafx.domain.Show;
import ro.mpp2026.festivalmuzicajavafx.domain.Ticket;
import ro.mpp2026.festivalmuzicajavafx.dto.ShowResponseDTO;
import ro.mpp2026.festivalmuzicajavafx.dto.TicketResponseDTO;

public class TicketMapper implements Mapper<Ticket, TicketResponseDTO> {
    private final Mapper<Show, ShowResponseDTO> showMapper = new ShowMapper();

    @Override
    public TicketResponseDTO convert(Ticket ticket) {
        return new TicketResponseDTO(
                ticket.getId(),
                ticket.getClientName(),
                showMapper.convert(ticket.getShow()),
                ticket.getNoSeats()
        );
    }
}
