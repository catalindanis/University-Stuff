using System.Configuration;
using Npgsql;

namespace Proiect.Utils;

public class ConnectionLeakTests { 
    public void RunLeakVsFixDemo()
    {
        string baseConnectionString = ConfigurationManager.ConnectionStrings["dbURL"].ConnectionString;

        var builder = new NpgsqlConnectionStringBuilder(baseConnectionString)
        {
            Pooling = true,
            MinPoolSize =0,
            MaxPoolSize =5,
            Timeout =2 
        };

        string connectionString = builder.ConnectionString;

        NpgsqlConnection.ClearAllPools();
        Console.WriteLine("Leak scenario: open connections without closing them.");
        DemonstratePoolExhaustion(connectionString);

        NpgsqlConnection.ClearAllPools();
        Console.WriteLine("Fixed scenario: use proper resource management.");
        DemonstrateProperResourceManagement(connectionString);
    }

    private void DemonstratePoolExhaustion(string connectionString)
    {
        var leakedConnections = new List<NpgsqlConnection>();

        try {
            for (int i =0; i <5; i++)
            {
                var connection = new NpgsqlConnection(connectionString);
                connection.Open();
                leakedConnections.Add(connection);
                Console.WriteLine($"Opened leaked connection {i +1}/5");
            }

            Console.WriteLine("Trying to open one more connection (should fail due to pool exhaustion)...");
            using var extra = new NpgsqlConnection(connectionString);
            extra.Open();

            Console.WriteLine("Unexpected: extra connection opened.");
        }
        catch (Exception ex)
        {
            Console.WriteLine($"Pool exhaustion observed: {ex.GetType().Name} - {ex.Message}");
        }
        finally 
        {
            foreach (var c in leakedConnections)
            {
                c.Dispose();
            }
        }
    }

    private void DemonstrateProperResourceManagement(string connectionString)
    {
        try {
            for (int i = 0; i < 50; i++)
            {
                using var connection = new NpgsqlConnection(connectionString);
                connection.Open();
            }

            Console.WriteLine("Success: no pool exhaustion when connections are disposed correctly.");
        }
        catch (Exception ex)
        {
            Console.WriteLine($"Unexpected error in fixed scenario: {ex.GetType().Name} - {ex.Message}");
        }
    }
}
