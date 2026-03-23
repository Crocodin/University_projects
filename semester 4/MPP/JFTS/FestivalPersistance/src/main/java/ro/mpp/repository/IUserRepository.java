package ro.mpp.repository;

import ro.mpp.domain.User;

import java.util.Optional;

public interface IUserRepository extends Repository<Integer, User> {
    public Optional<User> findByUsernameAndPassword(String username, String password);
}
