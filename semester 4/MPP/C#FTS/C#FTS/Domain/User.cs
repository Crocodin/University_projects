using System;
using System.Collections.Generic;
using System.Data;
using System.Text;

namespace C_FTS.Domain
{
    public class User : Entity<int>
    {
        public string Username { get; set; }
        public string Password { get; set; }

        public User(IDataReader reader) : base(reader.GetInt32(reader.GetOrdinal("id")))
        {
            this.Username = reader.GetString(reader.GetOrdinal("username"));
            this.Password = reader.GetString(reader.GetOrdinal("password"));
        }

        public User(int id, string username, string password) : base(id)
        {
            this.Username = username;
            this.Password = password;
        }

        public override string ToString()
        {
            return $"User(username = {Username}, password = {Password})";
        }
    }
}
