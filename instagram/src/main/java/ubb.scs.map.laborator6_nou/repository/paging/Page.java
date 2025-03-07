package ubb.scs.map.laborator_6nou.repository.paging;

import java.util.stream.Stream;

public interface Page<E> {
    Pageable getPageable();
    Pageable getNextPageable();
    Pageable getPreviousPageable();
    Stream<E> getElements();
}
