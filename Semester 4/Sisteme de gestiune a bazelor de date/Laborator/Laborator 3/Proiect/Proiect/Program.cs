using Proiect.Config;
using Proiect.Domain;
using Proiect.Repository;
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
        
        ConnectionLeakTests connectionLeakTests = new ConnectionLeakTests();
        connectionLeakTests.RunLeakVsFixDemo();
        
        // DBContextFactory.Initialize(AppConfig.Instance.GetConnectionString());
        // ApplicationConfiguration.Initialize();
        // Application.Run(new MainMenu());
    }
}