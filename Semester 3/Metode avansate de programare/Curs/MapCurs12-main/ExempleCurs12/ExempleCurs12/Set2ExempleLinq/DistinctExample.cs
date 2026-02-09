namespace ExempleCurs12.Set2ExempleLinq;

public class DistinctExample
{
    public static void Example()
    {
        int[] numere = { 1, 2, 2, 3, 5, 6, 6, 6, 8, 9 };
        
        var numereDistincte = numere.Distinct();
        
        //var numereDistincte = (from nr in numere select nr).Distinct();
        
        Console.WriteLine("Lista de numere fara duplicate:" + string.Join(",", numereDistincte));
    }
}

