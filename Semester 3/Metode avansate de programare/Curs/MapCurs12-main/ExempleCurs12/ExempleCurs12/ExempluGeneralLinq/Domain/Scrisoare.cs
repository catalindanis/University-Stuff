namespace ExempluGeneralLINQ.Domain;

public class Scrisoare
{
    public string NumeCopil { get; set; }
    public string Oras { get; set; }
    public List<string> Dorinte { get; set; } 
    public bool EsteCuminte { get; set; }
    
    public override string ToString()
    {

        return $"{NumeCopil} din {Oras} ({EsteCuminte}) - Doreste: {string.Join(", ", Dorinte)}";
    }
}