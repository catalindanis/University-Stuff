using Proiect.Config;
using Proiect.Domain;
using Proiect.Utils;
using Microsoft.EntityFrameworkCore;

namespace Proiect.Repository;

// Realizeaza operatii SQL pentru tabela departments folosind Entity Framework.
public class DepartmentsRepository : IRepository<long, Department>
{
    // Input: fara parametri.
    // Return: int.
    // Calculeaza numarul total de randuri din departments.
    public int Size()
    {
        using var context = DBContextFactory.CreateContext();
        return context.departments.Count();
    }

    // Input: Department entity.
    // Return: void.
    // Insereaza un departament nou in baza de date.
    public void Save(Department entity)
    {
        using var context = DBContextFactory.CreateContext();
        context.departments.Add(entity);
        context.SaveChanges();
    }

    // Input: long id.
    // Return: void.
    // Sterge departamentul cu id-ul primit.
    public void Delete(long id)
    {
        using var context = DBContextFactory.CreateContext();
        var entity = context.departments.Find(id);
        if (entity != null)
        {
            context.departments.Remove(entity);
            context.SaveChanges();
        }
    }

    // Input: long id, Department entity.
    // Return: void.
    // Actualizeaza nume si locatie pentru departamentul selectat.
    public void Update(long id, Department entity)
    {
        using var context = DBContextFactory.CreateContext();
        var existing = context.departments.Find(id);
        if (existing != null)
        {
            existing.Name = entity.Name;
            existing.Location = entity.Location;
            context.SaveChanges();
        }
    }

    // Input: long id.
    // Return: Department.
    // Citeste un singur departament dupa id.
    public Department FindById(long id)
    {
        using var context = DBContextFactory.CreateContext();
        var entity = context.departments.Find(id);
        if (entity != null)
            return entity;
        throw new Exception("Department not found");
    }

    // Input: fara parametri.
    // Return: IEnumerable<Department>.
    // Citeste toate departamentele din tabela.
    public IEnumerable<Department> FindAll()
    {
        using var context = DBContextFactory.CreateContext();
        return context.departments.ToList();
    }
}