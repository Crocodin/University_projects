package com.ubb.domain.connection;

import com.ubb.domain.Entity;
import com.ubb.domain.user.User;
import com.ubb.facade.UserFacade;
import lombok.Getter;
import lombok.Setter;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

/**
 * Represents a message sent from one user to another.
 * A message contains the sender, receiver, message content, and a timestamp indicating when it was sent.
 */
@Getter
@Setter
public class Message extends Entity<Long> {

    /** The user of the message sender. */
    private User from;

    /** The username of the message receiver. */
    private List<User> to;

    /** The content of the message. */
    private String message;

    /** The date and time when the message was sent. */
    private LocalDateTime timestamp;

    /** this is a reply to something, and this is that something */
    private Message replyTo = null;


    /**
     * Constructs a new {@code Message} with all required fields.
     *
     * @param id        the unique identifier for the message
     * @param from      the sender of the message
     * @param to        the receiver of the message
     * @param message   the content of the message
     * @param timestamp the time the message was sent
     */
    public Message(Long id, User from, List<User> to, String message, LocalDateTime timestamp) {
        super(id);
        this.from = from;
        this.to = to;
        this.message = message;
        this.timestamp = timestamp;
    }

    /**
     * Constructs a new {@code Reply Message} with all required fields.
     *
     * @param id        the unique identifier for the message
     * @param from      the sender of the message
     * @param to        the receiver of the message
     * @param message   the content of the message
     * @param timestamp the time the message was sent
     * @param replyTo   to what it's replying
     */
    public Message(Long id, User from, List<User> to, String message, LocalDateTime timestamp, Message replyTo) {
        super(id);
        this.from = from;
        this.to = to;
        this.message = message;
        this.timestamp = timestamp;
        this.replyTo = replyTo;
    }

    public User toGetFirst() {
        return to.getFirst();
    }
}
