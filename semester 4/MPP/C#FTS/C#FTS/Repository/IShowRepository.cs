using C_FTS.Domain;
using System;
using System.Collections.Generic;
using System.Text;

namespace C_FTS.Repository
{
    internal interface IShowRepository : IRepository<int, Show>
    {
        List<Show> FindByPerformerAndDate(Artist artist, DateTime dateTime);
    }
}
