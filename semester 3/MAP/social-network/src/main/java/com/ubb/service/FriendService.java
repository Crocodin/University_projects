package com.ubb.service;

import com.ubb.config.Config;
import com.ubb.domain.connection.FriendShip;
import com.ubb.domain.user.User;
import com.ubb.facade.UserFacade;
import com.ubb.repo.database.FriendShipDBRepo;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FriendService extends DBService<FriendShip> {
    public FriendService(UserFacade userFacade) {
        super(
                new FriendShipDBRepo(
                        Config.getProperties().getProperty("DB_URL"),
                        Config.getProperties().getProperty("DB_USER"),
                        Config.getProperties().getProperty("DB_PASSWORD"),
                        userFacade
                ),
                data -> {
                    Map<Long, User> users = userFacade.getUsers().stream().collect(Collectors.toMap(User::getId, user -> user));
                    return FriendShip.fromString(data, users);
                }
        );
    }

    public FriendShip findFriendShip(long id1, long id2) throws SQLException {
        var answer = ((FriendShipDBRepo) dbRepo).getObjectViaKeys(id1, id2);
        return answer.orElse(null);
    }

    public List<User> getFriends(User user) {
        return getObjects().stream()
                .filter(f -> f.getUserOne().equals(user) || f.getUserTwo().equals(user))
                .map(f -> f.getUserOne().equals(user) ? f.getUserTwo() : f.getUserOne())
                .collect(Collectors.toList());
    }
}
