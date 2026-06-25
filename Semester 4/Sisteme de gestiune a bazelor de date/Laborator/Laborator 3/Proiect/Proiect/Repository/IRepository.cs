using Proiect.Domain;

namespace Proiect.Repository;

// Contract generic pentru operatiile CRUD de baza.
public interface IRepository<TId, TEntity> where TEntity : IEntity<TId>
{
    // Input: fara parametri.
    // Return: int.
    // Intoarce numarul total de entitati.
    // Este folosit pentru statistici simple.
    int Size();

    // Input: TEntity entity.
    // Return: void.
    // Salveaza o entitate noua.
    // Implementarea decide mecanismul de persistenta.
    void Save(TEntity entity);

    // Input: TId id.
    // Return: void.
    // Sterge entitatea dupa id.
    // Implementarea poate arunca exceptie la esec.
    void Delete(TId id);

    // Input: TId id, TEntity entity.
    // Return: void.
    // Actualizeaza entitatea identificata prin id.
    // Valorile noi vin din obiectul entity.
    void Update(TId id, TEntity entity);

    // Input: TId id.
    // Return: TEntity.
    // Cauta si returneaza entitatea dupa id.
    // De regula arunca exceptie daca lipseste.
    TEntity FindById(TId id);

    // Input: fara parametri.
    // Return: IEnumerable<TEntity>.
    // Returneaza toate entitatile disponibile.
    // Este baza pentru afisari in lista/grila.
    IEnumerable<TEntity> FindAll();
}