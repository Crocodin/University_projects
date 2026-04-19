using C_FTS.Domain;
using C_FTS.Repository;
using C_FTS.Repository.DBRepository;
using System;
using System.Collections.Generic;
using System.Text;

namespace C_FTS.Service.Authenticator
{
    public class Authenticator : IAuthenticator
    {
        private readonly IUserRepository _userRepository = new UserRepository();
        public User? Authenticate(string username, string password)
        {
            return _userRepository.FindByUsernameAndPassword(username, password);
        }
    }
}
