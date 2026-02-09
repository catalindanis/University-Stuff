namespace ExempleCurs12.domain;

public class Duck
{
    private int Id { get; set; }
    private string Name {get; set; }
    private double Speed {get; set; }
    private double Resistance {get; set; }

    public Duck(int id, string name, double speed, double resistance)
    {
        this.Id = id;
        this.Name = name;
        this.Speed = speed;
        this.Resistance = resistance;
    }

    public override string ToString()
    {
        return $"{Id} - {Name} - {Speed} - {Resistance}" ;
    }
}