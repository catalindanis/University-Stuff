using Microsoft.EntityFrameworkCore;
using IsolationLevel = System.Data.IsolationLevel;

namespace Proiect.Utils;

public class UnrepeatableRead{
    public void run()
    {
        initData();

        using var context1 = DBContextFactory.CreateContext();
        using var context2 = DBContextFactory.CreateContext();

        using var tx1 = context1.Database.BeginTransaction(IsolationLevel.ReadCommitted);
        using var tx2 = context2.Database.BeginTransaction(IsolationLevel.ReadCommitted);

        var customer = context1.Customers.AsNoTracking().First();
        Console.WriteLine($"Context1 - Initial read: {customer?.FirstName} {customer?.LastName}");

        var customerToUpdate = context2.Customers.First();
        if (customerToUpdate != null)
        {
            customerToUpdate.FirstName = "UpdatedFirstName";
            context2.SaveChanges();
            tx2.Commit();
        }

        context1.ChangeTracker.Clear();

        var customerAfterUpdate = context1.Customers.AsNoTracking().First();
        Console.WriteLine($"Context1 - After update in Context2: {customerAfterUpdate?.FirstName} {customerAfterUpdate?.LastName}");

        tx1.Commit();
    }

    private void initData()
    {
        using var context = DBContextFactory.CreateContext();

        context.Customers.RemoveRange(context.Customers);
        context.SaveChanges();

        var customer = new Domain.Customer("John", "Doe", "email@gmail.com", "1234567890");
        context.Customers.Add(customer);
        context.SaveChanges();
    }
}