namespace ExempleCurs12;

public class AggregateExample
{
    //Aplica o operatie pentru fiecare element al colectiei, tinand resultatul in continuare

    public static int SumaCifre(int a)
    {
        int x = 0;
        while (a != 0)
        {
            x = x + a % 10;
            a /= 10;
        }
        return x;
    }

    public static void Example()
    {
        string[] duckNames = { "Petra", "Rudky", "Rata", "Marghiola", "Jim"};
        string longestName = duckNames.Aggregate((longest, next) => longest.Length > next.Length ? longest : next);
        Console.WriteLine("Longest Name: {0}", longestName);

        
        int[] numbers = { 476, 523, 432, 326 };
        int nr = numbers.Aggregate(0,(result, next) => result + SumaCifre(next));
        Console.WriteLine("Suma tuturor cifrelor: {0}", nr);
        
    }
    
}