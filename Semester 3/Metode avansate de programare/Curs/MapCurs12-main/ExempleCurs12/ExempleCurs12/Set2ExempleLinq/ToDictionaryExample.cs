namespace ExempleCurs12.Set2ExempleLinq;

public class Produs
{
    public int Id { get; set; }
    public string Nume { get; set; }
    public decimal Pret { get; set; }
}

public class ToDictionaryExample
{
    public static void Example()
    {
        List<Produs> listaProduse = new List<Produs>
        {
            new Produs { Id = 1, Nume = "Laptop", Pret = 3500 },
            new Produs { Id = 2, Nume = "Mouse", Pret = 100 },
            new Produs { Id = 3, Nume = "Tastatura", Pret = 250 }
        };
            
        //Creeaza un Dictionary<int, Produs>
        var dictionarProduse = listaProduse.ToDictionary(p => p.Id);
        
        Produs produsulDoi = dictionarProduse[2]; 
        
        Console.WriteLine($"[Scenariul 1] Produsul cu ID 2 este: {produsulDoi.Nume} - {produsulDoi.Pret} RON");

        
        //Creeaza un Dictionary<int, string>
        //Primul lambda este Cheia (p.Id), al doilea este Valoarea (p.Nume)
        var dictionarNume = listaProduse.ToDictionary(p => p.Id, p => p.Nume);

        string numeProdus = dictionarNume[1]; 
        
        Console.WriteLine($"[Scenariul 2] Numele produsului cu ID 1 este: {numeProdus}");
    }
    
    public static void ExampleQuerySyntax()
    {
        List<Produs> listaProduse = new List<Produs>
        {
            new Produs { Id = 1, Nume = "Laptop", Pret = 3500 },
            new Produs { Id = 2, Nume = "Mouse", Pret = 100 },
            new Produs { Id = 3, Nume = "Tastatura", Pret = 250 }
        };

        //Creeaza un Dictionary<int, Produs>
        var dictionarProduse = (from p in listaProduse select p)
            .ToDictionary(p => p.Id); 

        Produs produsulDoi = dictionarProduse[2];
        Console.WriteLine($"[Scenariul 1] Produs: {produsulDoi.Nume}");


        //Creeaza un Dictionary<int, string>
        var dictionarNume = (from p in listaProduse select p)
            .ToDictionary(p => p.Id, p => p.Nume);

        Console.WriteLine($"[Scenariul 2] Nume: {dictionarNume[1]}");
    }
}
