using FestivalMuzicaCSharp.Domain;

namespace FestivalMuzicaCSharp.Repository;

public interface IUsersRepository : IRepository<long, User>
{
    User? FindByEmailAndPassword(String email, String password);
    User? FindByEmail(String email);
}