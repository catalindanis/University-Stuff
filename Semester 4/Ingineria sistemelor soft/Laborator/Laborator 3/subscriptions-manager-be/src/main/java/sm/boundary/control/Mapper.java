package sm.boundary.control;

import java.util.List;

public interface Mapper<Entity, DTO> {
    DTO toDTO(Entity entity);
    default List<DTO> toDTOList(List<Entity> entities) { return entities.stream().map(this::toDTO).toList(); }
}
