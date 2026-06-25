using System.ComponentModel;

namespace FestivalMuzicaCSharpWFA.UI;

partial class Home
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
        showsTable = new System.Windows.Forms.DataGridView();
        artistSearchField = new System.Windows.Forms.TextBox();
        datePickerField = new System.Windows.Forms.DateTimePicker();
        searchButton = new System.Windows.Forms.Button();
        clientsNameField = new System.Windows.Forms.TextBox();
        numberOfSeatsField = new System.Windows.Forms.TextBox();
        buyButton = new System.Windows.Forms.Button();
        viewAllTicketsButton = new System.Windows.Forms.Button();
        logoutButton = new System.Windows.Forms.Button();
        messageLabel = new System.Windows.Forms.Label();
        ((System.ComponentModel.ISupportInitialize)showsTable).BeginInit();
        SuspendLayout();
        // 
        // showsTable
        // 
        showsTable.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
        showsTable.Location = new System.Drawing.Point(10, 10);
        showsTable.Name = "showsTable";
        showsTable.Size = new System.Drawing.Size(780, 200);
        showsTable.TabIndex = 0;
        // 
        // artistSearchField
        // 
        artistSearchField.Location = new System.Drawing.Point(10, 220);
        artistSearchField.Name = "artistSearchField";
        artistSearchField.PlaceholderText = "Artist";
        artistSearchField.Size = new System.Drawing.Size(150, 23);
        artistSearchField.TabIndex = 1;
        // 
        // datePickerField
        // 
        datePickerField.Location = new System.Drawing.Point(170, 220);
        datePickerField.Name = "datePickerField";
        datePickerField.Size = new System.Drawing.Size(150, 23);
        datePickerField.TabIndex = 2;
        // 
        // searchButton
        // 
        searchButton.Location = new System.Drawing.Point(330, 220);
        searchButton.Name = "searchButton";
        searchButton.Size = new System.Drawing.Size(100, 23);
        searchButton.TabIndex = 3;
        searchButton.Text = "Search";
        searchButton.UseVisualStyleBackColor = true;
        // 
        // clientsNameField
        // 
        clientsNameField.Location = new System.Drawing.Point(10, 260);
        clientsNameField.Name = "clientsNameField";
        clientsNameField.PlaceholderText = "Client\'s name";
        clientsNameField.Size = new System.Drawing.Size(150, 23);
        clientsNameField.TabIndex = 4;
        // 
        // numberOfSeatsField
        // 
        numberOfSeatsField.Location = new System.Drawing.Point(170, 260);
        numberOfSeatsField.Name = "numberOfSeatsField";
        numberOfSeatsField.PlaceholderText = "Number of seats";
        numberOfSeatsField.Size = new System.Drawing.Size(150, 23);
        numberOfSeatsField.TabIndex = 5;
        // 
        // buyButton
        // 
        buyButton.Location = new System.Drawing.Point(330, 260);
        buyButton.Name = "buyButton";
        buyButton.Size = new System.Drawing.Size(100, 23);
        buyButton.TabIndex = 6;
        buyButton.Text = "Buy";
        buyButton.UseVisualStyleBackColor = true;
        // 
        // viewAllTicketsButton
        // 
        viewAllTicketsButton.Location = new System.Drawing.Point(440, 260);
        viewAllTicketsButton.Name = "viewAllTicketsButton";
        viewAllTicketsButton.Size = new System.Drawing.Size(120, 23);
        viewAllTicketsButton.TabIndex = 7;
        viewAllTicketsButton.Text = "View all tickets";
        viewAllTicketsButton.UseVisualStyleBackColor = true;
        // 
        // logoutButton
        // 
        logoutButton.Location = new System.Drawing.Point(10, 300);
        logoutButton.Name = "logoutButton";
        logoutButton.Size = new System.Drawing.Size(100, 23);
        logoutButton.TabIndex = 8;
        logoutButton.Text = "Logout";
        logoutButton.UseVisualStyleBackColor = true;
        // 
        // messageLabel
        // 
        messageLabel.ForeColor = System.Drawing.Color.Red;
        messageLabel.Location = new System.Drawing.Point(120, 300);
        messageLabel.Name = "messageLabel";
        messageLabel.Size = new System.Drawing.Size(440, 40);
        messageLabel.TabIndex = 9;
        messageLabel.TextAlign = System.Drawing.ContentAlignment.MiddleLeft;
        // 
        // Home
        // 
        AutoScaleDimensions = new System.Drawing.SizeF(7F, 15F);
        AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
        ClientSize = new System.Drawing.Size(800, 350);
        Controls.Add(showsTable);
        Controls.Add(artistSearchField);
        Controls.Add(datePickerField);
        Controls.Add(searchButton);
        Controls.Add(clientsNameField);
        Controls.Add(numberOfSeatsField);
        Controls.Add(buyButton);
        Controls.Add(viewAllTicketsButton);
        Controls.Add(logoutButton);
        Controls.Add(messageLabel);
        Text = "Home";
        ((System.ComponentModel.ISupportInitialize)showsTable).EndInit();
        ResumeLayout(false);
        PerformLayout();
    }

    private System.Windows.Forms.DataGridView showsTable;
    private System.Windows.Forms.TextBox artistSearchField;
    private System.Windows.Forms.DateTimePicker datePickerField;
    private System.Windows.Forms.Button searchButton;
    private System.Windows.Forms.TextBox clientsNameField;
    private System.Windows.Forms.TextBox numberOfSeatsField;
    private System.Windows.Forms.Button buyButton;
    private System.Windows.Forms.Button viewAllTicketsButton;
    private System.Windows.Forms.Button logoutButton;
    private System.Windows.Forms.Label messageLabel;

    #endregion
}