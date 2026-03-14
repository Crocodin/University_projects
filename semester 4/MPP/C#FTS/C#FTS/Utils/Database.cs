using FTS.Persistence;
using System;
using System.Collections.Generic;
using System.Data;
using System.Text;

namespace C_FTS.Utils
{
    public class Database
    {
        private IDbConnection Connection;
        private IDictionary<string, string> Prop;
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
