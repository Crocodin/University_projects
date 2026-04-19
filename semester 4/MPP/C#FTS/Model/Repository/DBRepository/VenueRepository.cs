using C_FTS.Domain;
using C_FTS.Utils;
using log4net;
using System;
using System.Collections.Generic;
using System.Data;
using System.Text;

namespace C_FTS.Repository.DBRepository
{
    internal class VenueRepository : IVenueRepository
    {
        private static readonly ILog logger = LogManager.GetLogger(typeof(VenueRepository));
        private Database db = new Database();
        public void Delete(Venue entity)
        {
            throw new NotImplementedException();
        }

        public Venue? Find(int id)
        {
            logger.DebugFormat("Entering Find with id {0}", id);
            IDbConnection conn = db.GetConnection();

            using (IDbCommand cmd = conn.CreateCommand())
            {
                cmd.CommandText = "SELECT * FROM venue WHERE id = @id";
                IDbDataParameter dp = cmd.CreateParameter();
                dp.ParameterName = "@id";
                dp.Value = id;
                cmd.Parameters.Add(dp);

                using (var dataR = cmd.ExecuteReader())
                {
                    if (dataR.Read())
                    {
                        Venue venue = new Venue(dataR);
                        logger.DebugFormat("Exiting Find with {0}", venue);
                        return venue;
                    }
                    logger.Error("Exiting Find with {0}", null);
                    return null;
                }
            }
        }

        public List<Venue> FindAll()
        {
            throw new NotImplementedException();
        }

        public Venue? Save(Venue entity)
        {
            throw new NotImplementedException();
        }

        public Venue? Update(Venue entity)
        {
            throw new NotImplementedException();
        }
    }
}
