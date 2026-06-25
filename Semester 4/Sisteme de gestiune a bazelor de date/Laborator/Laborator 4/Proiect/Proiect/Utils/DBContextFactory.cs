using Microsoft.EntityFrameworkCore;
using Proiect.Config;

namespace Proiect.Utils;

public class DBContextFactory
{
    private static string _connectionString;
    public static void Initialize(string connectionString)
    {
        _connectionString = connectionString;
    }
    public static ApplicationDBContext CreateContext()
    {
        var optionsBuilder = new DbContextOptionsBuilder<ApplicationDBContext>(); 
        optionsBuilder.UseNpgsql(_connectionString);
        return new ApplicationDBContext(optionsBuilder.Options);
    }

}