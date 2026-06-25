using Microsoft.EntityFrameworkCore;
using Proiect.Config;
using Proiect.Domain;

namespace Proiect.Utils;

public class OptimisticLockingTests
{
    public void run()
    {
        DBContextFactory.Initialize(AppConfig.Instance.GetConnectionString());

        Console.WriteLine("=== Optimistic Locking Demo (2 concurrent updates) ===");

        var employeeId = EnsureTestEmployee();
        Console.WriteLine($"Using employee id={employeeId}");

        using (var initialContext = DBContextFactory.CreateContext())
        {
            var initial = initialContext.employees.AsNoTracking().Single(e => e.Id == employeeId);
            Console.WriteLine($"Initial salary: {initial.Salary}");
        }

        var start = new ManualResetEventSlim(false);

        var taskA = Task.Run(() =>
        {
            using var contextA = DBContextFactory.CreateContext();
            var employeeA = contextA.employees.Single(e => e.Id == employeeId);

            start.Wait();
            employeeA.Salary += 100;
            contextA.SaveChanges();

            var forcedVersion = Guid.NewGuid().ToByteArray();
            contextA.Database.ExecuteSqlRaw(
                "UPDATE employees SET \"RowVersion\" = {0} WHERE id = {1}",
                forcedVersion,
                employeeId
            );

            Console.WriteLine("[A] Update committed.");
        });

        var taskB = Task.Run(() =>
        {
            using var contextB = DBContextFactory.CreateContext();
            var employeeB = contextB.employees.Single(e => e.Id == employeeId);

            start.Wait();
            Thread.Sleep(120);
            employeeB.Salary += 200;

            try
            {
                contextB.SaveChanges();
                Console.WriteLine("[B] Unexpected: update also committed (no conflict). Check RowVersion strategy.");
            }
            catch (DbUpdateConcurrencyException)
            {
                Console.WriteLine("[B] Concurrency conflict caught (DbUpdateConcurrencyException).");
            }
        });

        start.Set();
        Task.WaitAll(taskA, taskB);

        using var verificationContext = DBContextFactory.CreateContext();
        var finalEmployee = verificationContext.employees.AsNoTracking().Single(e => e.Id == employeeId);
        Console.WriteLine($"Final salary in DB: {finalEmployee.Salary}");
        Console.WriteLine("=== End Demo ===");
    }

    private static long EnsureTestEmployee()
    {
        using var context = DBContextFactory.CreateContext();

        var existing = context.employees
            .AsNoTracking()
            .FirstOrDefault(e => e.Email == "optimistic.locking@test.local");

        if (existing != null)
            return existing.Id;

        var departmentId = context.departments
            .AsNoTracking()
            .Select(d => d.Id)
            .FirstOrDefault();

        if (departmentId == 0)
            throw new InvalidOperationException("No department found. Create at least one department before running this demo.");

        var employee = new Employee("Optimistic", "Locking", "optimistic.locking@test.local", departmentId)
        {
            Salary = 1000
        };

        context.employees.Add(employee);
        context.SaveChanges();
        return employee.Id;
    }
}
