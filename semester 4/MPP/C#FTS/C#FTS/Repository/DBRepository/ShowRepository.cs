using C_FTS.Domain;
using C_FTS.Utils;
using log4net;
using log4net.Repository.Hierarchy;
using System;
using System.Collections.Generic;
using System.Data;
using System.Text;

namespace C_FTS.Repository.DBRepository
{
    internal class ShowRepository : IShowRepository
    {
        private static readonly ILog logger = LogManager.GetLogger(typeof(ShowRepository));
        private Database db = new Database();
        private IArtistRepository artistRepository = new ArtistRepository();
        private IVenueRepository venueRepository = new VenueRepository();
        public void Delete(Show entity)
        {
            throw new NotImplementedException();
        }

        public Show? Find(int id)
        {
            logger.InfoFormat("Entering Find with id {0}", id);
            IDbConnection conn = db.GetConnection();

            using (IDbCommand cmd = conn.CreateCommand())
            {
                cmd.CommandText = "SELECT * FROM show WHERE id = @id";
                IDbDataParameter dp = cmd.CreateParameter();
                dp.ParameterName = "@id";
                dp.Value = id;
                cmd.Parameters.Add(dp);

                using (var dataR = cmd.ExecuteReader())
                {
                    if (dataR.Read())
                    {
                        Venue venue = venueRepository.Find(dataR.GetInt32(dataR.GetOrdinal("venue_id")));
                        List<Artist> performers = GetPerformers(conn, id);
                        Show show = new Show(dataR, performers, venue);
                        logger.InfoFormat("Exiting Find with {0}", show);
                        return show;
                    }
                    logger.Error("Exiting Find with {0}", null);
                    return null;
                }
            }
        }

        private List<Artist> GetPerformers(IDbConnection conn, int id)
        {
            logger.InfoFormat("Entering GetPerformers with show_id {0}", id);
            List<Artist> performers = new List<Artist>();

            using (IDbCommand cmd = conn.CreateCommand())
            {
                cmd.CommandText = "SELECT artist_id FROM show_artist WHERE show_id = @show_id";
                IDbDataParameter dp = cmd.CreateParameter();
                dp.ParameterName = "@show_id";
                dp.Value = id;
                cmd.Parameters.Add(dp);

                using (var dataR = cmd.ExecuteReader())
                {
                    while (dataR.Read())
                    {
                        Artist artist = artistRepository.Find(dataR.GetInt32(dataR.GetOrdinal("artist_id")));
                        if (artist != null)
                        {
                            performers.Add(artist);
                        }
                    }
                }
            }
            return performers;
        }

        public List<Show> FindAll()
        {
            logger.InfoFormat("Entering FinadAll");
            List<Show> shows = new List<Show>();

            IDbConnection conn = db.GetConnection();
            using (IDbCommand cmd = conn.CreateCommand())
            {
                cmd.CommandText = "SELECT * FROM show";
                using (var dataR = cmd.ExecuteReader())
                {
                    while(dataR.Read())
                    {
                        Venue venue = venueRepository.Find(dataR.GetInt32(dataR.GetOrdinal("venue_id")));
                        List<Artist> performers = GetPerformers(conn, dataR.GetInt32(dataR.GetOrdinal("venue_id")));
                        Show show = new Show(dataR, performers, venue);
                        shows.Add(show);
                    }
                }
            }
            return shows;
        }

        public List<Show> FindByPerformerAndDate(Artist artist, DateTime dateTime)
        {
            logger.InfoFormat("Entering FindByPAD with A {0} and D {0}", artist.Id, dateTime.ToString());
            IDbConnection conn = db.GetConnection();

            using (IDbCommand cmd = conn.CreateCommand())
            {
                cmd.CommandText = "SELECT S.* FROM show_artist SA JOIN show S ON SA.show_id = S.id WHERE SA.performer = @performer_id AND S.date = @date";
                List<Show> shows = new List<Show>();

                var dpPerformer = cmd.CreateParameter();
                dpPerformer.ParameterName = "@performer_id";
                dpPerformer.Value = artist.Id;
                cmd.Parameters.Add(dpPerformer);

                var dpDate = cmd.CreateParameter();
                dpDate.ParameterName = "@date";
                dpDate.Value = dateTime.ToString();
                cmd.Parameters.Add(dpDate);

                logger.Info("Executing query");
                using (var dataR = cmd.ExecuteReader())
                {
                    while (dataR.Read())
                    {
                        Venue venue = venueRepository.Find(dataR.GetInt32(dataR.GetOrdinal("venue_id")));
                        List<Artist> performers = GetPerformers(conn, dataR.GetInt32(dataR.GetOrdinal("venue_id")));
                        Show show = new Show(dataR, performers, venue);
                        shows.Add(show);
                    }
                }
                logger.InfoFormat("Exiting FindByPAD with len {0}", shows.Count);
                return shows;
            }
        }


        public Show? Save(Show entity)
        {
            throw new NotImplementedException();
        }

        public Show? Update(Show entity)
        {
            IDbConnection conn = db.GetConnection();
            using(IDbCommand cmd = conn.CreateCommand())
            {
                cmd.CommandText = "UPDATE show SET date = @date, title = @title, sold_seats = @sold_seats, venue_id = @venue_is WHERE id = @id";
                var dpDate = cmd.CreateParameter();
                dpDate.ParameterName = "@date";
                dpDate.Value = entity.Date;

                var dpTitle = cmd.CreateParameter();
                dpTitle.ParameterName = "@title";
                dpTitle.Value = entity.Title;

                var dpSoldSeats = cmd.CreateParameter();
                dpSoldSeats.ParameterName = "@sold_seats";
                dpSoldSeats.Value = entity.SoldSeats;

                var dpVenue = cmd.CreateParameter();
                dpVenue.ParameterName = "@venue_id";
                dpVenue.Value = entity.Venue.Id;

                var dpId = cmd.CreateParameter();
                dpId.ParameterName = "@Id";
                dpId.Value = entity.Id;

                cmd.Parameters.Add(dpDate);
                cmd.Parameters.Add(dpTitle);
                cmd.Parameters.Add(dpSoldSeats);
                cmd.Parameters.Add(dpVenue);
                cmd.Parameters.Add(dpId);

                var result = cmd.ExecuteNonQuery();
                if (result != 0)
                {
                    logger.InfoFormat("Updatesed Show {0}, {0}", entity.Id, entity);
                    return entity;
                }
                throw new RepositoryException("Error updateing tank");
            }
        }
    }
}
