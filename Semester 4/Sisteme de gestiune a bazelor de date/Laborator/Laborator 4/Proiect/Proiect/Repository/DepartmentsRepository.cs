using Proiect.Domain;
using Proiect.Utils;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Caching.Memory;
using Microsoft.Extensions.Options;

namespace Proiect.Repository;

// Realizeaza operatii SQL pentru tabela departments folosind Entity Framework.
public class DepartmentsRepository : IPageableRepository<long, Department>
{
    private readonly IMemoryCache _cache = new MemoryCache(Options.Create(new MemoryCacheOptions()));
    private static string CacheKey(long id) => $"dept_{id}";
    
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
        InvalidateDepartmentCache(entity.Id);
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
        InvalidateDepartmentCache(id);
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
        InvalidateDepartmentCache(id);
    }

    // Input: long id.
    // Return: Department.
    // Citeste un singur departament dupa id.
    public Department FindById(long id)
    {
        return _cache.GetOrCreate($"dept_{id}", entry =>
        {
            entry.AbsoluteExpirationRelativeToNow = TimeSpan.FromMinutes(5);
            using var context = DBContextFactory.CreateContext();
            var entity = context.departments.Find(id);
            if (entity != null)
                return entity;
            throw new Exception("Department not found");
        }) ?? throw new InvalidOperationException();
    }

    // Input: fara parametri.
    // Return: IEnumerable<Department>.
    // Citeste toate departamentele din tabela.
    public IEnumerable<Department> FindAll()
    {
        using var context = DBContextFactory.CreateContext();
        return context.departments
            .Include(department => department.Employees)
            .ToList();
    }

    // Input: int pageNumber (1-based), int pageSize.
    // Return: PaginatedResponse<Department>.
    // Citeste o pagina de departamente cu metadate de paginare.
    public PaginatedResponse<Department> FindAllPaginated(int pageNumber, int pageSize)
    {
        if (pageNumber < 1) pageNumber = 1;
        if (pageSize < 1) pageSize = 10;

        using var context = DBContextFactory.CreateContext();
        int totalRecords = context.departments.Count();
        int skip = (pageNumber - 1) * pageSize;

        var data = context.departments
            .Include(d => d.Employees)
            .OrderBy(d => d.Id)
            .Skip(skip)
            .Take(pageSize)
            .ToList();

        return new PaginatedResponse<Department>(data, pageNumber, pageSize, totalRecords);
    }
    
    // Input: long? lastId (null for first page), int pageSize.
    // Return: PaginatedResponse<Department>.
    // Citeste o pagina de departamente dupa cursor (lastId) cu metadate similare paginarii offset-based.
    public PaginatedResponse<Department> FindAllPaginatedByCursor(long? lastId, int pageSize)
    {
        if (pageSize < 1) pageSize = 10;

        using var context = DBContextFactory.CreateContext();

        int totalRecords = context.departments.Count();
        int recordsBeforeCursor = 0;

        IQueryable<Department> query = context.departments;

        if (lastId.HasValue)
        {
            recordsBeforeCursor = context.departments.Count(d => d.Id <= lastId.Value);
            query = query.Where(d => d.Id > lastId.Value);
        }

        var data = query
            .Include(d => d.Employees)
            .OrderBy(d => d.Id)
            .Take(pageSize)
            .ToList();

        int totalPages = totalRecords == 0 ? 1 : (totalRecords + pageSize - 1) / pageSize;
        int pageNumber = (recordsBeforeCursor / pageSize) + 1;
        if (pageNumber > totalPages) pageNumber = totalPages;

        return new PaginatedResponse<Department>(data, pageNumber, pageSize, totalRecords);
    }
    
    private void InvalidateDepartmentCache(long id)
    {
        _cache.Remove(CacheKey(id));
    }
}