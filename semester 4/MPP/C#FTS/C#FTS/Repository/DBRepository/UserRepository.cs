using C_FTS.Domain;
using C_FTS.Utils;
using log4net;
using System;
using System.Collections.Generic;
using System.Data;
using System.Text;

namespace C_FTS.Repository.DBRepository
{
    public class UserRepository : IUserRepository
    {
        private static readonly ILog logger = LogManager.GetLogger(typeof(UserRepository));
        private Database db = new Database();
        public void Delete(User entity)
        {
            throw new NotImplementedException();
        }

        public User? Find(int id)
        {
            throw new NotImplementedException();
        }

        public List<User> FindAll()
        {
            throw new NotImplementedException();
        }

        public User? FindByUsernameAndPassword(string username, string password)
        {
            logger.InfoFormat("Finding user {0} with password {0}", username, password);
            IDbConnection conn = db.GetConnection();

            using (IDbCommand cmd = conn.CreateCommand())
            {
                cmd.CommandText = "SELECT * FROM user WHERE username = @username AND password = @password";
                IDbDataParameter dpName = cmd.CreateParameter();
                dpName.ParameterName = "@username";
                dpName.Value = username;
                cmd.Parameters.Add(dpName);

                IDbDataParameter dpPass = cmd.CreateParameter();
                dpPass.ParameterName = "@password";
                dpPass.Value = password;
                cmd.Parameters.Add(dpPass);

                using (var dataR = cmd.ExecuteReader())
                {
                    if (dataR.Read())
                    {
                        User user = new User(dataR);
                        logger.InfoFormat("Exiting Find with {0}", user);
                        return user;
                    }
                    logger.Error("Exiting Find with null");
                    return null;
                }
            }
        }

        public User? Save(User entity)
        {
            throw new NotImplementedException();
        }

        public User? Update(User entity)
        {
            throw new NotImplementedException();
        }
    }
}
