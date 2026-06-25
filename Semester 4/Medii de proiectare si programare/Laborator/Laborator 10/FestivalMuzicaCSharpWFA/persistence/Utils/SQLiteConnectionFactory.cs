using System.Data;
using Microsoft.Data.Sqlite;

namespace persistence.Utils;

public class SQLiteConnectionFactory : ConnectionFactory
{
    public override IDbConnection createConnection(IDictionary<string, string> props)
    {
        var connectionString = props["ConnectionString"];
        var con = new SqliteConnection(connectionString);
        con.Open();

        using (var comm = con.CreateCommand())
        {
            comm.CommandText = $"PRAGMA foreign_keys = ON;";

            comm.ExecuteNonQuery();
        }

        return con;
    }
}