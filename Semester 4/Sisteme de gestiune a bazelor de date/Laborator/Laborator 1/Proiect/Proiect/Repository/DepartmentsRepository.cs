using Npgsql;
using Proiect.Config;
using Proiect.Domain;
using Proiect.Utils;

namespace Proiect.Repository;

// Realizeaza operatii SQL pentru tabela departments.
public class DepartmentsRepository : IRepository<long, Department>
{
    private readonly DatabaseConnection _databaseConnection;
    
    // Input: fara parametri.
    // Return: nu returneaza nimic (constructor).
    // Creeaza intern conexiunea pe baza config-ului aplicatiei.
    // Este constructorul folosit in productie.
    public DepartmentsRepository() => _databaseConnection = new DatabaseConnection(
        AppConfig.Instance.GetConnectionString()
    );
    
    // Input: DatabaseConnection databaseConnection.
    // Return: nu returneaza nimic (constructor).
    // Primeste conexiunea din exterior.
    // Este util pentru testare sau injectare manuala.
    public DepartmentsRepository(DatabaseConnection databaseConnection) => _databaseConnection = databaseConnection;

    // Input: fara parametri.
    // Return: int.
    // Calculeaza numarul total de randuri din departments.
    // Returneaza 0 daca rezultatul SQL este null.
    public int Size()
    {
        using var connection = _databaseConnection.GetConnection();
        connection.Open();

        using var command = new NpgsqlCommand(
            "SELECT COUNT(*) FROM departments", 
            connection
        );

        var result = command.ExecuteScalar();
        return result != null ? Convert.ToInt32(result) : 0;
    }

    // Input: Department entity.
    // Return: void.
    // Insereaza un departament nou in baza de date.
    // Foloseste parametri pentru a evita SQL injection.
    public void Save(Department entity)
    {
        using var connection = _databaseConnection.GetConnection();
        connection.Open();

        using var command = new NpgsqlCommand(
            "INSERT INTO departments (name, location) VALUES (@name, @location)", 
            connection
        );

        command.Parameters.AddWithValue("@name", entity.Name);
        command.Parameters.AddWithValue("@location", entity.Location);

        command.ExecuteNonQuery();
    }

    // Input: long id.
    // Return: void.
    // Sterge departamentul cu id-ul primit.
    // Executa un DELETE direct in baza de date.
    public void Delete(long id)
    {
        using var connection = _databaseConnection.GetConnection();
        connection.Open();

        using var command = new NpgsqlCommand(
            "DELETE FROM departments WHERE id = @id", 
            connection
        );

        command.Parameters.AddWithValue("@id", id);
        command.ExecuteNonQuery();
    }

    // Input: long id, Department entity.
    // Return: void.
    // Actualizeaza nume si locatie pentru departamentul selectat.
    // Identificarea randului se face dupa id.
    public void Update(long id, Department entity)
    {
        using var connection = _databaseConnection.GetConnection();
        connection.Open();

        using var command = new NpgsqlCommand(
            "UPDATE departments SET name = @name, location = @location WHERE id = @id", 
            connection
        );

        command.Parameters.AddWithValue("@id", id);
        command.Parameters.AddWithValue("@name", entity.Name);
        command.Parameters.AddWithValue("@location", entity.Location);

        command.ExecuteNonQuery();
    }

    // Input: long id.
    // Return: Department.
    // Citeste un singur departament dupa id.
    // Arunca exceptie daca nu este gasit.
    public Department FindById(long id)
    {
        using var connection = _databaseConnection.GetConnection();
        connection.Open();

        using var command = new NpgsqlCommand(
            "SELECT id, name, location FROM departments WHERE id = @id", 
            connection
        );

        command.Parameters.AddWithValue("@id", id);
    
        using var reader = command.ExecuteReader();
        if (reader.Read())
        {
            return new Department
            (
                reader.GetInt64(0),
                reader.GetString(1),
                reader.GetString(2)
            );
        }
        
        throw new Exception("Department not found");
    }

    // Input: fara parametri.
    // Return: IEnumerable<Department>.
    // Citeste toate departamentele din tabela.
    // Construieste lista de obiecte Department.
    public IEnumerable<Department> FindAll()
    {
        var departments = new List<Department>();
        
        using var connection = _databaseConnection.GetConnection();
        connection.Open();

        using var command = new NpgsqlCommand(
            "SELECT id, name, location FROM departments", 
            connection
        );

        using var reader = command.ExecuteReader();
        while (reader.Read())
        {
            departments.Add(new Department
            (
                reader.GetInt64(0),
                reader.GetString(1),
                reader.GetString(2)
            ));
        }

        return departments;
    }
}