package ro.mpp2026.festivalmuzicajavafx.mappers;

import java.util.List;

public interface Mapper<Entity, DTO> {
    DTO convert(Entity entity);
    default List<DTO> convertToList(List<Entity> entities) {
        return entities.stream().map(this::convert).toList();
    }
}
