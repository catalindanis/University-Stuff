namespace ExempleCurs12;

public class AnyExample
{
    //Verifica daca un element in colectie satisface o conditie
    
    
    public static void Example()
    {
        int[] speeds = { 23, 12, 32, 54, 2 };
        bool result = speeds.Any(speed => speed > 50);
        Console.WriteLine("Is there a speed above 50 : {0}", result);
    }
    
}