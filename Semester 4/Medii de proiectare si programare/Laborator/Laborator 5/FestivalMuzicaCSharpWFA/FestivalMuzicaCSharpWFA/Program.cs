using FestivalMuzicaCSharpWFA.Service;
using FestivalMuzicaCSharpWFA.UI;
using FestivalMuzicaCSharpWFA.Utils;

namespace FestivalMuzicaCSharp;

public class Program
{
    [STAThread]
    public static void Main()
    {
        Application.EnableVisualStyles();
        Application.SetCompatibleTextRenderingDefault(false);
        var authForm = new Auth();
        Navigator.SetMainForm(authForm);
        Navigator.NavigateTo(authForm, "Authenticate");
        Application.Run(authForm);
    }
}
