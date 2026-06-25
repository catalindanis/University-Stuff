using System.Diagnostics;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Design;
using Microsoft.Extensions.Logging;
using Proiect.Domain;

namespace Proiect.Config;

public class ApplicationDBContext : DbContext
{
    public DbSet<Department> departments { get; set; }
    public DbSet<Employee> employees { get; set; }
    public DbSet<Project> projects { get; set; }
    public DbSet<AuditLog> auditLogs { get; set; }
    public DbSet<Customer> Customers { get; set; }
    
    public ApplicationDBContext(DbContextOptions<ApplicationDBContext> options) : base(options) { }
    
    protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
    {
        optionsBuilder
            .UseNpgsql(AppConfig.Instance.GetConnectionString())
            .EnableSensitiveDataLogging();
        // .LogTo(
        //     message => Console.WriteLine(message),
        //     new[] { DbLoggerCategory.Database.Command.Name },
        //     LogLevel.Information
        // );
        // .UseLazyLoadingProxies();
    }

    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        base.OnModelCreating(modelBuilder);

        modelBuilder.Entity<Employee>()
            .HasIndex(e => e.Email)
            .IsUnique();
        
        modelBuilder.Entity<Employee>()
            .HasQueryFilter(e => !e.IsDeleted);
    }
}

public class DesignTimeApplicationDBContextFactory : IDesignTimeDbContextFactory<ApplicationDBContext>
{
    public ApplicationDBContext CreateDbContext(string[] args)
    {
        var optionsBuilder = new DbContextOptionsBuilder<ApplicationDBContext>();
        optionsBuilder.UseNpgsql(AppConfig.Instance.GetConnectionString());
        return new ApplicationDBContext(optionsBuilder.Options);
    }
}