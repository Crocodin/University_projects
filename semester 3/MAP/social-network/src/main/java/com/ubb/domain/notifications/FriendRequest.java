package com.ubb.domain.notifications;

import com.ubb.domain.user.User;
import lombok.Getter;
import lombok.Setter;

@Getter
public class FriendRequest extends Notification {
    private final User from;
    private final User to;

    @Setter
    private RequestStatus status;

    public FriendRequest(Long id, User from, User to, RequestStatus status) {
        super(id, NotificationType.FRIENDSHIP);
        this.from = from;
        this.to = to;
        this.status = status;
    }

    public void accept() {
        if (status == RequestStatus.PENDING) {
            status = RequestStatus.ACCEPTED;
        }
    }

    public void reject() {
        if (status == RequestStatus.PENDING) {
            status = RequestStatus.REJECTED;
        }
    }
}
