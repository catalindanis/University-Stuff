using Proiect.Domain;

namespace Proiect.Repository;

// Extinde contractul CRUD cu suport pentru filtrare.
public interface IFilterableRepository<TId, TEntity, TFilter> : IRepository<TId, TEntity> where TEntity : IEntity<TId>
{
    // Input: TFilter filter.
    // Return: IEnumerable<TEntity>.
    // Returneaza entitatile care respecta filtrul.
    // Criteriile exacte depind de implementare.
    IEnumerable<TEntity> FindAll(TFilter filter);
}