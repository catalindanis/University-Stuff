using Proiect.Domain;
using Proiect.Repository;

namespace Proiect.Service;

// Ofera operatii de business pentru angajati.
public class EmployeesService
{
    public static readonly EmployeesService Instance = new  EmployeesService();
    private readonly IFilterableRepository<long, Employee, EmployeeFilter> _repository;
    private readonly EmployeesRepository _employeesRepository;
    
    // Input: fara parametri.
    // Return: nu returneaza nimic (constructor).
    // Initializeaza repository-ul de angajati.
    // Este folosit la crearea instantei singleton.
    private EmployeesService()
    {
        _employeesRepository = new EmployeesRepository();
        _repository = _employeesRepository;
    }
    
    // Input: string firstName, string lastName, string email, long departmentId.
    // Return: void.
    // Creeaza si salveaza un angajat nou.
    // Id-ul este generat de baza de date.
    public void Save(string firstName, string lastName, string email, long departmentId)
    {
        var entity = new Employee(firstName, lastName, email, departmentId);
        _repository.Save(entity);
    }

    // Input: long id, string firstName, string lastName, string email, long departmentId.
    // Return: void.
    // Actualizeaza datele unui angajat existent.
    // Trimite noua entitate la repository.
    public void Update(long id, string firstName, string lastName, string email, long departmentId)
    {
        var entity = new Employee(firstName, lastName, email, departmentId);
        _repository.Update(id, entity);
    }
    
    // Input: long id.
    // Return: void.
    // Sterge angajatul cu id-ul primit.
    // Operatia este delegata repository-ului.
    public void DeleteById(long id)
    {
        _repository.Delete(id);
    }

    // Input: bool isAdmin.
    // Return: IEnumerable<Employee>.
    // Returneaza inregistrarile sterse logic (doar admin).
    public IEnumerable<Employee> FindDeleted(bool isAdmin)
    {
        EnsureAdmin(isAdmin);
        return _employeesRepository.FindDeleted();
    }

    // Input: long id, bool isAdmin.
    // Return: void.
    // Restaureaza un angajat sters logic (doar admin).
    public void RestoreById(long id, bool isAdmin)
    {
        EnsureAdmin(isAdmin);
        _employeesRepository.Restore(id);
    }

    // Input: long id, bool isAdmin.
    // Return: void.
    // Sterge definitiv un angajat (doar admin).
    public void HardDeleteById(long id, bool isAdmin)
    {
        EnsureAdmin(isAdmin);
        _employeesRepository.HardDelete(id);
    }
    
    // Input: fara parametri.
    // Return: IEnumerable<Employee>.
    // Intoarce toti angajatii din baza de date.
    // Este util pentru incarcare fara filtre.
    public IEnumerable<Employee> FindAll()
    {
        return _repository.FindAll();
    }
    
    // Input: EmployeeFilter filter.
    // Return: IEnumerable<Employee>.
    // Intoarce angajatii filtrati dupa criteriile primite.
    // Criteriile sunt aplicate direct in query.
    public IEnumerable<Employee> FindAll(EmployeeFilter filter)
    {
        return _repository.FindAll(filter);
    }

    private static void EnsureAdmin(bool isAdmin)
    {
        if (!isAdmin)
            throw new UnauthorizedAccessException("Admin rights are required for this operation.");
    }
}