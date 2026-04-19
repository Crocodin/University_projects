using C_FTS.Domain;
using System;
using System.Collections.Generic;
using System.Text;

namespace C_FTS.Service.Authenticator
{
    public interface IAuthenticator
    {
        public User? Authenticate(string username, string password);
    }
}
