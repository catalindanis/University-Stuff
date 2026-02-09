namespace ExempleCurs12.Set2ExempleLinq;

public class GroupByExample
{
    public static void Example()
    {
        List<Produs> listaProduse = new List<Produs>
        {
            new Produs { Id = 1, Nume = "Laptop", Pret = 3500 },
            new Produs { Id = 2, Nume = "Mouse", Pret = 100 },
            new Produs { Id = 3, Nume = "Tastatura", Pret = 250 },
            new Produs { Id = 4, Nume = "Monitor", Pret = 3500 },   
            new Produs { Id = 5, Nume = "MousePad", Pret = 100 },  
            new Produs { Id = 6, Nume = "Casti", Pret = 250 }      
        };
        
        //Sintaxa METHOD
        //var grupuri = listaProduse.GroupBy(p => p.Pret);
        
        //Sintaxa QUERY
        var grupuri = from p in listaProduse group p by p.Pret;
        
        foreach (var grup in grupuri)
        {
            // grup.Key este prețul comun (ex: 3500, 100, etc.)
            Console.WriteLine($"Produse cu prețul: {grup.Key} RON");

            foreach (var produs in grup)
            {
                Console.WriteLine($"   - {produs.Nume} (ID: {produs.Id})");
            }
        }
    }
}