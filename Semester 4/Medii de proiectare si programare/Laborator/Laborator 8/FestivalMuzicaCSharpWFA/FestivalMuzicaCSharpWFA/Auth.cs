using System.Text.RegularExpressions;
using FestivalMuzicaClient;
using FestivalMuzicaCSharpWFA.Utils;

namespace FestivalMuzicaCSharpWFA.UI;

public partial class Auth : Form
{
    private readonly Controller _controller;
    public Auth(Controller controller)
    {
        _controller = controller;
        InitializeComponent();
        loginButton.Click += HandleLoginClick;
        registerButton.Click += HandleRegisterClick;
        messageLabel.Text = string.Empty;
    }

    private void HandleLoginClick(object sender, EventArgs e)
    {
        try
        {
            ValidateInputs();
            var user = _controller.Login(emailField.Text, passwordField.Text);
            ResetFields();
            var props = new Dictionary<string, object> { { "user", user } };
            Navigator.NavigateTo(new Home(_controller), "Dashboard", true, props);
        }
        catch (Exception ex)
        {
            messageLabel.Text = ex.Message;
        }
    }

    private void HandleRegisterClick(object sender, EventArgs e)
    {
        try
        {
            ValidateInputs();
            _controller.Register(emailField.Text, passwordField.Text);
            ResetFields();
            messageLabel.Text = "Account created! Please log in";
        }
        catch (Exception ex)
        {
            messageLabel.Text = ex.Message;
        }
    }

    private void ResetFields()
    {
        emailField.Text = string.Empty;
        passwordField.Text = string.Empty;
        messageLabel.Text = string.Empty;
    }

    private void ValidateInputs()
    {
        string email = emailField.Text;
        string password = passwordField.Text;
        string errorMessage = string.Empty;

        if (string.IsNullOrWhiteSpace(email))
            errorMessage += "Email field cannot be empty!\n";
        else if (!Regex.IsMatch(email, "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"))
            errorMessage += "Email format is invalid!\n";

        if (string.IsNullOrWhiteSpace(password))
            errorMessage += "Password field cannot be empty\n";

        if (!string.IsNullOrEmpty(errorMessage))
            throw new Exception(errorMessage);
    }

    private void Auth_Load(object sender, EventArgs e)
    {
        
    }
}