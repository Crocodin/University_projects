using C_FTS.Service;
using C_FTS.Service.Authenticator;

namespace FTS.Networking.Networking.Service
{
    public interface IFestivalSubject : IFestivalService, IAuthenticator
    {
        void Login(string username, string password, IFestivalObserver observer);
        void Logout(string username);
        void SetObserver(IFestivalObserver observer);
    }
}
