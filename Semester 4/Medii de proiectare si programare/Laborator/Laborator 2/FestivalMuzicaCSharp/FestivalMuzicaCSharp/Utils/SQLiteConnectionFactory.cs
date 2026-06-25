using System.Data;
using Microsoft.Data.Sqlite;

namespace FestivalMuzicaCSharp.Utils;

public class SQLiteConnectionFactory : ConnectionFactory
{
    public override IDbConnection createConnection(IDictionary<string, string> props)
    {
        var connectionString = props["ConnectionString"];
        return new SqliteConnection(connectionString);
    }
}