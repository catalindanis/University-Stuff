using System.Diagnostics;
using Proiect.Service;

namespace Proiect.Utils;

public class CachingTests
{
    public void run()
    {
        CacheMiss();
        CacheHit();
    }
    
    private void CacheMiss()
    {
        Stopwatch sw = Stopwatch.StartNew();
        DepartmentsService.Instance.FindById(1);
        sw.Stop();
        Console.WriteLine($"Cache miss test: {sw.ElapsedMilliseconds} ms");
    }

    private void CacheHit()
    {
        Stopwatch sw = Stopwatch.StartNew();
        DepartmentsService.Instance.FindById(1);
        sw.Stop();
        Console.WriteLine($"Cache hit test: {sw.ElapsedMilliseconds} ms");
    }
}