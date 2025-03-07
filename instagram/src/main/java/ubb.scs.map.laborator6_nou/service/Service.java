package ubb.scs.map.laborator_6nou.service;

public interface Service<E> {
    E delete(Long ID);
    Iterable<E> findAll();

}