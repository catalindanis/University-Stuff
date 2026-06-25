using System.Diagnostics;
using Microsoft.EntityFrameworkCore;

namespace Proiect.Utils;

public class UpdatingTests
{
    public void run()
    {
        IndividualUpdates();
        BulkUpdateQuery();
        BatchUpdates();
    }
    
    private void IndividualUpdates()
    {
        using var context = DBContextFactory.CreateContext();
        var items = context.departments.ToList();
        var sw = Stopwatch.StartNew();

        foreach (var d in items)
        {
            d.Location = (d.Location ?? string.Empty) + "_ind";
            context.SaveChanges();
        }

        sw.Stop();
        Console.WriteLine($"IndividualUpdates: updated {items.Count} rows in {sw.ElapsedMilliseconds} ms.");
    }
    
    private void BulkUpdateQuery()
    {
        using var context = DBContextFactory.CreateContext();
        var sw = Stopwatch.StartNew();

        var newValue = "bulk_updated_" + DateTime.UtcNow.Ticks;
        var sql = "UPDATE departments SET Location = {0}";
        var affected = context.Database.ExecuteSqlRaw(sql, newValue);

        sw.Stop();
        Console.WriteLine($"BulkUpdateQuery: affected {affected} rows in {sw.ElapsedMilliseconds} ms.");
    }
    
    private void BatchUpdates()
    {
        using var context = DBContextFactory.CreateContext();
        var items = context.departments.ToList();
        var sw = Stopwatch.StartNew();

        const int batchSize = 50;
        for (int i = 0; i < items.Count; i++)
        {
            items[i].Location = (items[i].Location ?? string.Empty) + "_batch";
            if ((i + 1) % batchSize == 0)
            {
                context.SaveChanges();
            }
        }

        context.SaveChanges();
        sw.Stop();
        Console.WriteLine($"BatchUpdates: updated {items.Count} rows in {sw.ElapsedMilliseconds} ms (batchSize={batchSize}).");
    }
}