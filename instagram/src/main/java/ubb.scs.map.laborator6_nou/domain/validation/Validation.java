package ubb.scs.map.laborator_6nou.domain.validation;


public interface Validation<T>{
    void validate(T entity) throws ValidationException;
}

