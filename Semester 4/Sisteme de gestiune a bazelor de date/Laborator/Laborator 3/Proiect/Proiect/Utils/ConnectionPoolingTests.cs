using System.Configuration;
using System.Diagnostics;
using Npgsql;

namespace Proiect.Utils;

public class ConnectionPoolingTests
{
    private const string BaseConnectionString = "Server=localhost;Database=TestDB;Integrated Security=True;TrustServerCertificate=True;";
    public void RunPoolingTests()
    {
        string baseConnectionString = ConfigurationManager.ConnectionStrings["dbURL"].ConnectionString;

        NpgsqlConnectionStringBuilder builder = new NpgsqlConnectionStringBuilder(baseConnectionString);

        builder.Pooling = false;
        string noPoolingConnString = builder.ConnectionString;

        builder.Pooling = true;
        string poolingConnString = builder.ConnectionString;

        Console.WriteLine("DB URL (no pooling): " + noPoolingConnString);
        MeasureConnections(noPoolingConnString, 100);

        Console.WriteLine("DB URL (pooling): " + poolingConnString);
        MeasureConnections(poolingConnString, 100);
    }

    private void MeasureConnections(string connectionString, int iterations)
    {
        Stopwatch stopwatch = new Stopwatch();

        stopwatch.Start();

        for (int i = 0; i < iterations; i++)
        {
            using (NpgsqlConnection connection = new NpgsqlConnection(connectionString))
            {
                connection.Open();
            }
        }

        stopwatch.Stop();

        long totalTimeMs = stopwatch.ElapsedMilliseconds;
        double averageTimeMs = (double)totalTimeMs / iterations;

        Console.WriteLine($"Timp total pentru {iterations} conexiuni: {totalTimeMs} ms");
        Console.WriteLine($"Timp mediu per conexiune: {averageTimeMs:F4} ms");
    }
}
