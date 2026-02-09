package utils.paging;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
public class Page<E> {
    @Getter
    private List<E> elements;
    @Getter
    private int totalNumberOfElements;
}
