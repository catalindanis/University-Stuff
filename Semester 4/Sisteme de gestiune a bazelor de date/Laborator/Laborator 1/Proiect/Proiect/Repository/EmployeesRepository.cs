using Npgsql;
using Proiect.Config;
using Proiect.Domain;
using Proiect.Utils;

namespace Proiect.Repository;

// Realizeaza operatii SQL pentru tabela employees.
public class EmployeesRepository : IFilterableRepository<long, Employee, EmployeeFilter>
{
    private readonly DatabaseConnection _databaseConnection;
    
    // Input: fara parametri.
    // Return: nu returneaza nimic (constructor).
    // Creeaza intern conexiunea pe baza config-ului aplicatiei.
    // Este constructorul folosit in productie.
    public EmployeesRepository() => _databaseConnection = new DatabaseConnection(
        AppConfig.Instance.GetConnectionString()
    );
    
    // Input: DatabaseConnection databaseConnection.
    // Return: nu returneaza nimic (constructor).
    // Primeste conexiunea din exterior.
    // Este util pentru testare sau injectare manuala.
    public EmployeesRepository(DatabaseConnection databaseConnection) => _databaseConnection = databaseConnection;

    // Input: fara parametri.
    // Return: int.
    // Calculeaza numarul total de angajati.
    // Returneaza 0 daca rezultatul SQL este null.
    public int Size()
    {
        using var connection = _databaseConnection.GetConnection();
        connection.Open();

        using var command = new NpgsqlCommand(
            "SELECT COUNT(*) FROM employees", 
            connection
        );

        var result = command.ExecuteScalar();
        return result != null ? Convert.ToInt32(result) : 0;
    }

    // Input: Employee entity.
    // Return: void.
    // Insereaza un angajat nou in baza de date.
    // Foloseste parametri pentru a evita SQL injection.
    public void Save(Employee entity)
    {
        using var connection = _databaseConnection.GetConnection();
        connection.Open();

        using var command = new NpgsqlCommand(
            "INSERT INTO employees (first_name, last_name, email, department_id) VALUES " +
            "(@firstName, @lastName, @email, @departmentId)", 
            connection
        );

        command.Parameters.AddWithValue("@firstName", entity.FirstName);
        command.Parameters.AddWithValue("@lastName", entity.LastName);
        command.Parameters.AddWithValue("@email", entity.Email);
        command.Parameters.AddWithValue("@departmentId", entity.DepartmentId);

        command.ExecuteNonQuery();
    }

    // Input: long id.
    // Return: void.
    // Sterge angajatul cu id-ul primit.
    // Executa un DELETE direct in baza de date.
    public void Delete(long id)
    {
        using var connection = _databaseConnection.GetConnection();
        connection.Open();

        using var command = new NpgsqlCommand(
            "DELETE FROM employees WHERE id = @id", 
            connection
        );

        command.Parameters.AddWithValue("@id", id);
        command.ExecuteNonQuery();
    }

    // Input: long id, Employee entity.
    // Return: void.
    // Actualizeaza datele unui angajat existent.
    // Identificarea randului se face dupa id.
    public void Update(long id, Employee entity)
    {
        using var connection = _databaseConnection.GetConnection();
        connection.Open();

        using var command = new NpgsqlCommand(
            "UPDATE employees SET first_name = @firstName, last_name = @lastName, email = @email, department_id = @departmentId WHERE id = @id", 
            connection
        );

        command.Parameters.AddWithValue("@id", id);
        command.Parameters.AddWithValue("@firstName", entity.FirstName);
        command.Parameters.AddWithValue("@lastName", entity.LastName);
        command.Parameters.AddWithValue("@email", entity.Email);
        command.Parameters.AddWithValue("@departmentId", entity.DepartmentId);

        command.ExecuteNonQuery();
    }

    // Input: long id.
    // Return: Employee.
    // Citeste un singur angajat dupa id.
    // Arunca exceptie daca nu este gasit.
    public Employee FindById(long id)
    {
        using var connection = _databaseConnection.GetConnection();
        connection.Open();

        using var command = new NpgsqlCommand(
            "SELECT id, first_name, last_name, email, department_id FROM employees WHERE id = @id", 
            connection
        );

        command.Parameters.AddWithValue("@id", id);
    
        using var reader = command.ExecuteReader();
        if (reader.Read())
        {
            return new Employee
            (
                reader.GetInt64(0),
                reader.GetString(1),
                reader.GetString(2),
                reader.GetString(3),
                reader.GetInt64(4)
            );
        }
        
        throw new Exception("Employee not found");
    }

    // Input: fara parametri.
    // Return: IEnumerable<Employee>.
    // Citeste toti angajatii din tabela.
    // Construieste lista de obiecte Employee.
    public IEnumerable<Employee> FindAll()
    {
        var employees = new List<Employee>();
        
        using var connection = _databaseConnection.GetConnection();
        connection.Open();

        using var command = new NpgsqlCommand(
            "SELECT id, first_name, last_name, email, department_id FROM employees", 
            connection
        );

        using var reader = command.ExecuteReader();
        while (reader.Read())
        {
            employees.Add(new Employee
            (
                reader.GetInt64(0),
                reader.GetString(1),
                reader.GetString(2),
                reader.GetString(3),
                reader.GetInt64(4)
            ));
        }

        return employees;
    }
    
    // Input: EmployeeFilter filter.
    // Return: IEnumerable<Employee>.
    // Construieste query-ul SQL in functie de filtrele completate.
    // Returneaza doar angajatii care respecta criteriile.
    public IEnumerable<Employee> FindAll(EmployeeFilter filter)
    {
         var employees = new List<Employee>();
        
         using var connection = _databaseConnection.GetConnection();
         connection.Open();
         
         var sql = new System.Text.StringBuilder(
             "SELECT id, first_name, last_name, email, department_id FROM employees WHERE 1=1"
         );
         using var command = new NpgsqlCommand();
         command.Connection = connection;
        
         if (!string.IsNullOrWhiteSpace(filter?.FirstName))
         {
             sql.Append(" AND first_name ILIKE @firstName");
             command.Parameters.AddWithValue("@firstName", $"%{filter.FirstName.Trim()}%");
         }
        
         if (!string.IsNullOrWhiteSpace(filter?.LastName))
         {
             sql.Append(" AND last_name ILIKE @lastName");
             command.Parameters.AddWithValue("@lastName", $"%{filter.LastName.Trim()}%");
         }
        
         if (!string.IsNullOrWhiteSpace(filter?.Email))
         {
             sql.Append(" AND email ILIKE @email");
             command.Parameters.AddWithValue("@email", $"%{filter.Email.Trim()}%");
         }
        
         if (filter?.DepartmentId.HasValue ?? false)
         {
             sql.Append(" AND department_id = @departmentId");
             command.Parameters.AddWithValue("@departmentId", filter.DepartmentId.Value);
         }
        
         command.CommandText = sql.ToString();
        
         using var reader = command.ExecuteReader();
         while (reader.Read())
         {
             employees.Add(new Employee(
             reader.GetInt64(0),
             reader.GetString(1),
             reader.GetString(2),
             reader.GetString(3),
             reader.GetInt64(4)
             ));
         }
        
         return employees;
    }
    
}