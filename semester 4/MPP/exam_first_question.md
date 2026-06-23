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