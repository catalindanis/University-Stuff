namespace ExempluGeneralLINQ.Data;
using ExempluGeneralLINQ.Domain;

public static class MockData
{
    public static List<Scrisoare> GetScrisori()
    {
        return new List<Scrisoare>
        {
            new Scrisoare { NumeCopil = "Andrei", Oras = "Cluj", EsteCuminte = true,
                Dorinte = new List<string> { "Trenuleț", "Ciocolată" } },

            new Scrisoare { NumeCopil = "Maria",  Oras = "București", EsteCuminte = true,
                Dorinte = new List<string> { "Păpușă" } },

            new Scrisoare { NumeCopil = "Gigel",  Oras = "Cluj", EsteCuminte = false, 
                Dorinte = new List<string> { "Tobă", "Lego" } },

            new Scrisoare { NumeCopil = "Elena",  Oras = "Iași", EsteCuminte = true,
                Dorinte = new List<string> { "Lego", "Ciocolată", "Păpușă" } } 
        };
    }

    public static List<JucarieInventar> GetInventar()
    {
        return new List<JucarieInventar>
        {
            new JucarieInventar { Nume = "Trenuleț", GreutateKG = 1.5, PretProductie = 50 },
            new JucarieInventar { Nume = "Păpușă",   GreutateKG = 0.5, PretProductie = 30 },
            new JucarieInventar { Nume = "Lego",     GreutateKG = 1.0, PretProductie = 40 },
            new JucarieInventar { Nume = "Tobă",     GreutateKG = 2.0, PretProductie = 45 },
            new JucarieInventar { Nume = "Ciocolată",GreutateKG = 0.2, PretProductie = 10 }
        };
    }
}