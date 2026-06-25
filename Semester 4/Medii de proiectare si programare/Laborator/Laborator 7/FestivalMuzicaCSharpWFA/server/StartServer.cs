using FestivalMuzicaCSharp.Repository;
using log4net;
using log4net.Config;
using networking;
using System;
using System.Configuration;
using System.Net.Sockets;
using System.Reflection;
using networking.protocol;

namespace server;

public class StartServer
{
    private static int DEFAULT_PORT = 55555;
    private static String DEFAULT_IP = "127.0.0.1";
    private static readonly ILog log = LogManager.GetLogger(typeof(StartServer));

    public static void Main(string[] args)
    {
        var logRepository = LogManager.GetRepository(Assembly.GetEntryAssembly());
        XmlConfigurator.Configure(logRepository, new FileInfo("log4net.config"));

        log.Info("Starting chat server");
        log.Info("Reading properties from app.config ...");
        int port = DEFAULT_PORT;
        String ip = DEFAULT_IP;
        String portS = ConfigurationManager.AppSettings["port"];
        if (portS == null)
        {
            log.Debug("Port property not set. Using default value " + DEFAULT_PORT);
        }
        else
        {
            bool result = Int32.TryParse(portS, out port);
            if (!result)
            {
                log.Debug("Port property not a number. Using default value " + DEFAULT_PORT);
                port = DEFAULT_PORT;
                log.Debug("Portul " + port);
            }
        }
        String ipS = ConfigurationManager.AppSettings["ip"];

        if (ipS == null)
        {
            log.Info("Port property not set. Using default value " + DEFAULT_IP);
        }
        log.InfoFormat("Configuration Settings for database {0}", GetConnectionStringByName("musicFestivalDB"));
        IDictionary<String, string> props = new SortedList<String, String>();
        props.Add("ConnectionString", GetConnectionStringByName("musicFestivalDB"));
        IUsersRepository userRepo = new UsersDbRepository(props);
        IShowsRepository showsRepository = new ShowsDbRepository(props);
        ITicketsRepository ticketsRepository = new TicketsDbRepository(props);
        IService serviceImpl = new ServiceImpl(userRepo, showsRepository, ticketsRepository);

        log.DebugFormat("Starting server on IP {0} and port {1}", ip, port);
        ConcurrentServer server = new SerialChatServer(ip,port, serviceImpl);
        server.Start();

        log.Debug("Server started ...");
    }

    static string GetConnectionStringByName(string name)
    {
        string returnValue = null;

        ConnectionStringSettings settings = ConfigurationManager.ConnectionStrings[name];

        if (settings != null)
            returnValue = settings.ConnectionString;

        return returnValue;
    }
}

public class SerialChatServer : ConcurrentServer
{
    private IService server;
    private ClientWorker worker;
    public SerialChatServer(string host, int port, IService server) : base(host, port)
    {
        this.server = server;
        Console.WriteLine("Server started...");
    }
    protected override Thread createWorker(TcpClient client)
    {
        worker = new ClientWorker(server, client);
        return new Thread(new ThreadStart(worker.run));
    }
}
