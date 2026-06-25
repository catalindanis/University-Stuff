using Microsoft.EntityFrameworkCore;
using Proiect.Config;
using Proiect.Domain;
using Proiect.Service;

namespace Proiect.Utils;

public class SoftVsHardDeleteTests
{
    public void run()
    {
        DBContextFactory.Initialize(AppConfig.Instance.GetConnectionString());

        Console.WriteLine("=== Soft Delete vs Hard Delete (Employee) ===");

        var departmentId = EnsureDepartment();
        var softCandidateId = CreateEmployee("soft", departmentId);
        var hardCandidateId = CreateEmployee("hard", departmentId);

        Console.WriteLine($"Created employees: soft={softCandidateId}, hard={hardCandidateId}");

        EmployeesService.Instance.DeleteById(softCandidateId);
        var visibleAfterSoftDelete = ExistsInActiveSet(softCandidateId);
        var existsAfterSoftDelete = ExistsIgnoringFilters(softCandidateId);

        Console.WriteLine("After soft delete:");
        Console.WriteLine($"- visible in normal queries: {visibleAfterSoftDelete}");
        Console.WriteLine($"- exists physically in DB: {existsAfterSoftDelete}");

        EmployeesService.Instance.HardDeleteById(hardCandidateId, isAdmin: true);
        var visibleAfterHardDelete = ExistsInActiveSet(hardCandidateId);
        var existsAfterHardDelete = ExistsIgnoringFilters(hardCandidateId);

        Console.WriteLine("After hard delete:");
        Console.WriteLine($"- visible in normal queries: {visibleAfterHardDelete}");
        Console.WriteLine($"- exists physically in DB: {existsAfterHardDelete}");

        var softAudit = HasAuditEntry(softCandidateId, "soft-delete");
        var hardAudit = HasAuditEntry(hardCandidateId, "hard-delete");
        Console.WriteLine($"Audit trail captured soft delete: {softAudit}");
        Console.WriteLine($"Audit trail captured hard delete: {hardAudit}");

        var deletedIds = EmployeesService.Instance.FindDeleted(isAdmin: true)
            .Select(e => e.Id)
            .ToHashSet();
        Console.WriteLine($"Deleted list contains soft candidate: {deletedIds.Contains(softCandidateId)}");

        EmployeesService.Instance.RestoreById(softCandidateId, isAdmin: true);
        var visibleAfterRestore = ExistsInActiveSet(softCandidateId);
        Console.WriteLine($"After restore, soft candidate visible: {visibleAfterRestore}");

        Console.WriteLine("=== End Demo ===");
    }

    private static bool HasAuditEntry(long entityId, string deleteType)
    {
        using var context = DBContextFactory.CreateContext();
        return context.auditLogs
            .AsNoTracking()
            .Any(log => log.EntityName == nameof(Employee)
                        && log.EntityId == entityId
                        && log.DeleteType == deleteType);
    }

    private static long EnsureDepartment()
    {
        using var context = DBContextFactory.CreateContext();
        var existingId = context.departments
            .AsNoTracking()
            .Select(d => d.Id)
            .FirstOrDefault();

        if (existingId != 0)
            return existingId;

        var department = new Department("Temporary", "Bucharest");
        context.departments.Add(department);
        context.SaveChanges();
        return department.Id;
    }

    private static long CreateEmployee(string marker, long departmentId)
    {
        // Keep email <= 50 chars to work with schemas that still use varchar(50).
        var safeMarker = new string((marker ?? "e")
            .Where(char.IsLetterOrDigit)
            .Take(6)
            .ToArray())
            .ToLowerInvariant();

        if (string.IsNullOrWhiteSpace(safeMarker))
            safeMarker = "e";

        var token = Guid.NewGuid().ToString("N").Substring(0, 8);
        var uniqueEmail = $"{safeMarker}.{token}@d.local";

        var employee = new Employee("Delete", "Demo", uniqueEmail, departmentId)
        {
            Salary = 3000
        };

        using var context = DBContextFactory.CreateContext();
        context.employees.Add(employee);
        context.SaveChanges();
        return employee.Id;
    }

    private static bool ExistsInActiveSet(long employeeId)
    {
        using var context = DBContextFactory.CreateContext();
        return context.employees.AsNoTracking().Any(e => e.Id == employeeId);
    }

    private static bool ExistsIgnoringFilters(long employeeId)
    {
        using var context = DBContextFactory.CreateContext();
        return context.employees
            .IgnoreQueryFilters()
            .AsNoTracking()
            .Any(e => e.Id == employeeId);
    }
}
