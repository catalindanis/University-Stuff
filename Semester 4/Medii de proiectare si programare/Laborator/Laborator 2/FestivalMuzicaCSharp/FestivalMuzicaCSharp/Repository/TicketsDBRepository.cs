using FestivalMuzicaCSharp.Domain;
using FestivalMuzicaCSharp.Utils;
using log4net;

namespace FestivalMuzicaCSharp.Repository;

public class TicketsDBRepository : ITicketsRepository
{
    private const string TableName = "tickets";
    private readonly ILog Log = LogManager.GetLogger(typeof(TicketsDBRepository));
    private IDictionary<string, string> props;

    public TicketsDBRepository(IDictionary<string, string> props)
    {
        this.props = props;
    }

    public int Size()
    {
        Log.InfoFormat("Entering Size");
        var con = DBUtils.getConnection(props);
        var result = 0;

        using (var comm = con.CreateCommand())
        {
            comm.CommandText = $"select count(*) from {TableName}";

            Log.InfoFormat("Executing: {0}", comm.CommandText);

            result = Convert.ToInt32(comm.ExecuteScalar());
        }

        Log.InfoFormat("Exiting Size with value {0}", result);
        return result;
    }

    public void Save(Ticket entity)
    {
        Log.InfoFormat("Entering Save with value {0}", entity);
        var con = DBUtils.getConnection(props);

        using (var comm = con.CreateCommand())
        {
            comm.CommandText = $"insert into {TableName}(client_name, show_id, no_seats) values(@clientName, @showId, @noSeats)";

            var paramClientName = comm.CreateParameter();
            paramClientName.ParameterName = "@clientName";
            paramClientName.Value = entity.ClientName;
            comm.Parameters.Add(paramClientName);

            var paramShowId = comm.CreateParameter();
            paramShowId.ParameterName = "@showId";
            paramShowId.Value = entity.Show.Id;
            comm.Parameters.Add(paramShowId);

            var paramNoSeats = comm.CreateParameter();
            paramNoSeats.ParameterName = "@noSeats";
            paramNoSeats.Value = entity.NoSeats;
            comm.Parameters.Add(paramNoSeats);

            Log.InfoFormat("Executing: {0}", comm.CommandText);

            comm.ExecuteNonQuery();
        }

        Log.InfoFormat("Exiting Save");
    }

    public void Delete(long id)
    {
        Log.InfoFormat("Entering Delete with value {0}", id);
        var con = DBUtils.getConnection(props);

        using (var comm = con.CreateCommand())
        {
            comm.CommandText = $"delete from {TableName} where id = @id";

            var paramId = comm.CreateParameter();
            paramId.ParameterName = "@id";
            paramId.Value = id;
            comm.Parameters.Add(paramId);

            Log.InfoFormat("Executing: {0}", comm.CommandText);

            comm.ExecuteNonQuery();
        }

        Log.InfoFormat("Exiting Delete");
    }

    public void Update(long id, Ticket entity)
    {
        Log.InfoFormat("Entering Update with value {0}", entity);
        var con = DBUtils.getConnection(props);

        using (var comm = con.CreateCommand())
        {
            comm.CommandText = $"update {TableName} set client_name = @clientName, show_id = @showId, no_seats = @noSeats where id = @id";

            var paramClientName = comm.CreateParameter();
            paramClientName.ParameterName = "@clientName";
            paramClientName.Value = entity.ClientName;
            comm.Parameters.Add(paramClientName);

            var paramShowId = comm.CreateParameter();
            paramShowId.ParameterName = "@showId";
            paramShowId.Value = entity.Show.Id;
            comm.Parameters.Add(paramShowId);

            var paramNoSeats = comm.CreateParameter();
            paramNoSeats.ParameterName = "@noSeats";
            paramNoSeats.Value = entity.NoSeats;
            comm.Parameters.Add(paramNoSeats);

            var paramId = comm.CreateParameter();
            paramId.ParameterName = "@id";
            paramId.Value = id;
            comm.Parameters.Add(paramId);

            Log.InfoFormat("Executing: {0}", comm.CommandText);

            comm.ExecuteNonQuery();
        }

        Log.InfoFormat("Exiting Update");
    }

    public Ticket? FindOne(long id)
    {
        Log.InfoFormat("Entering FindOne with value {0}", id);
        var con = DBUtils.getConnection(props);
        Ticket? result = null;

        using (var comm = con.CreateCommand())
        {
            comm.CommandText =
                $"select t.id, t.client_name, t.no_seats, s.id, s.artist_name, s.date, s.location, s.remaining_seats " +
                $"from {TableName} t inner join shows s on t.show_id = s.id where t.id = @id";

            var paramId = comm.CreateParameter();
            paramId.ParameterName = "@id";
            paramId.Value = id;
            comm.Parameters.Add(paramId);

            Log.InfoFormat("Executing: {0}", comm.CommandText);

            using (var dataR = comm.ExecuteReader())
            {
                if (dataR.Read())
                {
                    result = BuildTicketFromJoinRow(dataR);
                }
            }
        }

        Log.InfoFormat("Exiting FindOne with value {0}", result);
        return result;
    }

    public IEnumerable<Ticket> FindAll()
    {
        Log.InfoFormat("Entering FindAll");
        var con = DBUtils.getConnection(props);
        var result = new List<Ticket>();

        using (var comm = con.CreateCommand())
        {
            comm.CommandText =
                $"select t.id, t.client_name, t.no_seats, s.id, s.artist_name, s.date, s.location, s.remaining_seats " +
                $"from {TableName} t inner join shows s on t.show_id = s.id";

            Log.InfoFormat("Executing: {0}", comm.CommandText);

            using (var dataR = comm.ExecuteReader())
            {
                while (dataR.Read())
                {
                    result.Add(BuildTicketFromJoinRow(dataR));
                }
            }
        }

        Log.InfoFormat("Exiting FindAll with value {0}", result);
        return result;
    }

    private static Ticket BuildTicketFromJoinRow(System.Data.IDataReader dataR)
    {
        var ticketId = dataR.GetInt64(0);
        var clientName = dataR.GetString(1);
        var noSeats = dataR.GetInt32(2);

        var showId = dataR.GetInt64(3);
        var artistName = dataR.GetString(4);
        var date = DateOnly.FromDateTime(dataR.GetDateTime(5));
        var location = dataR.GetString(6);
        var remainingSeats = dataR.GetInt32(7);

        var show = new Show(showId, artistName, date, location, remainingSeats);
        return new Ticket(ticketId, clientName, show, noSeats);
    }
}