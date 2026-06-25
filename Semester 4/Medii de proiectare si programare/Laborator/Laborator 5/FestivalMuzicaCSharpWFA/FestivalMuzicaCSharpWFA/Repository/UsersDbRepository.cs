using FestivalMuzicaCSharp.Domain;
using FestivalMuzicaCSharp.Utils;
using log4net;

namespace FestivalMuzicaCSharp.Repository;

public class UsersDbRepository : IUsersRepository
{
    private const string TableName = "users";
    private readonly ILog Log = LogManager.GetLogger(typeof(UsersDbRepository));
    private IDictionary<string, string> props;

    public UsersDbRepository(IDictionary<string, string> props)
    {
        this.props = props;
    }

    public int Size()
    {
        Log.InfoFormat("Entering Size");
        var con = DBUtils.getConnection(props);
        var result = 0;

        using (var comm = con.CreateCommand())
        {
            comm.CommandText = $"select count(*) from {TableName}";
            
            Log.InfoFormat("Executing: {0}", comm.CommandText);

            result = Convert.ToInt32(comm.ExecuteScalar());
        }
        
        Log.InfoFormat("Exiting Size with value {0}", result);
        return result;
    }

    public void Save(User entity)
    {
        Log.InfoFormat("Entering Save with value {0}", entity);
        var con = DBUtils.getConnection(props);

        using (var comm = con.CreateCommand())
        {
            comm.CommandText = $"insert into {TableName}(email, password) values(@email, @password)";

            var paramEmail = comm.CreateParameter();
            paramEmail.ParameterName = "@email";
            paramEmail.Value = entity.Email;
            comm.Parameters.Add(paramEmail);
            
            var paramPassword = comm.CreateParameter();
            paramPassword.ParameterName = "@password";
            paramPassword.Value = entity.Password;
            comm.Parameters.Add(paramPassword);

            Log.InfoFormat("Executing: {0}", comm.CommandText);

            comm.ExecuteNonQuery();
        }
        
        Log.InfoFormat("Exiting Save");
    }

    public void Delete(long id)
    {
        Log.InfoFormat("Entering Delete with value {0}", id);
        var con = DBUtils.getConnection(props);

        using (var comm = con.CreateCommand())
        {
            comm.CommandText = $"delete from {TableName} where id = @id";

            var paramId = comm.CreateParameter();
            paramId.ParameterName = "@id";
            paramId.Value = id;
            comm.Parameters.Add(paramId);

            Log.InfoFormat("Executing: {0}", comm.CommandText);

            comm.ExecuteNonQuery();
        }
        
        Log.InfoFormat("Exiting Delete");
    }

    public void Update(long id, User entity)
    {
        Log.InfoFormat("Entering Update with value {0}", entity);
        var con = DBUtils.getConnection(props);

        using (var comm = con.CreateCommand())
        {
            comm.CommandText = $"update {TableName} set email = @email, password = @password where id = @id";

            var paramEmail = comm.CreateParameter();
            paramEmail.ParameterName = "@email";
            paramEmail.Value = entity.Email;
            comm.Parameters.Add(paramEmail);
            
            var paramPassword = comm.CreateParameter();
            paramPassword.ParameterName = "@password";
            paramPassword.Value = entity.Password;
            comm.Parameters.Add(paramPassword);
            
            var paramId = comm.CreateParameter();
            paramId.ParameterName = "@id";
            paramId.Value = id;
            comm.Parameters.Add(paramId);

            Log.InfoFormat("Executing: {0}", comm.CommandText);

            comm.ExecuteNonQuery();
        }
        
        Log.InfoFormat("Exiting Update");
    }

    public User? FindOne(long id)
    {
        Log.InfoFormat("Entering FindOne with value {0}", id);
        var con = DBUtils.getConnection(props);
        User result = null;

        using (var comm = con.CreateCommand())
        {
            comm.CommandText = $"select * from {TableName} where id = @id";
            var paramId = comm.CreateParameter();
            paramId.ParameterName = "@id";
            paramId.Value = id;
            comm.Parameters.Add(paramId);
            
            Log.InfoFormat("Executing: {0}", comm.CommandText);

            using (var dataR = comm.ExecuteReader())
            {
                if (dataR.Read())
                {
                    var userId = dataR.GetInt32(0);
                    var email = dataR.GetString(1);
                    var password = dataR.GetString(2);
                    
                    result = new User(userId, email, password);
                }
            }
        }
        
        Log.InfoFormat("Exiting FindOne with value {0}", result);
        return result;
    }

    public IEnumerable<User> FindAll()
    {
        Log.InfoFormat("Entering FindAll");
        var con = DBUtils.getConnection(props);
        var result = new List<User>();

        using (var comm = con.CreateCommand())
        {
            comm.CommandText = $"select * from {TableName}";
            
            Log.InfoFormat("Executing: {0}", comm.CommandText);

            using (var dataR = comm.ExecuteReader())
            {
                while (dataR.Read())
                {
                    var userId = dataR.GetInt32(0);
                    var email = dataR.GetString(1);
                    var password = dataR.GetString(2);
                    
                    result.Add(new User(userId, email, password));
                }
            }
        }
        
        Log.InfoFormat("Exiting FindAll with value {0}", result);
        return result;
    }

    public User? FindByEmailAndPassword(string email, string password)
    {
        Log.InfoFormat("Entering FindByEmailAndPassword with value email={0}, password={1}", email, password);
        var con = DBUtils.getConnection(props);
        User result = null;
        
        using (var comm = con.CreateCommand())
        {
            comm.CommandText = $"select * from {TableName} where email = @email and password = @password";
            
            var paramEmail = comm.CreateParameter();
            paramEmail.ParameterName = "@email";
            paramEmail.Value = email;
            comm.Parameters.Add(paramEmail);
            
            var paramPassword = comm.CreateParameter();
            paramPassword.ParameterName = "@password";
            paramPassword.Value = password;
            comm.Parameters.Add(paramPassword);
            
            Log.InfoFormat("Executing: {0}", comm.CommandText);

            using (var dataR = comm.ExecuteReader())
            {
                if (dataR.Read())
                {
                    var userId = dataR.GetInt32(0);
                    var foundEmail = dataR.GetString(1);
                    var foundPassword = dataR.GetString(2);
                    
                    result = new User(userId, foundEmail, foundPassword);
                }
            }
        }
        
        Log.InfoFormat("Exiting FindByEmailAndPassword with value {0}", result);
        return result; 
    }

    public User? FindByEmail(string email)
    {
        Log.InfoFormat("Entering FindByEmail with value email={0}", email);
        var con = DBUtils.getConnection(props);
        User result = null;
        
        using (var comm = con.CreateCommand())
        {
            comm.CommandText = $"select * from {TableName} where email = @email";
            
            var paramEmail = comm.CreateParameter();
            paramEmail.ParameterName = "@email";
            paramEmail.Value = email;
            comm.Parameters.Add(paramEmail);
            
            Log.InfoFormat("Executing: {0}", comm.CommandText);

            using (var dataR = comm.ExecuteReader())
            {
                if (dataR.Read())
                {
                    var userId = dataR.GetInt32(0);
                    var foundEmail = dataR.GetString(1);
                    var foundPassword = dataR.GetString(2);
                    
                    result = new User(userId, foundEmail, foundPassword);
                }
            }
        }
        
        Log.InfoFormat("Exiting FindByEmail with value {0}", result);
        return result; 
    }
}