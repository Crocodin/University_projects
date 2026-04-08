package ro.mpp.observer;

import ro.mpp.authenticator.IAuthenticator;

public interface IFestivalService extends ro.mpp.service.IFestivalService, IAuthenticator {
    void login(String username, String password, IFestivalObserver observer);
    void logout(String username);
    void setObserver(IFestivalObserver observer);
}
