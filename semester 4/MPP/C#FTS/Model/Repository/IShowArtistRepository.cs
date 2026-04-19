using C_FTS.Domain;
using System;
using System.Collections.Generic;
using System.Text;

namespace C_FTS.Repository
{
    public interface IShowArtistRepository : IRepository<int, ShowArtist>
    {
        public List<ShowArtist> FilterByDate(DateTime date);
    }
}
