using System.Configuration;
using System.Reflection;
using FestivalMuzicaCSharp.Domain;
using FestivalMuzicaCSharp.Repository;
using log4net;
using log4net.Config;

namespace FestivalMuzicaCSharp;

public class Program
{
    public static void Main()
    {
        var logRepository = LogManager.GetRepository(Assembly.GetEntryAssembly());
        XmlConfigurator.Configure(logRepository, new FileInfo("log4net.config"));
        
        IDictionary<String, string> props = new SortedList<String, String>();
        props.Add("ConnectionString", GetConnectionStringByName("musicFestivalDB"));
        
        IUsersRepository usersRepository = new UsersDbRepository(props);
        IShowsRepository showsRepository = new ShowsDbRepository(props);
        ITicketsRepository ticketsRepository = new TicketsDBRepository(props);
        
        foreach (var user in usersRepository.FindAll())
        {
            Console.WriteLine(user);
        }

        showsRepository.Save(new Show(-1, "Artist", new DateOnly(2024, 7, 1), "Location", 100));
        showsRepository.FindAll().ToList().ForEach(show => Console.WriteLine(show));
        
        ticketsRepository.Save(new Ticket(-1, "Client", showsRepository.FindAll().First(), 2));
        ticketsRepository.FindAll().ToList().ForEach(ticket => Console.WriteLine(ticket));
        
        ticketsRepository.Delete(ticketsRepository.FindAll().First().Id);
        showsRepository.Delete(showsRepository.FindAll().First().Id);
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