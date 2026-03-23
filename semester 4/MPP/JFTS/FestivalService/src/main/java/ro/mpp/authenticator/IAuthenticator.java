package ro.mpp.authenticator;

import ro.mpp.domain.User;

import java.util.Optional;

public interface IAuthenticator {
    public Optional<User> authenticate(String username, String password);
}
