package repositories;

import models.Entity;
import utils.paging.Page;
import utils.paging.Pageable;

public interface PageableRepository<ID, E extends Entity<ID>, DTO> extends Repository<ID, E> {
    Page<E> getAllOnPage(Pageable pageable, DTO dto);
}
