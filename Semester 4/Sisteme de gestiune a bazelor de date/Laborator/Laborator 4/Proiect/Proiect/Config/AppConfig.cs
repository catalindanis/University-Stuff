using System.Configuration;

namespace Proiect.Config;

// Citeste setarile aplicatiei din appsettings.json.
public sealed class AppConfig
{
    // Instanta unica folosita in toata aplicatia.
    public static readonly AppConfig Instance = new AppConfig();
    private readonly Dictionary<string, string> _props;

    // Input: fara parametri.
    // Return: nu returneaza nimic (constructor).
    // Incarca configurarea din fisierul appsettings.json.
    // Pregateste obiectul intern pentru citirea setarilor.
    private AppConfig()
    {
        _props = new Dictionary<string, string>();
        var connStr = ConfigurationManager.ConnectionStrings["dbURL"]?.ConnectionString;
        if (connStr == null)
            throw new Exception("Cannot find connection string in app.config");
        _props["ConnectionString"] = connStr;
    }

    // Input: fara parametri.
    // Return: string (connection string pentru PostgreSQL).
    // Citeste conexiunea din sectiunea ConnectionStrings.
    // Este folosita de repository-uri pentru deschiderea conexiunii.
    public string GetConnectionString()
    {
        return _props["ConnectionString"];
    }
}