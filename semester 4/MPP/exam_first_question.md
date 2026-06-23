## The answers to the first question from the exams

### What is a message broker?
A message broker is a piece of software (or service) that sits between different applications, services, or components and handles the passing of messages between them.

```java
private final SimpMessagingTemplate simpMessagingTemplate;

public void sendMessage(String destination, String message) {
    simpMessagingTemplate.convertAndSend(destination, message);
}
```

### How can you map a inheritance hierarchy in ORM?
1. **Single Table Inheritance**: All classes in the hierarchy are mapped to a single table, with a discriminator column to differentiate between types.

```sql
CREATE TABLE Player (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    player_type VARCHAR(50),
    admin_level VARCHAR(255) -- This column will be used for AdminPlayer, but will be NULL for Player
);
```

```java
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "player_type")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}

@Entity
@DiscriminatorValue("ADMIN")
public class AdminPlayer extends Player {
    private String adminLevel;
}
```

2. **Joined Table Inheritance**: Each class in the hierarchy is mapped to its own table, and the tables are joined using foreign keys.

```sql
CREATE TABLE player (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255)
);

CREATE TABLE admin_player (
    id BIGINT PRIMARY KEY,
    admin_level VARCHAR(255),
    FOREIGN KEY (id) REFERENCES player(id)
);
```

```java
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
}

@Entity
public class AdminPlayer extends Player {
    private String adminLevel;
}
```

3. **Table Per Class Inheritance**: Each class in the hierarchy is mapped to its own table, and there is no relationship between the tables.

```sql
CREATE TABLE admin_player (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255),
    admin_level VARCHAR(255)
);

CREATE TABLE regular_player (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255),
    score INT
);
```

```java
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;
}

@Entity
public class AdminPlayer extends Player {
    private String adminLevel;
}

@Entity
public class RegularPlayer extends Player {
    private Integer score;
}
```

### Stateless Dependency in a REST Architecture?
Each request from a client to the server must contain all the information needed for the server to understand and process it. The server doesn't store any session/context about the client between requests. Every request is independent!

### ORM technique for static field mapping?
In ORM, static fields are typically not mapped to database. However, you can map a static field with a workaround by creating a separate entity or table to hold the static data, or by using a singleton pattern to manage the static data in your application code.

JPA's specification explicitly excludes static and transient (the Java keyword, not @Transient) from database mapping. (@Transient annotation is used to indicate that a field should not be persisted in the database.)

```java
@Entity
public class Word {
    @Id private Integer id;

    private String word;
    @Transient private String maskedView; // exists on the object, but never saved to the DB
}
```

### What is Inversion of Control (IoC)?
Instead of your code creating and controlling its own dependencies, an external container creates and supplies them to you. 

### How does the Proxy pattern work?
The Proxy pattern provides a surrogate or placeholder for another object to control access to it. The proxy object implements the same interface as the real object and delegates requests to it, while adding additional behavior.

<img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTRUJGN9l8zVX9-3NEsbA34gFy6pMLzuSsxgUnNScARBw&s=10" alt="Proxy Pattern Diagram" width="500" />

### Publish-Subscribe pattern, what is it?
Is an asynchronous messaging pattern where the publisher sends messages yo an intermediary (a message broker) without knowledge of the subscribers. Subscribers express interest in certain messages and receive them from the broker.

**What's the difference between Publish-Subscribe and Observer pattern?**
- **Publish-Subscribe**: The publisher and subscribers are decoupled.
- **Observer**: The subject maintains a list of observers and notifies them directly when its state changes.

### JWT (JSON Web Token) and how it works?
It is a transfer with _claims_. A _claim_ is a piece of info encoded in a JWT and then digitally signed. The JWT is composed of three parts: Header, Payload, and Signature.

<img src="https://miro.medium.com/v2/resize:fit:1100/format:webp/0*0ZeSNTnn1pZhkpZQ.png" alt="JWT Structure" />

<img src="https://media.geeksforgeeks.org/wp-content/uploads/20250908173351627043/client.webp" alt="JWT Flow" />

### Dependency Injection (DI) what's the deal with it?
Just: higher-level modules should not depend on lower-level modules. Both should depend on abstractions. Abstractions should not depend on details. Details should depend on abstractions.

### What is ORM Impedance Mismatch?
In OOP we have complex data structures, while in relational databases we have tables with rows and columns. The philosophical mismatch between these two models is known as ORM Impedance Mismatch. It can lead to bottlenecks in performance and difficulties in mapping between the two paradigms.

### Point-to-Point vs Point-to-Multipoint communication?
- **Point-to-Point**: A single sender communicates with a single receiver. Each message is sent directly to the intended recipient. Example: A direct message in a chat application.
- **Point-to-Multipoint**: A single sender communicates with multiple receivers. The message is broadcast to all interested parties. Example: A live streaming event.

**Point-to-Point vs P2P (Peer-to-Peer)**:
- **Point-to-Point**: A communication model where a single sender communicates with a single receiver. It is typically used in client-server architectures.
- **Peer-to-Peer (P2P)**: A decentralized communication model where each participant can act as both a sender and a receiver. In P2P networks, nodes communicate directly with each other without relying on a central server.

### How does the WebSocket protocol work?
WebSocket is a protocol that provides full-duplex communication channels over a single TCP connection. It starts with an HTTP handshake to establish the connection, and once established, it allows for continuous, bidirectional communication between the client and server.

### DTO?
A design pattern used to transfer data between software application subsystems. DTOs are often used to encapsulate data and send it from one subsystem of an application to another. They are typically simple objects that do not contain any business logic.