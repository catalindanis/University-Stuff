namespace ExempleCurs12.Set3ExempleLinq;

public class Exercitiu3
{
    public static void Run()
    {
        OtherDuck[] OtherDucks =
        {
            new OtherDuck("OtherDuck1", 5L),
            new OtherDuck("OtherDuck5",3L),
            new OtherDuck("OtherDuck2",3L),
            new OtherDuck("OtherDuck3",4L),
            new OtherDuck("OtherDuck4",2L)
        };


        Child[] children =
        {
            new Child("Andrei", 3L),
            new Child("Malina", 4L),
            new Child("Ana", 2L)
        };
        

        // Sa se determine numele ratelor care trimit cadouri copiilor din lista, dar si
        // numele copiilor. Rezultatul va fi ordonat dupa numele copiilor, iar mai apoi dupa numele ratelor.


        // SINTAXA QUERY
        var join11 = from duck in OtherDucks
            join child in children on duck.ChildId equals child.Id
            orderby child.Name,duck.Name
            select new
            {
                duckName = duck.Name,
                childName = child.Name
            };

        var join12 = from duck in OtherDucks
            join child in children on duck.ChildId equals child.Id
            select new
            {
                duckName = duck.Name,
                childName = child.Name
            }into result
            orderby result.childName, result.duckName
            select result;

        // SINTAXA METHOD
        var join2 = OtherDucks.Join(
            children,
            duck => duck.ChildId,
            child => child.Id,
            (duck, child) => new
            {
                duckName = duck.Name,
                childName = child.Name
            })
            .OrderBy(x => x.childName)
            .ThenBy(x => x.duckName);


        foreach (var j in join2)
            Console.WriteLine(String.Format("Duck name: {0}, child name: {1}", j.duckName, j.childName));

    }
}
