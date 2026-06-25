using FestivalMuzicaCSharp.Domain;

namespace FestivalMuzicaCSharp.Repository;

public interface IRepository<TId, T> where T : IEntity<TId>
{
    int Size();
    void Save(T entity);
    void Delete(TId id);
    void Update(TId id, T entity);
    T? FindOne(TId id);
    IEnumerable<T> FindAll();
}