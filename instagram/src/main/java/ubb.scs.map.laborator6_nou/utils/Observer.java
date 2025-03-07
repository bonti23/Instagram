package ubb.scs.map.laborator_6nou.utils;

import jdk.jfr.Event;

public interface Observer<E extends Event> {
    void update(E e);
}
