namespace ExempleCurs12;

public class Exercitiu1
{
    public static void Run()
    {
        
        //se da o lista de nume de rate, si doua liste de viteze
        
        string[] duckNames = { "Petra", "Rudky", "Rata", "Marghiola", "Jim"};
        int[] bigSpeeds = { 100, 200, 160 };
        int[] slowSpeeds = { 2, 6, 5 };
        
        //combinati cele doua liste astfel incat fiecare rata care incepe cu R sa aiba fiecare din vitezele mari, iar celelate sa aiba vitezele mici
        //hint: folositi Where pentru a verfica daca numele incepe cu R, SelectMany pentru combinari si Concat pentru a concatena cele 2 colectii la final
        //TODO 
        
        //verficati daca Marghiola are o viteza peste 10 folosind lista creata mai sus
        //hint: folositi any
        //TODO 
        
        //calculati perechea rata - viteza cu viteza cea mai mare, daca sunt mai multe de aceeasi viteza, atunci returnati rata cu cel mai lung nume
        //hint: folositi aggregate
        //TODO
    }

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public static void Rezolvare()
    {
        //se da o lista de nume de rate, si doua liste de viteze
        
        string[] duckNames = { "Petra", "Rudky", "Rata", "Marghiola", "Jim"};
        int[] bigSpeeds = { 100, 200, 160 };
        int[] slowSpeeds = { 2, 6, 5 };
        
        //combinati cele doua liste astfel incat fiecare rata care incepe cu R sa aiba fiecare din vitezele mari, iar celelate sa aiba vitezele mici
        //hint: folositi Where pentru a verfica daca numele incepe cu R, SelectMany pentru combinari si Concat pentru a concatena cele 2 colectii la final
        
        var fastDucks = duckNames.Where(name => name.StartsWith("R"))
            .SelectMany(duckName => bigSpeeds, (duckName, speed) => new { DuckName = duckName, Speed = speed });
        
        var slowDucks = duckNames.Where(name => !name.StartsWith("R"))
            .SelectMany(duckName => slowSpeeds, (duckName, speed) => new { DuckName = duckName, Speed = speed });
        
        var duckSpeeds = fastDucks.Concat(slowDucks);
        
        
        foreach (var x1 in duckSpeeds)
        {
            Console.WriteLine($"{x1.DuckName} {x1.Speed}");
        }
        
        //verficati daca Marghiola are o viteza peste 10 folosind lista creata mai sus

        bool ok = duckSpeeds.Any(var => var.DuckName == "Marghiola" && var.Speed > 10);

        Console.WriteLine(ok);
        
        //calculati perechea rata - viteza cu viteza cea mai mare, daca sunt mai multe de aceeasi viteza, atunci returnati rata cu cel mai lung nume

        var result = duckSpeeds.Aggregate((total, next) =>
        {
            if (total.Speed == next.Speed)
                return total.DuckName.Length > next.DuckName.Length ? total : next;
            return total.Speed > next.Speed ? total : next;
        });
        Console.WriteLine("{0} -- {1}" , result.DuckName, result.Speed);
    }
}