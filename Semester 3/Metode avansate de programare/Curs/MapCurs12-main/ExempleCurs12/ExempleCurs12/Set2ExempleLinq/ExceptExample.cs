namespace ExempleCurs12.Set2ExempleLinq;

public class ExceptExample
{
    public static void Example()
    {
        int[] numere1 = { 1, 2, 1, 1, 3, 3, 5, 6 };
        int[] numere2 = { 3, 4, 5 };
        //var rezultat = numere1.Except(numere2);
        
        var rezultat = (from n in numere1 select n).Except(numere2);
        
        Console.WriteLine("Lista de numere fara duplicate:" + string.Join(",", rezultat));
    }
}
