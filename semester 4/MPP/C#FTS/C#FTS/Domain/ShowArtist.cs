using System;
using System.Collections.Generic;
using System.Text;

namespace C_FTS.Domain
{
    public class ShowArtist : Entity<int>
    {
        public Show Show { get; private set; }
        public Artist Artist { get; private set; }

        public ShowArtist(Show show, Artist artist) : base(-1)
        {
            this.Show = show;
            this.Artist = artist;
        }

        public override string ToString()
        {
            return $"ShowArtist(Show = {Show}, Artist = {Artist})";
        }
    }
}
