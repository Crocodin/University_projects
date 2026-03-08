using System;
using System.Data;

namespace C_FTS.Domain
{
    public class Artist : Entity<int>
    {
        public string Name { get; set; }

        public Artist(int id,  string name) : base(id) { Name = name; }

        public Artist(IDataReader reader) : base(reader.GetInt32(reader.GetOrdinal("id")))
        {
            Name = reader.GetString(reader.GetOrdinal("name"));
        }

        public override string ToString()
        {
            return $"Artist(Id={Id}, Name={Name})";
        }
    }
}
