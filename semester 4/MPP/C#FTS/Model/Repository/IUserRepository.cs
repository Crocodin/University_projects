using C_FTS.Domain;
using System;
using System.Collections.Generic;
using System.Text;

namespace C_FTS.Repository
{
    public interface IUserRepository : IRepository<int, User>
    {
        public User? FindByUsernameAndPassword(string username, string password);
    }
}
