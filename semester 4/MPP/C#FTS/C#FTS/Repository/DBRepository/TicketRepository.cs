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
    public class TicketRepository : ITicketRepository
    {
        private static readonly ILog logger = LogManager.GetLogger(typeof(ArtistRepository));
        private Database db = new Database();
        private ShowRepository showRepository = new ShowRepository();

        public void Delete(Ticket entity)
        {
            throw new NotImplementedException();
        }

        public Ticket? Find(int id)
        {
            logger.InfoFormat("Entering Find ticket with id {0}", id);

            IDbConnection conn = db.GetConnection();

            using (IDbCommand cmd = conn.CreateCommand())
            {
                cmd.CommandText = "SELECT * FROM ticket WHERE id = @id";

                IDbDataParameter dp = cmd.CreateParameter();
                dp.ParameterName = "@id";
                dp.Value = id;
                cmd.Parameters.Add(dp);

                using (var dataR = cmd.ExecuteReader())
                {
                    if (dataR.Read())
                    {
                        Show show = showRepository.Find(
                            dataR.GetInt32(dataR.GetOrdinal("show_id"))
                        ) ?? throw new RepositoryException("Show not found");

                        Ticket ticket = new(dataR, show);

                        logger.InfoFormat("Exiting Find with {0}", ticket);
                        return ticket;
                    }
                }
            }

            logger.Error("Exiting Find with null");
            return null;
        }

        public List<Ticket> FindAll()
        {
            throw new NotImplementedException();
        }

        public Ticket? IncrementSeats(Ticket ticket, int seats)
        {
            Show show = ticket.Show;
            if (show == null) { return null; }
            if (show.RemainingSeats() > seats)
            {
                ticket.NumberOfSeats = ticket.NumberOfSeats + seats;
                return ticket;
            }
            throw new RepositoryException("Not enough seats to modify ticket" + ticket.Id);
        }

        public Ticket? Save(Ticket entity)
        {
            logger.InfoFormat("Entering Save with entity {0}", entity);

            IDbConnection conn = db.GetConnection();

            using (IDbCommand cmd = conn.CreateCommand())
            {
                cmd.CommandText = "INSERT INTO ticket (buyer_name, number_of_seats, purchase_date, show_id) " +
                                  "VALUES (@buyer_name, @number_of_seats, @purchase_date, @show_id); " +
                                  "SELECT last_insert_rowid();";

                IDbDataParameter p1 = cmd.CreateParameter();
                p1.ParameterName = "@buyer_name";
                p1.Value = entity.BuyerName;
                cmd.Parameters.Add(p1);

                IDbDataParameter p2 = cmd.CreateParameter();
                p2.ParameterName = "@number_of_seats";
                p2.Value = entity.NumberOfSeats;
                cmd.Parameters.Add(p2);

                IDbDataParameter p3 = cmd.CreateParameter();
                p3.ParameterName = "@purchase_date";
                p3.Value = entity.PurchaseDate;
                cmd.Parameters.Add(p3);

                IDbDataParameter p4 = cmd.CreateParameter();
                p4.ParameterName = "@show_id";
                p4.Value = entity.Show.Id;
                cmd.Parameters.Add(p4);

                try
                {
                    object result = cmd.ExecuteScalar();
                    if (result != null)
                    {
                        entity.Id = Convert.ToInt32(result);
                    }

                    logger.InfoFormat("Exiting Save with {0}", entity);
                    return entity;
                }
                catch (Exception e)
                {
                    logger.Error("Error while saving ticket", e);
                    throw new Exception("Error while saving ticket: " + e.Message);
                }
            }
        }

        public Ticket? Update(Ticket entity)
        {
            logger.InfoFormat("Entering Update with entity {0}", entity);

            IDbConnection conn = db.GetConnection();

            using (IDbCommand cmd = conn.CreateCommand())
            {
                cmd.CommandText = "UPDATE ticket SET buyer_name = @buyer_name, " +
                                  "number_of_seats = @number_of_seats, " +
                                  "purchase_date = @purchase_date, " +
                                  "show_id = @show_id WHERE id = @id";

                IDbDataParameter p1 = cmd.CreateParameter();
                p1.ParameterName = "@buyer_name";
                p1.Value = entity.BuyerName;
                cmd.Parameters.Add(p1);

                IDbDataParameter p2 = cmd.CreateParameter();
                p2.ParameterName = "@number_of_seats";
                p2.Value = entity.NumberOfSeats;
                cmd.Parameters.Add(p2);

                IDbDataParameter p3 = cmd.CreateParameter();
                p3.ParameterName = "@purchase_date";
                p3.Value = entity.PurchaseDate;
                cmd.Parameters.Add(p3);

                IDbDataParameter p4 = cmd.CreateParameter();
                p4.ParameterName = "@show_id";
                p4.Value = entity.Show.Id;
                cmd.Parameters.Add(p4);

                IDbDataParameter p5 = cmd.CreateParameter();
                p5.ParameterName = "@id";
                p5.Value = entity.Id;
                cmd.Parameters.Add(p5);

                try
                {
                    int result = cmd.ExecuteNonQuery();

                    if (result > 0)
                    {
                        logger.InfoFormat("Exiting Update with {0}", entity);
                        return entity;
                    }

                    logger.Error("Exiting Update with null");
                    return null;
                }
                catch (Exception e)
                {
                    logger.Error("Error while updating ticket", e);
                    throw new Exception("Error while updating ticket: " + e.Message);
                }
            }
        }
    }
}
