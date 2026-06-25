using System.ComponentModel;

namespace FestivalMuzicaCSharpWFA.UI;

partial class Auth
{
    /// <summary>
    /// Required designer variable.
    /// </summary>
    private IContainer components = null;

    /// <summary>
    /// Clean up any resources being used.
    /// </summary>
    /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
    protected override void Dispose(bool disposing)
    {
        if (disposing && (components != null))
        {
            components.Dispose();
        }

        base.Dispose(disposing);
    }

    #region Windows Form Designer generated code

    /// <summary>
    /// Required method for Designer support - do not modify
    /// the contents of this method with the code editor.
    /// </summary>
    private void InitializeComponent()
    {
        emailField = new System.Windows.Forms.TextBox();
        passwordField = new System.Windows.Forms.TextBox();
        loginButton = new System.Windows.Forms.Button();
        registerButton = new System.Windows.Forms.Button();
        messageLabel = new System.Windows.Forms.Label();
        SuspendLayout();
        // 
        // emailField
        // 
        emailField.Location = new System.Drawing.Point(30, 30);
        emailField.Name = "emailField";
        emailField.PlaceholderText = "Email";
        emailField.Size = new System.Drawing.Size(200, 23);
        emailField.TabIndex = 0;
        // 
        // passwordField
        // 
        passwordField.Location = new System.Drawing.Point(30, 70);
        passwordField.Name = "passwordField";
        passwordField.PlaceholderText = "Password";
        passwordField.Size = new System.Drawing.Size(200, 23);
        passwordField.TabIndex = 1;
        passwordField.UseSystemPasswordChar = true;
        // 
        // loginButton
        // 
        loginButton.Location = new System.Drawing.Point(30, 110);
        loginButton.Name = "loginButton";
        loginButton.Size = new System.Drawing.Size(200, 30);
        loginButton.TabIndex = 2;
        loginButton.Text = "Login";
        loginButton.UseVisualStyleBackColor = true;
        // 
        // registerButton
        // 
        registerButton.Location = new System.Drawing.Point(30, 150);
        registerButton.Name = "registerButton";
        registerButton.Size = new System.Drawing.Size(200, 30);
        registerButton.TabIndex = 3;
        registerButton.Text = "Register";
        registerButton.UseVisualStyleBackColor = true;
        // 
        // messageLabel
        // 
        messageLabel.ForeColor = System.Drawing.Color.Red;
        messageLabel.Location = new System.Drawing.Point(30, 190);
        messageLabel.Name = "messageLabel";
        messageLabel.Size = new System.Drawing.Size(200, 40);
        messageLabel.TabIndex = 4;
        messageLabel.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
        // 
        // Auth
        // 
        AutoScaleDimensions = new System.Drawing.SizeF(7F, 15F);
        AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
        ClientSize = new System.Drawing.Size(260, 260);
        Controls.Add(emailField);
        Controls.Add(passwordField);
        Controls.Add(loginButton);
        Controls.Add(registerButton);
        Controls.Add(messageLabel);
        Text = "Auth";
        Load += Auth_Load;
        ResumeLayout(false);
        PerformLayout();
    }

    #endregion

    private System.Windows.Forms.TextBox emailField;
    private System.Windows.Forms.TextBox passwordField;
    private System.Windows.Forms.Button loginButton;
    private System.Windows.Forms.Button registerButton;
    private System.Windows.Forms.Label messageLabel;
}