using Proiect.Config;
using Proiect.Domain;
using Proiect.Utils;
using Microsoft.EntityFrameworkCore;
using System.Collections.Generic;

namespace Proiect.Repository;

// Realizeaza operatii SQL pentru tabela employees folosind Entity Framework.
public class EmployeesRepository : IFilterableRepository<long, Employee, EmployeeFilter>
{
    // Input: fara parametri.
    // Return: int.
    // Calculeaza numarul total de angajati.
    public int Size()
    {
        using var context = DBContextFactory.CreateContext();
        return context.employees.Count();
    }

    // Input: Employee entity.
    // Return: void.
    // Insereaza un angajat nou in baza de date.
    public void Save(Employee entity)
    {
        using var context = DBContextFactory.CreateContext();
        context.employees.Add(entity);
        context.SaveChanges();
    }

    // Input: long id.
    // Return: void.
    // Marcheaza angajatul ca sters logic (soft delete).
    public void Delete(long id)
    {
        using var context = DBContextFactory.CreateContext();
        var entity = context.employees
            .IgnoreQueryFilters()
            .SingleOrDefault(e => e.Id == id);

        if (entity == null || entity.IsDeleted)
            return;

        entity.IsDeleted = true;
        AuditTrailLogger.LogDelete(context, nameof(Employee), entity.Id, "soft-delete");
        context.SaveChanges();
    }

    // Input: long id, Employee entity.
    // Return: void.
    // Actualizeaza datele unui angajat existent.
    public void Update(long id, Employee entity)
    {
        using var context = DBContextFactory.CreateContext();
        var existing = context.employees.Find(id);
        if (existing != null)
        {
            existing.FirstName = entity.FirstName;
            existing.LastName = entity.LastName;
            existing.Email = entity.Email;
            existing.DepartmentId = entity.DepartmentId;
            context.SaveChanges();
        }
    }

    // Input: long id.
    // Return: Employee.
    // Citeste un singur angajat dupa id.
    public Employee FindById(long id)
    {
        using var context = DBContextFactory.CreateContext();
        var entity = context.employees.Find(id);
        if (entity != null)
            return entity;
        throw new Exception("Employee not found");
    }

    // Input: fara parametri.
    // Return: IEnumerable<Employee>.
    // Citeste toti angajatii din tabela.
    public IEnumerable<Employee> FindAll()
    {
        using var context = DBContextFactory.CreateContext();
        return context.employees.Include(e => e.Department).ToList();
    }

    // Input: EmployeeFilter filter.
    // Return: IEnumerable<Employee>.
    // Returneaza doar angajatii care respecta criteriile.
    public IEnumerable<Employee> FindAll(EmployeeFilter filter)
    {
        using var context = DBContextFactory.CreateContext();
        var query = context.employees.Include(e => e.Department).AsQueryable();
        if (!string.IsNullOrWhiteSpace(filter?.FirstName))
            query = query.Where(e => EF.Functions.ILike(e.FirstName, $"%{filter.FirstName.Trim()}%"));
        if (!string.IsNullOrWhiteSpace(filter?.LastName))
            query = query.Where(e => EF.Functions.ILike(e.LastName, $"%{filter.LastName.Trim()}%"));
        if (!string.IsNullOrWhiteSpace(filter?.Email))
            query = query.Where(e => EF.Functions.ILike(e.Email, $"%{filter.Email.Trim()}%"));
        if (filter?.DepartmentId.HasValue ?? false)
            query = query.Where(e => e.DepartmentId == filter.DepartmentId.Value);
        return query.ToList();
    }

    // Input: fara parametri.
    // Return: IEnumerable<Employee>.
    // Returneaza doar angajatii marcati ca stersi (admin view).
    public IEnumerable<Employee> FindDeleted()
    {
        using var context = DBContextFactory.CreateContext();
        return context.employees
            .IgnoreQueryFilters()
            .Where(e => e.IsDeleted)
            .Include(e => e.Department)
            .ToList();
    }

    // Input: long id.
    // Return: void.
    // Restaureaza un angajat sters logic.
    public void Restore(long id)
    {
        using var context = DBContextFactory.CreateContext();
        var entity = context.employees
            .IgnoreQueryFilters()
            .SingleOrDefault(e => e.Id == id);

        if (entity == null || !entity.IsDeleted)
            return;

        entity.IsDeleted = false;
        context.SaveChanges();
    }

    // Input: long id.
    // Return: void.
    // Sterge fizic angajatul din baza de date.
    public void HardDelete(long id)
    {
        using var context = DBContextFactory.CreateContext();
        var entity = context.employees
            .IgnoreQueryFilters()
            .SingleOrDefault(e => e.Id == id);

        if (entity == null)
            return;

        AuditTrailLogger.LogDelete(context, nameof(Employee), entity.Id, "hard-delete");
        context.employees.Remove(entity);
        context.SaveChanges();
    }
}