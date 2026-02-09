namespace ExempleCurs12;

public class ConcatExample
{
    //concateneaza doua collectii
    
    public static void Example()
    {
        int[] numbers1 = { 1, 2, 3 };
        int[] numbers2 = { 4, 5, 6 };
        var result = numbers1.Concat(numbers2);
        foreach (var i in result)
        {
            Console.WriteLine(i);
        }
    }
}