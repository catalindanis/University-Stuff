using FestivalMuzicaCSharp.Domain;
using FestivalMuzicaCSharp.Utils;
using log4net;

namespace FestivalMuzicaCSharp.Repository;

public class ShowsDbRepository : IShowsRepository
{
    private const string TableName = "shows";
    private readonly ILog Log = LogManager.GetLogger(typeof(ShowsDbRepository));
    private IDictionary<string, string> props;

    public ShowsDbRepository(IDictionary<string, string> props)
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

    public void Save(Show entity)
    {
        Log.InfoFormat("Entering Save with value {0}", entity);
        var con = DBUtils.getConnection(props);

        using (var comm = con.CreateCommand())
        {
            comm.CommandText = $"insert into {TableName}(artist_name, date, location, remaining_seats) values(@artistName, @date, @location, @remainingSeats)";

            var paramArtistName = comm.CreateParameter();
            paramArtistName.ParameterName = "@artistName";
            paramArtistName.Value = entity.ArtistName;
            comm.Parameters.Add(paramArtistName);

            var paramDate = comm.CreateParameter();
            paramDate.ParameterName = "@date";
            paramDate.Value = entity.Date.ToDateTime(TimeOnly.MinValue);
            comm.Parameters.Add(paramDate);

            var paramLocation = comm.CreateParameter();
            paramLocation.ParameterName = "@location";
            paramLocation.Value = entity.Location;
            comm.Parameters.Add(paramLocation);

            var paramRemainingSeats = comm.CreateParameter();
            paramRemainingSeats.ParameterName = "@remainingSeats";
            paramRemainingSeats.Value = entity.RemainingSeats;
            comm.Parameters.Add(paramRemainingSeats);

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

    public void Update(long id, Show entity)
    {
        Log.InfoFormat("Entering Update with value {0}", entity);
        var con = DBUtils.getConnection(props);

        using (var comm = con.CreateCommand())
        {
            comm.CommandText = $"update {TableName} set artist_name = @artistName, date = @date, location = @location, remaining_seats = @remainingSeats where id = @id";

            var paramArtistName = comm.CreateParameter();
            paramArtistName.ParameterName = "@artistName";
            paramArtistName.Value = entity.ArtistName;
            comm.Parameters.Add(paramArtistName);

            var paramDate = comm.CreateParameter();
            paramDate.ParameterName = "@date";
            paramDate.Value = entity.Date.ToDateTime(TimeOnly.MinValue);
            comm.Parameters.Add(paramDate);

            var paramLocation = comm.CreateParameter();
            paramLocation.ParameterName = "@location";
            paramLocation.Value = entity.Location;
            comm.Parameters.Add(paramLocation);

            var paramRemainingSeats = comm.CreateParameter();
            paramRemainingSeats.ParameterName = "@remainingSeats";
            paramRemainingSeats.Value = entity.RemainingSeats;
            comm.Parameters.Add(paramRemainingSeats);

            var paramId = comm.CreateParameter();
            paramId.ParameterName = "@id";
            paramId.Value = id;
            comm.Parameters.Add(paramId);

            Log.InfoFormat("Executing: {0}", comm.CommandText);

            comm.ExecuteNonQuery();
        }

        Log.InfoFormat("Exiting Update");
    }

    public Show? FindOne(long id)
    {
        Log.InfoFormat("Entering FindOne with value {0}", id);
        var con = DBUtils.getConnection(props);
        Show? result = null;

        using (var comm = con.CreateCommand())
        {
            comm.CommandText = $"select * from {TableName} where id = @id";

            var paramId = comm.CreateParameter();
            paramId.ParameterName = "@id";
            paramId.Value = id;
            comm.Parameters.Add(paramId);

            Log.InfoFormat("Executing: {0}", comm.CommandText);

            using (var dataR = comm.ExecuteReader())
            {
                if (dataR.Read())
                {
                    var showId = dataR.GetInt32(0);
                    var artistName = dataR.GetString(1);
                    var date = DateOnly.FromDateTime(dataR.GetDateTime(2));
                    var location = dataR.GetString(3);
                    var remainingSeats = dataR.GetInt32(4);

                    result = new Show(showId, artistName, date, location, remainingSeats);
                }
            }
        }

        Log.InfoFormat("Exiting FindOne with value {0}", result);
        return result;
    }

    public IEnumerable<Show> FindAll()
    {
        Log.InfoFormat("Entering FindAll");
        var con = DBUtils.getConnection(props);
        var result = new List<Show>();

        using (var comm = con.CreateCommand())
        {
            comm.CommandText = $"select * from {TableName}";

            Log.InfoFormat("Executing: {0}", comm.CommandText);

            using (var dataR = comm.ExecuteReader())
            {
                while (dataR.Read())
                {
                    var showId = dataR.GetInt32(0);
                    var artistName = dataR.GetString(1);
                    var date = DateOnly.FromDateTime(dataR.GetDateTime(2));
                    var location = dataR.GetString(3);
                    var remainingSeats = dataR.GetInt32(4);

                    result.Add(new Show(showId, artistName, date, location, remainingSeats));
                }
            }
        }

        Log.InfoFormat("Exiting FindAll with value {0}", result);
        return result;
    }
}