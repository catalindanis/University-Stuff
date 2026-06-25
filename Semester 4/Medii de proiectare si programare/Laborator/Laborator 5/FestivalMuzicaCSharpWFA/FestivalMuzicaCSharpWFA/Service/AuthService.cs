using System.Configuration;
using System.Security.Cryptography;
using System.Text;
using FestivalMuzicaCSharp.Domain;
using FestivalMuzicaCSharp.Repository;

namespace FestivalMuzicaCSharpWFA.Service;

public class AuthService
{
    private static readonly AuthService _instance = new AuthService();
    public static AuthService Instance => _instance;
    private readonly IUsersRepository _repository;
    private readonly string _encryptionAESKey;

    private AuthService()
    {
        var props = new Dictionary<string, string>();
        var connStr = ConfigurationManager.ConnectionStrings["musicFestivalDB"]?.ConnectionString;
        if (connStr == null)
            throw new Exception("Cannot find connection string 'musicFestivalDB' in app.config");
        props["ConnectionString"] = connStr;

        // Load encryption key from config (add this to app.config if not present)
        _encryptionAESKey = ConfigurationManager.AppSettings["encryptionAESKey"];
        if (string.IsNullOrEmpty(_encryptionAESKey))
            throw new Exception("Missing encryptionAESKey in app.config <appSettings>");

        _repository = new UsersDbRepository(props);
    }

    public User Login(string email, string password)
    {
        string encryptedPassword = Encrypt(password, _encryptionAESKey);
        
        var user = _repository.FindByEmailAndPassword(email, encryptedPassword);
        if (user != null)
            return user;
        
        throw new Exception("Invalid credentials");
    }

    public void Register(string email, string password)
    {
        string encryptedPassword = Encrypt(password, _encryptionAESKey);
        
        var existingUser = _repository.FindByEmail(email);
        if (existingUser != null)
            throw new Exception("User already exists with this email");
        
        _repository.Save(new User(0, email, encryptedPassword));
    }

    public string Encrypt(string data, string key)
    {
        try
        {
            using (Aes aesAlg = Aes.Create())
            {
                aesAlg.Key = Encoding.UTF8.GetBytes(key.PadRight(32).Substring(0, 32));
                aesAlg.Mode = CipherMode.ECB;
                aesAlg.Padding = PaddingMode.PKCS7;
                using (ICryptoTransform encryptor = aesAlg.CreateEncryptor())
                {
                    byte[] inputBytes = Encoding.UTF8.GetBytes(data);
                    byte[] encrypted = encryptor.TransformFinalBlock(inputBytes, 0, inputBytes.Length);
                    return Convert.ToBase64String(encrypted);
                }
            }
        }
        catch
        {
            throw new Exception("Password encryption failed!");
        }
    }
}