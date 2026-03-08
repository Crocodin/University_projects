using System;
using System.Collections.Generic;
using System.Data;
using System.Text;

namespace C_FTS.Domain
{
    public class Venue : Entity<int>
    {
        public string Name { get; }
        public string Address { get; }
        public int Capacity { get; }

        public Venue(int id, string name, string address, int capacity) : base(id)
        {
            Name = name;
            Address = address;
            Capacity = capacity;
        }

        public Venue(IDataReader reader) : base(reader.GetInt32(reader.GetOrdinal("id")))
        {
            Name = reader.GetString(reader.GetOrdinal("name"));
            Address = reader.GetString(reader.GetOrdinal("address"));
            Capacity = reader.GetInt32(reader.GetOrdinal("capacity"));
        }

        public override string ToString()
        {
            return $"Venue(Id={Id}, Name={Name}, Address={Address}, Capacity={Capacity})";
        }
    }
}
