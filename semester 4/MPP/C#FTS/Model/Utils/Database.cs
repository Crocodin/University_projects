using FTS.Persistence;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data;

namespace C_FTS.Utils
{
    public class Database
    {
        private IDbConnection Connection;
        private IDictionary<string, string> Prop;

        public Database()
        {
            Prop = new Dictionary<string, string>
            {
                { "DB_URL", ConfigurationManager.AppSettings["DB_URL"] }
            };
        }

        public IDbConnection GetConnection()
        {
            if (Connection == null || Connection.State == ConnectionState.Closed)
            {
                Connection = GetNewConnection();
                Connection.Open();
            }
            return Connection;
        }

        private IDbConnection GetNewConnection()
        {
            return ConnectionFactory.GetInstance().CreateConnection(Prop);
        }
    }
}
