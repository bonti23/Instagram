package ubb.scs.map.laborator_6nou.domain.event;

import ubb.scs.map.laborator_6nou.domain.User;

public class UserEntityChange extends jdk.jfr.Event {
    private ChangeEventType type;
    private User data, oldData;

    public UserEntityChange(ChangeEventType type, User data) {
        this.type = type;
        this.data = data;
    }
    public UserEntityChange(ChangeEventType type, User data, User oldData) {
        this.type = type;
        this.data = data;
        this.oldData=oldData;
    }

    public ChangeEventType getType() {
        return type;
    }

    public User getData() {
        return data;
    }

    public User getOldData() {
        return oldData;
    }
}
