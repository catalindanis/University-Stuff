package repositories;

import models.Entity;

public interface DatabaseRepository<ID, E extends Entity<ID>> extends Repository<ID, E> {  }
