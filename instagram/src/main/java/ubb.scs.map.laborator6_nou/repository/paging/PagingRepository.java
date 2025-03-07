package ubb.scs.map.laborator_6nou.repository.paging;

import ubb.scs.map.laborator_6nou.domain.Entity;
import ubb.scs.map.laborator_6nou.repository.NewRepository;

public interface PagingRepository<ID, E extends Entity<ID>> extends NewRepository<ID, E> {
    Page<E> findAll(Pageable pageable);
}
