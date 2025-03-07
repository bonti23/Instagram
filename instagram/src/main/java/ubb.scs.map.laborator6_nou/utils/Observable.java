package ubb.scs.map.laborator_6nou.utils;

import jdk.jfr.Event;

public interface Observable<E extends Event> {
    void addObserver(Observer<E> e);
    void removeObserver(Observer<E> e);
    void notifyObservers(E t);
}
