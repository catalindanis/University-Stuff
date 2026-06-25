using FestivalMuzicaCSharp.Domain;
using System.Linq;

namespace Rest;

public static class ShowsClientRunner
{

    public static async Task RunAsync()
    {
        using var client = new ShowsClient();

        var newShow = new Show(0, "Test Artist", DateOnly.FromDateTime(DateTime.Now.AddDays(2)), "Test Hall", 100);
        var id = await client.SaveAsync(newShow);
        Console.WriteLine($"Created show id: {id}");

        var created = await client.FindByIdAsync(id);
        Console.WriteLine($"Fetched: {created}");

        if (created is not null)
        {
            created.RemainingSeats = 80;
            await client.UpdateAsync(id, created);
            Console.WriteLine($"Updated show id: {id}");
        }

        var all = await client.FindAllAsync();
        Console.WriteLine($"All shows: {string.Join(", ", all.Select(show => show.ToString()))}");

        var filter = new ShowFilter("Test Artist", null, null, null);
        var filtered = await client.FindAllFilteredAsync(filter);
        Console.WriteLine($"Filtered shows: {string.Join(", ", filtered.Select(show => show.ToString()))}");

        await client.DeleteAsync(id);
        Console.WriteLine($"Deleted show id: {id}");
    }
}
