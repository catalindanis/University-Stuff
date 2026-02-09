namespace ExempleCurs12;

public class SelectManyExample
{
    //combina 2 colectii intr-o singura colectie, asemanator cu cross join din sql
    
    public static void  Example()
    {
        string[] fruits = { "Grape", "Orange", "Apple" };
        int[] amounts = { 1, 2, 3 };
        var result = fruits.SelectMany(f => amounts, (f, a) => new
        {
            Fruit = f,
            Amount = a
        });
        Console.WriteLine("Selecting all values from each array, and mixing them:");
        foreach (var o in result) 
            Console.WriteLine(o.Fruit + ", " + o.Amount);
    }
}