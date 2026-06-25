using Proiect.Config;
using Proiect.Service;

namespace Proiect;

static class Program
{
    // Input: fara parametri.
    // Return: void.
    // Initializeaza configurarea WinForms.
    // Porneste ecranul principal al aplicatiei.
    static void Main()
    {
        ApplicationConfiguration.Initialize();
        Application.Run(new MainMenu());
    }
}