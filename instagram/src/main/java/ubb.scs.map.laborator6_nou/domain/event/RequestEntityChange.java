package ubb.scs.map.laborator_6nou.domain.event;

import jdk.jfr.Event;
import ubb.scs.map.laborator_6nou.domain.FriendshipRequest;

public class RequestEntityChange extends Event {
    private ChangeEventType type;
    private FriendshipRequest data, oldData;

    public RequestEntityChange(ChangeEventType type, FriendshipRequest data) {
        this.type = type;
        this.data = data;
    }

    public RequestEntityChange(ChangeEventType type, FriendshipRequest data, FriendshipRequest oldData) {
        this.type = type;
        this.data = data;
        this.oldData = oldData;
    }

    public ChangeEventType getType() {
        return type;
    }

    public FriendshipRequest getData() {
        return data;
    }

    public FriendshipRequest getOldData() {
        return oldData;
    }
}
