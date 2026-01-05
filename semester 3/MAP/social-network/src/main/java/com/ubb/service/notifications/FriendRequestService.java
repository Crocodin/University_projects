package com.ubb.service.notifications;

import com.ubb.config.Config;
import com.ubb.domain.notifications.FriendRequest;
import com.ubb.domain.notifications.RequestStatus;
import com.ubb.facade.UserFacade;
import com.ubb.repo.database.notifications.FriendRequestDBRepo;

import java.sql.SQLException;
import java.util.List;

public class FriendRequestService {
    private final FriendRequestDBRepo repository;

    public FriendRequestService(UserFacade userFacade) {
        this.repository = new FriendRequestDBRepo(
                Config.getProperties().getProperty("DB_URL"),
                Config.getProperties().getProperty("DB_USER"),
                Config.getProperties().getProperty("DB_PASSWORD"),
                userFacade
        );
    }

    public List<FriendRequest> getRequestsForUser(Long userId) throws SQLException {
        return repository.findForUser(userId);
    }

    public FriendRequest sendRequest(FriendRequest request) throws SQLException {
        return repository.add(request).orElseThrow();
    }

    public void accept(FriendRequest request) throws SQLException {
        repository.updateStatus(request.getId(), RequestStatus.ACCEPTED);
    }

    public void reject(FriendRequest request) throws SQLException {
        repository.updateStatus(request.getId(), RequestStatus.REJECTED);
    }

    public void remove(FriendRequest request) throws SQLException {
        repository.remove(request);
    }
}
