namespace ExempleCurs12.Set2ExempleLinq;

public class Exercitiu2
{
    public static void Run()
    {
        //Se da o lista cu inscrieri (ce contine duplicate) si o lista cu pasari descalificate
        string[] inscrieriBrute = { "Rata", "Gasca", "Rata", "Pelican", "Barza", "Gasca", "Rata", "Soim" };
        string[] pasariDescalificate = { "Pelican", "Barza" };

        //obtineti lista de participanti unici ce s-au inscris la cursa(RaceEvent)
        //TODO

        //din lista de participanti unici, eliminati pasarile ce au fost descalificate (-> participantii valizi)
        //TODO

        //grupati participantii ramasi dupa prima litera a numelui lor
        //TODO

        //creati un dictionar din lista de participanti valizi
        //unde Cheia este numele pasarii, iar Valoarea este lungimea numelui (int)
        //TODO
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

    public static void Rezolvare()
    {
        //date initiale
        string[] inscrieriBrute = { "Rata", "Gasca", "Rata", "Pelican", "Barza", "Gasca", "Rata", "Soim" };
        string[] pasariDescalificate = { "Pelican", "Barza" };

        //obtineti lista de participanti unici ce s-au inscris la cursa(RaceEvent)
        var participantiUnici = inscrieriBrute.Distinct();

        Console.WriteLine("--- Unici ---");
        Console.WriteLine(string.Join(", ", participantiUnici));


        //din lista de participanti unici, eliminati pasarile ce au fost descalificate
        var participantiValizi = participantiUnici.Except(pasariDescalificate);

        Console.WriteLine("\n--- Valizi (fara descalificati) ---");
        Console.WriteLine(string.Join(", ", participantiValizi));


        //grupati participantii ramasi dupa prima litera a numelui lor
        var grupuriPasari = participantiValizi.GroupBy(nume => nume[0]);

        Console.WriteLine("\n--- Grupuri (dupa prima litera) ---");
        foreach (var grup in grupuriPasari)
        {
            Console.WriteLine($"Litera: {grup.Key}");
            foreach (var pasare in grup)
            {
                Console.WriteLine($"   - {pasare}");
            }
        }


        //creati un dictionar din lista de participanti valizi
        //unde Cheia este numele pasarii, iar Valoarea este lungimea numelui (int)
        var dictionarLungimi = participantiValizi.ToDictionary(nume => nume, nume => nume.Length);

        Console.WriteLine("\n--- Dictionar (Nume -> Lungime) ---");
        foreach (var kvp in dictionarLungimi)
        {
            Console.WriteLine($"Cheie: {kvp.Key}, Valoare: {kvp.Value}");
        }
    }
}