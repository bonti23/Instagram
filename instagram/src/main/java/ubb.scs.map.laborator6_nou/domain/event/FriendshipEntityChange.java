package ubb.scs.map.laborator_6nou.domain.event;

import ubb.scs.map.laborator_6nou.domain.Friendship;

public class FriendshipEntityChange extends jdk.jfr.Event {
    private ChangeEventType type;
    private Friendship data, oldData;

    public FriendshipEntityChange(ChangeEventType type, Friendship data) {
        this.type = type;
        this.data = data;
    }
    public FriendshipEntityChange(ChangeEventType type, Friendship data, Friendship oldData) {
        this.type = type;
        this.data = data;
        this.oldData=oldData;
    }

    public ChangeEventType getType() {
        return type;
    }

    public Friendship getData() {
        return data;
    }

    public Friendship getOldData() {
        return oldData;
    }
}

