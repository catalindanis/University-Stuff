using Microsoft.Extensions.Configuration;

namespace Proiect.Config;

// Citeste setarile aplicatiei din appsettings.json.
public sealed class AppConfig
{
    // Instanta unica folosita in toata aplicatia.
    public static readonly AppConfig Instance = new AppConfig();
    private readonly IConfiguration _configuration;

    // Input: fara parametri.
    // Return: nu returneaza nimic (constructor).
    // Incarca configurarea din fisierul appsettings.json.
    // Pregateste obiectul intern pentru citirea setarilor.
    private AppConfig()
    {
        _configuration = new ConfigurationBuilder()
            .SetBasePath(Directory.GetCurrentDirectory())
            .AddJsonFile("appsettings.json", optional: false, reloadOnChange: true)
            .Build();
    }

    // Input: fara parametri.
    // Return: string (connection string pentru PostgreSQL).
    // Citeste conexiunea din sectiunea ConnectionStrings.
    // Este folosita de repository-uri pentru deschiderea conexiunii.
    public string GetConnectionString()
    {
        return _configuration.GetConnectionString("PostgreSqlConnection")!;
    }
}