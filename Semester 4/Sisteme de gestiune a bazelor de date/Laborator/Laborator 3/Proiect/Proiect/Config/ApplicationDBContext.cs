using System.Diagnostics;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Logging;
using Proiect.Domain;

namespace Proiect.Config;

public class ApplicationDBContext : DbContext
{
    public DbSet<Department> departments { get; set; }
    public DbSet<Employee> employees { get; set; }
    
    public ApplicationDBContext(DbContextOptions<ApplicationDBContext> options) : base(options) { }
    
    protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
    {
        optionsBuilder 
            .UseNpgsql(AppConfig.Instance.GetConnectionString())
            .EnableSensitiveDataLogging()
            .LogTo(
                message => Console.WriteLine(message),
                new[] { DbLoggerCategory.Database.Command.Name },
                LogLevel.Information
            ); 
    }
}