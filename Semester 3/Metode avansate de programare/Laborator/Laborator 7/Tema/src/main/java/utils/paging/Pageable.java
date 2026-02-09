package utils.paging;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class Pageable {
    private int pageNumber;

    private int pageSize;
}