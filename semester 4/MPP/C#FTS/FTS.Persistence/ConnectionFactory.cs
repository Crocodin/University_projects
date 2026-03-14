using System;
using System.Data;
using System.Reflection;
using System.Collections.Generic;

namespace FTS.Persistence
{
    public abstract class ConnectionFactory
    {
        public ConnectionFactory() { }

        private static ConnectionFactory Instance;

        public static ConnectionFactory GetInstance()
        {
            if (Instance == null)
            {
                Assembly assembly = Assembly.GetExecutingAssembly();
                Type[] types = assembly.GetTypes();

                foreach (var type in types)
                {
                    if (type.IsSubclassOf(typeof(ConnectionFactory)))
                        Instance = (ConnectionFactory)Activator.CreateInstance(type);
                }
            }
            return Instance;
        }

        public abstract IDbConnection CreateConnection(IDictionary<string, string> props);
    }
}
