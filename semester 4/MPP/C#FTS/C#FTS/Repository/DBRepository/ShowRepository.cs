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
            try
            {
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
                            Show show = new Show(dataR, venue);
                            logger.InfoFormat("Exiting Find with {0}", show);
                            return show;
                        }

                        logger.WarnFormat("Find: no Show found with id {0}", id);
                        return null;
                    }
                }
            }
            catch (Exception ex)
            {
                logger.ErrorFormat("Find: exception while finding Show with id {0}: {1}", id, ex.Message);
                throw;
            }
        }

        public List<Show> FindAll()
        {
            logger.Info("Entering FindAll");
            List<Show> shows = new List<Show>();

            try
            {
                IDbConnection conn = db.GetConnection();

                using (IDbCommand cmd = conn.CreateCommand())
                {
                    cmd.CommandText = "SELECT * FROM show";

                    using (var dataR = cmd.ExecuteReader())
                    {
                        while (dataR.Read())
                        {
                            Venue venue = venueRepository.Find(dataR.GetInt32(dataR.GetOrdinal("venue_id")));
                            Show show = new Show(dataR, venue);
                            shows.Add(show);
                        }
                    }
                }

                logger.InfoFormat("FindAll: returning {0} show(s)", shows.Count);
                return shows;
            }
            catch (Exception ex)
            {
                logger.ErrorFormat("FindAll: exception while retrieving all shows: {0}", ex.Message);
                throw;
            }
        }

        public Show? Save(Show entity)
        {
            throw new NotImplementedException();
        }

        public Show? Update(Show entity)
        {
            logger.InfoFormat("Entering Update for Show id {0}", entity.Id);
            try
            {
                IDbConnection conn = db.GetConnection();

                using (IDbCommand cmd = conn.CreateCommand())
                {
                    cmd.CommandText = "UPDATE show SET date = @date, title = @title, sold_seats = @sold_seats, venue_id = @venue_id WHERE id = @id";

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
                    dpId.ParameterName = "@id";
                    dpId.Value = entity.Id;

                    cmd.Parameters.Add(dpDate);
                    cmd.Parameters.Add(dpTitle);
                    cmd.Parameters.Add(dpSoldSeats);
                    cmd.Parameters.Add(dpVenue);
                    cmd.Parameters.Add(dpId);

                    var result = cmd.ExecuteNonQuery();

                    if (result != 0)
                    {
                        logger.InfoFormat("Update: successfully updated Show with id {0}", entity.Id);
                        return entity;
                    }

                    logger.WarnFormat("Update: no rows affected for Show with id {0}", entity.Id);
                    throw new RepositoryException("Error updating show");
                }
            }
            catch (RepositoryException)
            {
                throw;
            }
            catch (Exception ex)
            {
                logger.ErrorFormat("Update: exception while updating Show with id {0}: {1}", entity.Id, ex.Message);
                throw;
            }
        }
    }
}