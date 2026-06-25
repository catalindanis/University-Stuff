using System.Diagnostics;
using Microsoft.EntityFrameworkCore;
using Proiect.Config;

namespace Proiect.Utils;

public class StatementsCachingTests
{
    private const int Iterations = 1000;

    public void run()
    {
        using var context = DBContextFactory.CreateContext();
        var employeeIds = context.employees
            .AsNoTracking()
            .Select(e => e.Id)
            .Take(Iterations)
            .ToList();

        if (employeeIds.Count == 0)
        {
            return;
        }

        TestA_NoReuse(context, employeeIds);
        TestB_WithReuse(context, employeeIds);
    }

    private static void TestA_NoReuse(ApplicationDBContext context, IReadOnlyList<long> employeeIds)
    {
        var sw = Stopwatch.StartNew();
        int found = 0;

        for (int i = 0; i < Iterations; i++)
        {
            long id = employeeIds[i % employeeIds.Count];
            var query = context.employees
                .AsNoTracking()
                .Where(e => e.Id == id);

            if (query.FirstOrDefault() != null)
                found++;
        }

        sw.Stop();
        Console.WriteLine($"Test A - Fără reutilizare: {sw.ElapsedMilliseconds} ms, rezultate găsite: {found}/{Iterations}");
    }

    private static void TestB_WithReuse(ApplicationDBContext context, IReadOnlyList<long> employeeIds)
    {
        var parameter = new QueryParameter { Id = employeeIds[0] };
        var query = context.employees
            .AsNoTracking()
            .Where(e => e.Id == parameter.Id);

        var sw = Stopwatch.StartNew();
        int found = 0;

        for (int i = 0; i < Iterations; i++)
        {
            parameter.Id = employeeIds[i % employeeIds.Count];

            if (query.FirstOrDefault() != null)
                found++;
        }

        sw.Stop();
        Console.WriteLine($"Test B - Cu reutilizare: {sw.ElapsedMilliseconds} ms, rezultate găsite: {found}/{Iterations}");
    }

    private sealed class QueryParameter
    {
        public long Id { get; set; }
    }
}