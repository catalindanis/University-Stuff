using Proiect.Config;
using Proiect.Service;
using Proiect.Utils;

namespace Proiect;

static class Program
{
    // Input: fara parametri.
    // Return: void.
    // Initializeaza configurarea WinForms.
    // Porneste ecranul principal al aplicatiei.
    static void Main()
    {
        // ConnectionPoolingTests connectionPoolingTests = new ConnectionPoolingTests();
        // connectionPoolingTests.RunPoolingTests();
        
        // ConnectionLeakTests connectionLeakTests = new ConnectionLeakTests();
        // connectionLeakTests.RunLeakVsFixDemo();
        
        // N + 1
        // NPOQueries npoQueries = new NPOQueries();
        // npoQueries.run();
        
        // Indexing
        // IndexingTests indexingTests = new IndexingTests();
        // indexingTests.run();
        
        // Caching
        // CachingTests cachingTests = new CachingTests();
        // cachingTests.run();
        
        // Updating
        // UpdatingTests updatingTests = new UpdatingTests();
        // updatingTests.run();
        
        // StatementsCachingTests statementsCachingTests = new StatementsCachingTests();
        // statementsCachingTests.run();

        // Soft delete vs hard delete
        // SoftVsHardDeleteTests softVsHardDeleteTests = new SoftVsHardDeleteTests();
        // softVsHardDeleteTests.run();

        // Optimistic locking
        // OptimisticLockingTests optimisticLockingTests = new OptimisticLockingTests();
        // optimisticLockingTests.run();
        
        // DBContextFactory.Initialize(AppConfig.Instance.GetConnectionString());
        // ApplicationConfiguration.Initialize();
        // Application.Run(new MainMenu());
        
        UnrepeatableRead unrepeatableRead = new UnrepeatableRead();
        unrepeatableRead.run();
    }
}