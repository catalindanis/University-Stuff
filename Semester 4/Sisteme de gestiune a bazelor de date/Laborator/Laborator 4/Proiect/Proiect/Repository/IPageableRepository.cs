using Proiect.Domain;
using Proiect.Utils;

namespace Proiect.Repository;

public interface IPageableRepository<TId, TEntity> : IRepository<TId, TEntity> where TEntity : IEntity<TId>
{
    // Input: int pageNumber (1-based), int pageSize.
    // Return: PaginatedResponse<TEntity>.
    // Returneaza o pagina de entitati cu metadate de paginare.
    // Folosit pentru paginare offset-based in UI.
    PaginatedResponse<TEntity> FindAllPaginated(int pageNumber, int pageSize);
    
    // Input: long? lastId (null for first page), int pageSize.
    // Return: List<Department>.
    // Simpler cursor approach - just fetch after the last ID.
    PaginatedResponse<Department> FindAllPaginatedByCursor(long? lastId, int pageSize);
}