using System;
using System.Collections.Generic;
using System.Text;

namespace C_FTS.Domain
{
    public class Entity<TId>(TId id)
    {
        public TId Id { get; set; } = id;
    }
}
