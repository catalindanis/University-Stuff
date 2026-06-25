using FestivalMuzicaClient;
using FestivalMuzicaCSharpWFA.UI;
using FestivalMuzicaCSharpWFA.Utils;
using log4net;
using log4net.Config;
using networking.protocol;
using System.Configuration;
using System.Reflection;

namespace FestivalMuzicaCSharp;

public class Program
{
    private static int DEFAULT_PORT = 55555;
    private static String DEFAULT_IP = "127.0.0.1";
    private static readonly ILog log = LogManager.GetLogger(typeof(Program));

    [STAThread]
    public static void Main()
    {
        var logRepository = LogManager.GetRepository(Assembly.GetEntryAssembly());
        XmlConfigurator.Configure(logRepository, new FileInfo("log4net.config"));

        log.Debug("Reading properties from app.config ...");
        int port = DEFAULT_PORT;
        String ip = DEFAULT_IP;
        String portS = ConfigurationManager.AppSettings["port"];
        if (portS == null)
        {
            log.DebugFormat("Port property not set. Using default value {0}", DEFAULT_PORT);
        }
        else
        {
            bool result = Int32.TryParse(portS, out port);
            if (!result)
            {
                log.DebugFormat("Port property not a number. Using default value {0}", DEFAULT_PORT);
                port = DEFAULT_PORT;
                log.DebugFormat("Portul {0}", port);
            }
        }
        String ipS = ConfigurationManager.AppSettings["ip"];

        if (ipS == null)
        {
            log.DebugFormat("Port property not set. Using default value {0}", DEFAULT_IP);
        }

        log.InfoFormat("Using  server on IP {0} and port {1}", ip, port);

        IService service = new ServerProxy(ip, port);
        Controller controller = new Controller(service);

        Application.EnableVisualStyles();
        Application.SetCompatibleTextRenderingDefault(false);
        var authForm = new Auth(controller);
        Navigator.SetMainForm(authForm);
        Navigator.NavigateTo(authForm, "Authenticate");
        Application.Run(authForm);
    }
}
