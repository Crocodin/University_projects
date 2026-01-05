package com.ubb.service;

import com.ubb.domain.user.User;

import java.util.*;

/**
 * Provides functionality to find connected communities among users
 * based on friendships.
 * <p>
 * A community is defined as a set of users where each user is directly
 * or indirectly connected to every other user through friendships.
 * This class uses depth-first search (DFS) to explore all connections.
 * </p>
 */
public class ConnectionService {

    /**
     * Finds all communities in a list of users.
     * <p>
     * Each community is represented as a {@link Set} of users.
     * Users connected directly or indirectly through friendships
     * are grouped into the same community.
     * </p>
     *
     * @param allUsers the list of all users to analyze
     * @return a list of communities, each community being a set of connected users
     */
    public static List<Set<User>> findCommunities(List<User> allUsers, FriendService fs) {
        List<Set<User>> communities = new ArrayList<>();
        Set<User> visited = new HashSet<>();

        for (User user : allUsers) {
            if (!visited.contains(user)) {
                // start a new community
                Set<User> community = new HashSet<>();
                DFS(user, visited, community, fs);
                communities.add(community);
            }
        }

        return communities;
    }

    /**
     * Recursively explores a user's friendships to identify all members
     * of the same community.
     * <p>
     * Uses depth-first search (DFS) to traverse the friendship graph.
     * </p>
     *
     * @param user      the current user being explored
     * @param visited   the set of already visited users
     * @param community the set representing the current community
     */
    private static void DFS(User user, Set<User> visited, Set<User> community, FriendService fs) {
        visited.add(user);
        community.add(user);

        for (User friend : fs.getFriends(user)) {
            if (!visited.contains(friend)) {
                DFS(friend, visited, community, fs);
            }
        }
    }

    public static Set<User> biggestCommunity(List<User> allUsers, FriendService fs) {
        Set<User> bestCommunity = null;
        int maxDiameter = 0;

        for (Set<User> community : findCommunities(allUsers, fs)) {
            int diameter = findDiameter(community, fs);
            if (diameter > maxDiameter) {
                maxDiameter = diameter;
                bestCommunity = community;
            }
        }

        return bestCommunity;
    }

    public static int findDiameter(Set<User> community, FriendService fs) {
        if (community.isEmpty()) return 0;

        User start = community.iterator().next();

        return BFSMaxDistance(BFS(start, fs), fs);
    }

    private static User BFS(User start, FriendService fs) {
        Queue<User> queue = new LinkedList<>();
        Set<User> visited = new HashSet<>();
        queue.add(start);
        visited.add(start);

        User last = start;

        while (!queue.isEmpty()) {
            User current = queue.poll();
            last = current;
            for (User friend : fs.getFriends(current)) {
                if (!visited.contains(friend)) {
                    visited.add(friend);
                    queue.add(friend);
                }
            }
        }
        return last;
    }

    private static int BFSMaxDistance(User start, FriendService fs) {
        Queue<User> queue = new LinkedList<>();
        Map<User, Integer> distance = new HashMap<>();
        queue.add(start);
        distance.put(start, 0);

        int maxDistance = 0;

        while (!queue.isEmpty()) {
            User current = queue.poll();
            int currentDist = distance.get(current);

            for (User friend : fs.getFriends(current)) {
                if (!distance.containsKey(friend)) {
                    distance.put(friend, currentDist + 1);
                    queue.add(friend);
                    maxDistance = Math.max(maxDistance, currentDist + 1);
                }
            }
        }

        return maxDistance;
    }
}
