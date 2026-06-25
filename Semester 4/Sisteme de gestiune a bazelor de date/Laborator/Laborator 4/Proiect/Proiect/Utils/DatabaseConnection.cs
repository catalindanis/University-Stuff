using Npgsql;

namespace Proiect.Utils;

// Gestioneaza string-ul de conectare pentru PostgreSQL.
public class DatabaseConnection
{
    private readonly string _connectionString;
    
    // Input: string connectionString.
    // Return: nu returneaza nimic (constructor).
    // Salveaza connection string-ul primit la initializare.
    // Il foloseste ulterior pentru a crea conexiuni noi.
    public DatabaseConnection(string connectionString)
    {
        _connectionString = connectionString;
    }

    // Input: fara parametri.
    // Return: NpgsqlConnection.
    // Creeaza o conexiune noua catre baza de date.
    // Conexiunea este returnata inchisa (se deschide in repository).
    public NpgsqlConnection GetConnection()
    {
        return new NpgsqlConnection(_connectionString);
    }
}