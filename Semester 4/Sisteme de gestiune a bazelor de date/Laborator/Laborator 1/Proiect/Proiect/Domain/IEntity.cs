namespace Proiect.Domain;

public interface IEntity<T>
{
    public T Id { get; set; }
}