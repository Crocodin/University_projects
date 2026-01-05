package com.ubb.ui;

import com.ubb.domain.connection.Friendship;
import com.ubb.domain.duck.Duck;
import com.ubb.domain.event.Event;
import com.ubb.domain.event.RaceEvent;
import com.ubb.domain.flock.Flock;
import com.ubb.domain.lane.Lane;
import com.ubb.domain.user.User;

import java.util.List;

public class Console {

    public static void dubleLine() {
        System.out.println("================================================================================");
    }

    public static void singleLine() {
        System.out.println("--------------------------------------------------------------------------------");
    }

    public static void title() {
        System.out.println(
                """
                         ____  _   _  ____ _  ______     ___     _   _ _   _ __  __    _    _   _ ____
                        |  _ \\| | | |/ ___| |/ / ___|   ( _ )   | | | | | | |  \\/  |  / \\  | \\ | / ___|
                        | | | | | | | |   | ' /\\___ \\   / _ \\/\\ | |_| | | | | |\\/| | / _ \\ |  \\| \\___ \\
                        | |_| | |_| | |___| . \\ ___) | | (_>  < |  _  | |_| | |  | |/ ___ \\| |\\  |___) |
                        |____/ \\___/ \\____|_|\\_\\____/   \\___/\\/ |_| |_|\\___/|_|  |_/_/   \\_\\_| \\_|____/
                        """
        );
    }

    public static void options() {
        dubleLine();
        title();
        singleLine();
        System.out.print("""
                
                                                <<   OPTIONS   >>
                
                \t1) User options
                \t2) Friendship options
                \t3) Graph options
                \t4) Event options
                \t5) Lane options
                \t6) Flock options
                \t7) Exit
                
                \t\tYour option:""");
    }

    public static void laneOptions() {
        dubleLine();
        System.out.print("""
                
                                              <<   LANE OPTIONS   >>
                
                \t1) Add lane
                \t2) Remove lane
                \t3) Show all lanes
                \t4) Go back
                
                \t\tYour option:""");
    }

    public static void userOption() {
        dubleLine();
        System.out.print("""
                
                                              <<   USER OPTIONS   >>
                
                \t1) Add user
                \t2) Remove user
                \t3) Show all users
                \t4) Go back
                
                \t\tYour option:""");
    }

    public static void printUsers(List<User> users) {
        singleLine();
        for (User user : users) {
            System.out.println("\t└─ " + user.getId() + " - " + user.getUsername());
        }
    }

    public static void printLanes(List<Lane> lanes) {
        singleLine();
        for (Lane lane : lanes) {
            System.out.println("\t id:" + lane.getId() + " -> " + lane.getDistance());
        }
    }

    public static void printFriends(List<Friendship> friendShips) {
        singleLine();
        for (Friendship f : friendShips) {
            System.out.println(f.getId() + " -> " + f.getUserOne().getId() + ": " + f.getUserOne().getUsername() + " - " + f.getUserTwo().getId() + ": " + f.getUserTwo().getUsername());
        }
    }

    public static void friendOption() {
        dubleLine();
        System.out.print("""
                
                                           <<   FRIENDSHIP OPTIONS   >>
                
                \t1) Show all friendships
                \t2) Add a friendship
                \t3) Remove a  friendship
                \t4) Go back
                
                \t\tYour option:""");
    }

    public static void eventOption() {
        dubleLine();
        System.out.print("""
                
                                              <<   EVENT OPTIONS   >>
                
                \t1) Add event
                \t2) Remove event
                \t3) Subscribe person to event
                \t4) Unsubscribe person to event
                \t5) Show all events
                \t6) Notify subjects
                \t7) Go back
                
                \t\tYour option:""");
    }

    public static void printEvents(List<Event> events) {
        singleLine();
        for (Event event : events) {
            System.out.println("\t└─ " + event.getId() + " - " + event.getName());
            if (event instanceof RaceEvent) {
                System.out.print("Ducks: ");
                for (User d: ((RaceEvent) event).getDucks()) {
                    System.out.print("id: " + d.getId() + " - " + d.getUsername() + ", ");
                }
                System.out.println();
                System.out.println("Best time is: " + ((RaceEvent) event).getTime());
            }
            for (User sub : event.getSubscribers()) {
                System.out.println("\t\t└─ " + sub.getId() + " - " + sub.getUsername());
            }
        }
    }

    public static void graphOption() {
        dubleLine();
        System.out.print("""
                
                                             <<   GRAPH OPTIONS   >>
                
                \t1) Number of comunities
                \t2) The comunity with the longest connection chaine
                \t3) Go back
                
                \t\tYour option:""");
    }

    public static void flockOption() {
        dubleLine();
        System.out.print("""
                \s
                                              <<   FLOCK OPTIONS   >>
                \s
                \t1) Add flock
                \t2) Remove flock
                \t3) Show all flocks
                \t4) Add duck to flock
                \t5) Remove duck to flock
                \t6) Go back
                \s
                \t\tYour option:""");
    }

    public static void printFlocks(List<Flock> flocks) {
        singleLine();
        for (Flock flock : flocks) {
            System.out.println("\t└─ " + flock.getId() + " - " + flock.getName() + " performance avg: " + flock.getPerformanceAvg());
            for (Duck d : flock.getDucks()) {
                System.out.println("\t\t└─ " + d.getId() + " - " + d.getUsername());
            }
        }
    }
}