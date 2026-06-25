using System.Diagnostics;
using Proiect.Domain;
using Proiect.Service;

namespace Proiect.Utils;

public class NPOQueries
{
    public void run()
    {
        var sw = Stopwatch.StartNew();
        IEnumerable<Department> departments = DepartmentsService.Instance.FindAll();
        foreach (Department department in departments)
        {
            Console.WriteLine(department.Employees.Count);
        }
        sw.Stop();
        Console.WriteLine($"Time taken: {sw.ElapsedMilliseconds} ms");
    }
}


