package ubb.scs.map.laborator_6nou.repository;

import ubb.scs.map.laborator_6nou.domain.Entity;
import ubb.scs.map.laborator_6nou.domain.validation.Validation;

import java.util.Optional;

public abstract class AbstractDBRepository <ID,E extends Entity<ID>> implements NewRepository<ID,E> {
    private final String url;
    private final String username;
    private final String password;
    private final Validation<E> validator;

    public AbstractDBRepository(String url, String username, String password, Validation<E> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }
    public abstract Optional<E> findOne(ID id);

    public abstract Iterable<E> findAll();

    public abstract Optional<E> save(E entity);

    public abstract Optional<E> delete(ID id);

    public abstract Optional<E> update(E entity);

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Validation<E> getValidator() {
        return validator;
    }
}
