using C_FTS.Domain;
using C_FTS.Utils;
using System;
using System.Collections.Generic;
using System.Text;
using log4net;
using System.Data;

namespace C_FTS.Repository.DBRepository
{
    public class ArtistRepository : IArtistRepository
    {
        private static readonly ILog logger = LogManager.GetLogger(typeof(ArtistRepository));
        private Database db = new Database();

        public void Delete(Artist entity)
        {
            throw new NotImplementedException();
        }

        public Artist? Find(int id)
        {
            logger.DebugFormat("Entering Find with id {0}", id);
            try
            {
                IDbConnection conn = db.GetConnection();

                using (IDbCommand cmd = conn.CreateCommand())
                {
                    cmd.CommandText = "SELECT * FROM artist WHERE id = @id";
                    IDbDataParameter dp = cmd.CreateParameter();
                    dp.ParameterName = "@id";
                    dp.Value = id;
                    cmd.Parameters.Add(dp);

                    using (var dataR = cmd.ExecuteReader())
                    {
                        if (dataR.Read())
                        {
                            Artist artist = new Artist(dataR);
                            logger.InfoFormat("Exiting Find with {0}", artist);
                            return artist;
                        }
                        logger.Error("Exiting Find with {0}", null);
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

        public List<Artist> FindAll()
        {
            throw new NotImplementedException();
        }

        public Artist? Save(Artist entity)
        {
            throw new NotImplementedException();
        }

        public Artist? Update(Artist entity)
        {
            throw new NotImplementedException();
        }
    }
}
