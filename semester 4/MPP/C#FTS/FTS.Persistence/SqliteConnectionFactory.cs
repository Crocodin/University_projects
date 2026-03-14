using Microsoft.Data.Sqlite;
using System;
using System.Collections.Generic;
using System.Data;
using System.Text;

namespace FTS.Persistence
{
    public class SQLiteConnectionFactory : ConnectionFactory
    {
        public override IDbConnection CreateConnection(IDictionary<string, string> props)
        {
            string connectionString = props["DB_URL"];

            return new SqliteConnection(connectionString);
        }
    }
}
