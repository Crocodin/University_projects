using C_FTS.Domain;
using C_FTS.Utils;
using log4net;
using System;
using System.Collections.Generic;
using System.Data;

namespace C_FTS.Repository.DBRepository
{
    public class ShowArtistRepository : IShowArtistRepository
    {
        private static readonly ILog logger = LogManager.GetLogger(typeof(ShowArtistRepository));
        private readonly Database db = new Database();
        private readonly IArtistRepository artistRepository = new ArtistRepository();
        private readonly IShowRepository showRepository = new ShowRepository();

        public List<ShowArtist> FilterByDate(DateTime date)
        {
            logger.DebugFormat("FilterByDate {0}", date);
            string sql = "SELECT SA.* FROM show_artist SA JOIN show S ON SA.show_id = S.id WHERE DATE(S.date) = @date";
            List<ShowArtist> showArtists = new List<ShowArtist>();

            try
            {
                IDbConnection conn = db.GetConnection();
                using (IDbCommand cmd = conn.CreateCommand())
                {
                    cmd.CommandText = sql;

                    var dpDate = cmd.CreateParameter();
                    dpDate.ParameterName = "@date";
                    dpDate.Value = date.ToString("yyyy-MM-dd");
                    cmd.Parameters.Add(dpDate);

                    using (var rs = cmd.ExecuteReader())
                    {
                        return GetShowArtistsFromResultSet(showArtists, rs);
                    }
                }
            }
            catch (Exception e)
            {
                logger.ErrorFormat("Error while finding shows & artists by date: {0}", e.Message);
                throw new RepositoryException("Error while finding shows & artists by date: " + e.Message);
            }
        }

        public ShowArtist? Save(ShowArtist entity)
        {
            logger.DebugFormat("Save {0}", entity);
            string sql = "INSERT INTO show_artist (show_id, artist_id) VALUES (@show_id, @artist_id)";

            try
            {
                IDbConnection conn = db.GetConnection();
                using (IDbCommand cmd = conn.CreateCommand())
                {
                    cmd.CommandText = sql;

                    var dpShow = cmd.CreateParameter();
                    dpShow.ParameterName = "@show_id";
                    dpShow.Value = entity.Show.Id;

                    var dpArtist = cmd.CreateParameter();
                    dpArtist.ParameterName = "@artist_id";
                    dpArtist.Value = entity.Artist.Id;

                    cmd.Parameters.Add(dpShow);
                    cmd.Parameters.Add(dpArtist);

                    cmd.ExecuteNonQuery();
                }
            }
            catch (Exception e)
            {
                logger.ErrorFormat("Error while saving ShowArtist: {0}", e.Message);
                throw new RepositoryException("Error while saving ShowArtist: " + e.Message);
            }

            return entity;
        }

        public ShowArtist? Update(ShowArtist entity)
        {
            throw new NotImplementedException();
        }

        public void Delete(ShowArtist entity)
        {
            throw new NotImplementedException();
        }

        public ShowArtist? Find(int id)
        {
            throw new NotImplementedException();
        }

        public List<ShowArtist> FindAll()
        {
            logger.Info("FindAll");
            string sql = "SELECT * FROM show_artist";
            List<ShowArtist> showArtists = new List<ShowArtist>();

            try
            {
                IDbConnection conn = db.GetConnection();
                using (IDbCommand cmd = conn.CreateCommand())
                {
                    cmd.CommandText = sql;

                    using (var rs = cmd.ExecuteReader())
                    {
                        return GetShowArtistsFromResultSet(showArtists, rs);
                    }
                }
            }
            catch (Exception e)
            {
                logger.ErrorFormat("Error while finding all ShowArtists: {0}", e.Message);
                throw new RepositoryException("Error while finding all ShowArtists: " + e.Message);
            }
        }

        private List<ShowArtist> GetShowArtistsFromResultSet(List<ShowArtist> showArtists, IDataReader rs)
        {
            while (rs.Read())
            {
                int showId = rs.GetInt32(rs.GetOrdinal("show_id"));
                int artistId = rs.GetInt32(rs.GetOrdinal("artist_id"));

                var show = showRepository.Find(showId);
                var artist = artistRepository.Find(artistId);

                if (show == null || artist == null)
                {
                    logger.DebugFormat("Show or artist is null for show_id {0} and artist_id {1}", showId, artistId);
                }
                else
                {
                    showArtists.Add(new ShowArtist(show, artist));
                }
            }
            return showArtists;
        }
    }
}