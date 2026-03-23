package ro.mpp.authenticator;

import ro.mpp.domain.User;
import ro.mpp.repository.IUserRepository;

import java.util.Optional;

public class Authenticator implements IAuthenticator {
    private final IUserRepository userRepo;

    public Authenticator(IUserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public Optional<User> authenticate(String username, String password) {
        return userRepo.findByUsernameAndPassword(username, password);
    }
}
