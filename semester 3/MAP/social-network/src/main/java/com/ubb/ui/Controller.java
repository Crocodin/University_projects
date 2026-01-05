package com.ubb.ui;

import com.ubb.domain.event.Event;
import com.ubb.domain.user.User;
import com.ubb.exception.eventException.EventException;
import com.ubb.exception.flockException.FlockException;
import com.ubb.exception.friendException.FriendDoseNotExistsException;
import com.ubb.exception.userException.UserDoseNotExistException;
import com.ubb.exception.userException.UserException;
import com.ubb.facade.UserFacade;
import com.ubb.service.*;
import com.ubb.service.DuckService;
import com.ubb.service.PersonService;
import com.ubb.domain.validator.*;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Scanner;

public class Controller {
    private final FriendService friendService;
    private final EventService eventService;
    private final FlockService flockService;
    private final UserFacade userFacade;
    private final LaneService laneService;

    public Controller() {
        // -------------------------------------------
        var duckService = new DuckService();
        var personService = new PersonService();
        this.userFacade = new UserFacade(duckService, personService);
        this.friendService = new FriendService(userFacade);
        // -------------------------------------------
        this.laneService = new LaneService();
        this.eventService = new EventService(userFacade, duckService, laneService);
        // -------------------------------------------
        this.flockService = new FlockService(duckService);
    }

    public void run() {
        boolean run  = true;
        var sc = new Scanner(System.in);
        while (run) {
            Console.options();

            if (sc.hasNextInt()) {
                switch (sc.nextInt()) {
                    case 1 -> userOptions();
                    case 2 -> friendOptions();
                    case 3 -> graphOptions();
                    case 4 -> eventOptions();
                    case 5 -> laneOptions();
                    case 6 -> flockOptions();
                    case 7 -> run = false;
                    default -> System.out.println("\tInvalid option");
                }
            } else {
                System.out.println("\tMust be an integer");
                sc.nextLine(); // clear invalid input
            }
        }
    }

    private void userOptions() {
        boolean run = true;
        var sc = new Scanner(System.in);
        while (run) {
            Console.userOption();
            if (sc.hasNextInt()) {
                switch (sc.nextInt()) {
                    case 1 -> addUser();
                    case 2 -> removeUser();
                    case 3 -> Console.printUsers(userFacade.getUsers());
                    case 4 -> run = false;
                    default -> System.out.println("\tInvalid option");
                }
            } else {
                System.out.println("\tMust be an integer");
                sc.nextLine(); // clear invalid input
            }
        }
    }

    private void addUser() {
        String line = userFacade.size() + ",";
        Console.singleLine();
        var sc = new Scanner(System.in);
        System.out.print("What is the user type: ");
        String type = sc.nextLine();
        if (type.equalsIgnoreCase("person")) {
            System.out.print("Enter username: ");
            line +=  sc.nextLine() + ",";
            System.out.print("Enter email: ");
            line +=  sc.nextLine() + ",";

            // getting and hashing the password
            System.out.print("Enter password: ");
            String password = sc.nextLine();
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] hashBytes = md.digest(password.getBytes());
                String hashedPassword = String.format("%032x", new BigInteger(1, hashBytes));
                line +=  hashedPassword + ",";
            }
            catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e.getMessage());
            }

            // firstName + "," + lastName + "," + occupation + "," + empathyScore
            System.out.print("Enter first name: ");
            line +=  sc.nextLine() + ",";
            System.out.print("Enter last name: ");
            line +=  sc.nextLine() + ",";
            System.out.print("Enter occupation: ");
            line +=  sc.nextLine() + ",";
            System.out.print("Enter empathy score: ");
            line +=  sc.nextLine();

            ValidatorContext context = new ValidatorContext(new ValidatorPerson());
            if  (context.validate(line)) {
                try {
                    userFacade.getPersonService().addObject(line);
                    System.out.println("User successfully added!");
                } catch (SQLException e) {
                    System.out.println("SQL error: " + e.getMessage());
                }
            }
            else {
                System.out.println("Error adding the user, invalid input!");
            }
        } else if (type.equalsIgnoreCase("duck")) {
            System.out.print("Enter username: ");
            line +=  sc.nextLine() + ",";
            System.out.print("Enter email: ");
            line +=  sc.nextLine() + ",";
            System.out.print("Enter password: ");
            line +=  sc.nextLine() + ",";
            // type + "," + speed + "," + endurance
            System.out.print("Enter type: ");
            line +=  sc.nextLine() + ",";
            System.out.print("Enter speed: ");
            line +=  sc.nextLine() + ",";
            System.out.print("Enter endurance: ");
            line +=  sc.nextLine();

            ValidatorContext context = new ValidatorContext(new ValidatorDuck());
            if (context.validate(line)) {
                try {
                    userFacade.getDuckService().addObject(line);
                    System.out.println("User successfully added!");
                } catch (SQLException e) {
                    System.out.println("SQL error: " + e.getMessage());
                }
            }
            else {
                System.out.println("Error adding the user, invalid input!");
            }
        }
        else System.out.println("\tInvalid option; it must be a person or a duck");
    }

    private void removeUser() {
        Long id;
        var sc = new Scanner(System.in);
        System.out.print("The ID of the user you want to remove: ");
        try {
            id = sc.nextLong();
            User u = userFacade.removeUserId(id);
            for (Event e : eventService.getObjects()) {
                if (e.getSubscribers().contains(u))
                    e.unsubscribe(u);
            }
            System.out.println("User successfully removed!");
        } catch (UserDoseNotExistException e) {
            System.out.println(e.getMessage());
        }
        catch (Exception e) {
            System.out.println("Invalid input! " + e.getMessage());
            sc.nextLine(); // clear invalid input
        }
    }

    private void friendOptions() {
        boolean run = true;
        var sc = new Scanner(System.in);
        while (run) {
            Console.friendOption();
            if  (sc.hasNextInt()) {
                switch (sc.nextInt()) {
                    case 1 -> Console.printFriends(friendService.getObjects());
                    case 2 -> addFriendship();
                    case 3 -> removeFriendship();
                    case 4 -> run = false;
                    default -> System.out.println("Invalid option");
                }
            } else {
                System.out.println("\tMust be an integer");
                sc.nextLine(); // clear invalid input
            }
        }
    }

    private void addFriendship() {
        var sc = new Scanner(System.in);
        try {
            System.out.print("The id of the first user: ");
            int id1 = sc.nextInt();
            System.out.print("The id of the second user: ");
            int id2 = sc.nextInt();
            ValidatorContext context = new ValidatorContext(new ValidatorFriend());
            if (context.validate(id1+","+id2)) {
                friendService.addObject(friendService.getObjects().size() + "," + id1 + "," + id2);
                System.out.println("Friendship successfully added!");
            }
            else {
                System.out.println("Error adding the friend, invalid ID input!");
            }
        } catch (UserException e) {
            System.out.println(e.getMessage());
        }
        catch (Exception e) {
            System.out.println("Invalid input!");
            sc.nextLine(); // clear invalid input
        }
    }

    private void removeFriendship() {
        var sc = new Scanner(System.in);
        try {
            System.out.print("The id of the first user: ");
            long id1 = sc.nextLong();
            System.out.print("The id of the second user: ");
            long id2 = sc.nextLong();
            friendService.removeObject(
                    friendService.findFriendShip(id1, id2)
            );
            System.out.println("Friendship successfully removed!");
        } catch (UserDoseNotExistException | FriendDoseNotExistsException e) {
            System.out.println(e.getMessage());
        }
        catch (Exception e) {
            System.out.println("Invalid input!");
            sc.nextLine(); // clear invalid input
        }
    }

    private void graphOptions() {
        boolean run = true;
        var sc = new Scanner(System.in);
        while (run) {
            Console.graphOption();

            if (sc.hasNextLong()) {
                switch (sc.nextInt()) {
                    case 1 -> System.out.println("The number of communities is " + ConnectionService.findCommunities(userFacade.getUsers(), friendService).size());
                    case 2 -> {
                        Console.printUsers(ConnectionService.biggestCommunity(
                                userFacade.getUsers(), friendService).stream().toList());
                    }
                    case 3 -> run = false;
                    default -> System.out.println("Invalid option");
                }
            } else {
                System.out.println("\tMust be an integer");
                sc.nextLine(); // clear invalid input
            }
        }
    }

    private void eventOptions() {
        boolean run = true;
        var sc = new Scanner(System.in);
        while (run) {
            Console.eventOption();

            if (sc.hasNextLong()) {
                switch (sc.nextInt()) {
                    case 1 -> addEvent();
                    case 2 -> removeEvent();
                    case 3 -> subscribeUser();
                    case 4 -> unsubscribeUser();
                    case 5 -> Console.printEvents(eventService.getObjects());
                    case 6 -> notifySubject();
                    case 7 -> run = false;
                    default -> System.out.println("Invalid option");
                }
            } else {
                System.out.println("\tMust be an integer");
                sc.nextLine(); // clear invalid input
            }
        }
    }

    private void notifySubject() {
        long id;
        var sc = new Scanner(System.in);
        System.out.print("The ID of the event you want to notify his subscribers: ");
        if (sc.hasNextLong()) {
            id = sc.nextLong();
            try {
                Event e = eventService.findId(id);
                //e.notifySubscribers();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Invalid input!");
        }
    }

    private void addEvent() {
        var  sc = new Scanner(System.in);
        try {
            System.out.print("Enter type of event: ");
            String type = sc.nextLine();
            if (type.equalsIgnoreCase("race")) {
                String line = "RE," + eventService.getObjects().size() + ",";
                System.out.print("Please enter a name: ");
                line += sc.nextLine() + ",";
                ValidatorContext context = new ValidatorContext(new ValidatorEvent());
                if (context.validate(line)) {
                    eventService.addObject(line);
                    System.out.println("Event successfully added!");
                }
                else {
                    System.out.println("Error adding the event, invalid input!");
                }
            }
        }
        catch (Exception e) {
            System.out.println("Invalid input!");
            sc.nextLine(); // clear invalid input
        }
    }

    private void removeEvent() {
        long id;
        var sc = new Scanner(System.in);
        System.out.print("The ID of the event you want to remove: ");
        try {
            id = sc.nextLong();
            eventService.removeObject(eventService.findId(id));
            System.out.println("Event successfully removed!");
        } catch (UserDoseNotExistException e) {
            System.out.println(e.getMessage());
        }
        catch (Exception e) {
            System.out.println("Invalid input!");
            sc.nextLine(); // clear invalid input
        }
    }

    private void subscribeUser() {
        var sc = new Scanner(System.in);
        try {
            System.out.print("Enter id of the event: ");
            Long idE = sc.nextLong();
            System.out.print("Enter id of the user: ");
            Long idU = sc.nextLong();
            eventService.addSubscriber(idE, userFacade.getUser(idU));
            System.out.println("User successfully subscribed!");
        }
        catch (SQLException | EventException | UserException e) {
            System.out.println(e.getMessage());
        }
        catch (Exception e) {
            System.out.println("Invalid input!");
            sc.nextLine(); // clear invalid input
        }
    }

    private void unsubscribeUser() {
        var sc = new Scanner(System.in);
        try {
            System.out.print("Enter id of the event: ");
            Long idE = sc.nextLong();
            System.out.print("Enter id of the user: ");
            Long idU = sc.nextLong();
            eventService.removeSubscriber(idE, userFacade.getUser(idU));
            System.out.println("User successfully unsubscribed!");
        }
        catch (EventException | UserException e) {
            System.out.println(e.getMessage());
        }
        catch (Exception e) {
            System.out.println("Invalid input!");
            sc.nextLine(); // clear invalid input
        }
    }

    private void laneOptions() {
        boolean run = true;
        var sc = new Scanner(System.in);
        while (run) {
            Console.laneOptions();

            if (sc.hasNextLong()) {
                switch (sc.nextInt()) {
                    case 1 -> addLane();
                    case 2 -> removeLane();
                    case 3 -> Console.printLanes(laneService.getObjects());
                    case 4 -> run = false;
                }
            } else {
                System.out.println("Invalid input!");
                sc.nextLine();
            }
        }
    }

    private void addLane() {
        try {
            String line = laneService.getObjects().size() + ",";
            System.out.print("Please enter a distance: ");
            var sc = new Scanner(System.in);
            if (sc.hasNextLong()) {
                line += sc.nextLine();
                ValidatorContext context = new ValidatorContext(new ValidatorLane());
                if (context.validate(line)) {
                    laneService.addObject(line);
                    System.out.println("Lane successfully added!");
                } else {
                    System.out.println("Error adding the lane!");
                }
            } else {
                System.out.println("Invalid input!");
                sc.nextLine();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void removeLane() {
        Long id;
        var sc = new Scanner(System.in);
        System.out.print("The ID of the lane you want to remove: ");
        try {
            id = sc.nextLong();
            laneService.removeObject(laneService.findId(id));
        } catch (UserDoseNotExistException e) {
            System.out.println(e.getMessage());
        }
        catch (Exception e) {
            System.out.println("Invalid input!");
            sc.nextLine(); // clear invalid input
        }
    }

    private void flockOptions() {
        boolean run = true;
        var sc = new Scanner(System.in);
        while (run) {
            Console.flockOption();
            if (sc.hasNextInt()) {
                switch (sc.nextInt()) {
                    case 1 -> addFlock();
                    case 2 -> removeFlock();
                    case 3 -> Console.printFlocks(flockService.getObjects());
                    case 4 -> addToFlock();
                    case 5 -> removeFromFlock();
                    case 6 -> run = false;
                }
            } else {
                System.out.println("Invalid input!");
                sc.nextLine();
            }
        }
    }

    private  void addFlock() {
        try {
            var sc = new Scanner(System.in);
            System.out.print("Please enter the name of the flock: ");
            String data = flockService.getObjects().size() + ",";
            data += sc.nextLine();
            ValidatorContext context = new ValidatorContext(new ValidatorFlock());
            if (context.validate(data)) {
                flockService.addObject(data);
                System.out.println("Flock successfully added!");
            } else {
                System.out.println("Error adding the flock!");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void removeFlock() {
        try {
            Long id;
            var sc = new Scanner(System.in);
            System.out.print("The ID of the flock you want to remove: ");
            if (sc.hasNextLong()) {
                id = sc.nextLong();
                flockService.removeObject(flockService.findId(id));
                System.out.println("Flock successfully removed!");
            } else {
                System.out.println("Invalid input!");
                sc.nextLine();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void addToFlock() {
        var sc = new Scanner(System.in);
        try {
            System.out.print("Enter id of the flock: ");
            Long idF = sc.nextLong();
            System.out.print("Enter id of the duck: ");
            Long idD = sc.nextLong();
            flockService.addToFlock(idF, idD);
            System.out.println("Duck successfully added to the flock!");
        }
        catch (FlockException | UserException e) {
            System.out.println(e.getMessage());
        }
        catch (Exception e) {
            System.out.println("Invalid input!");
            sc.nextLine(); // clear invalid input
        }
    }

    private void removeFromFlock() {
        var sc = new Scanner(System.in);
        try {
            System.out.print("Enter id of the flock: ");
            Long idF = sc.nextLong();
            System.out.print("Enter id of the duck: ");
            Long idD = sc.nextLong();
            flockService.removeFromFlock(idF, idD);
            System.out.println("Duck successfully removed from the flock!");
        }
        catch (EventException | UserException e) {
            System.out.println(e.getMessage());
        }
        catch (Exception e) {
            System.out.println("Invalid input!");
            sc.nextLine(); // clear invalid input
        }
    }
}
