using System.ComponentModel;

namespace Proiect;

partial class EmployeesForm
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
        tableLayoutPanel1 = new System.Windows.Forms.TableLayoutPanel();
        EmployeesGridView = new System.Windows.Forms.DataGridView();
        tableLayoutPanel2 = new System.Windows.Forms.TableLayoutPanel();
        FirstNameTextBox = new System.Windows.Forms.TextBox();
        LastNameTextBox = new System.Windows.Forms.TextBox();
        EmailTextBox = new System.Windows.Forms.TextBox();
        AddEmployeeBtn = new System.Windows.Forms.Button();
        deleteEmployeeBtn = new System.Windows.Forms.Button();
        updateEmployeeBtn = new System.Windows.Forms.Button();
        DepartmentComboBox = new System.Windows.Forms.ComboBox();
        refreshDataBtn = new System.Windows.Forms.Button();
        tableLayoutPanel3 = new System.Windows.Forms.TableLayoutPanel();
        FirstNameFilterTextBox = new System.Windows.Forms.TextBox();
        LastNameFilterTextBox = new System.Windows.Forms.TextBox();
        EmailFilterTextBox = new System.Windows.Forms.TextBox();
        DepartmentFilterComboBox = new System.Windows.Forms.ComboBox();
        FilterBtn = new System.Windows.Forms.Button();
        ResetFilterBtn = new System.Windows.Forms.Button();
        tableLayoutPanel1.SuspendLayout();
        ((System.ComponentModel.ISupportInitialize)EmployeesGridView).BeginInit();
        tableLayoutPanel2.SuspendLayout();
        tableLayoutPanel3.SuspendLayout();
        SuspendLayout();
        // 
        // tableLayoutPanel1
        // 
        tableLayoutPanel1.ColumnCount = 1;
        tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 100F));
        tableLayoutPanel1.Controls.Add(EmployeesGridView, 0, 0);
        tableLayoutPanel1.Controls.Add(tableLayoutPanel2, 0, 1);
        tableLayoutPanel1.Location = new System.Drawing.Point(0, 3);
        tableLayoutPanel1.Name = "tableLayoutPanel1";
        tableLayoutPanel1.RowCount = 2;
        tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 80F));
        tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 20F));
        tableLayoutPanel1.Size = new System.Drawing.Size(806, 450);
        tableLayoutPanel1.TabIndex = 0;
        // 
        // EmployeesGridView
        // 
        EmployeesGridView.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
        EmployeesGridView.Location = new System.Drawing.Point(3, 3);
        EmployeesGridView.Name = "EmployeesGridView";
        EmployeesGridView.RowHeadersWidth = 51;
        EmployeesGridView.Size = new System.Drawing.Size(795, 354);
        EmployeesGridView.TabIndex = 0;
        EmployeesGridView.Text = "dataGridView1";
        // 
        // tableLayoutPanel2
        // 
        tableLayoutPanel2.ColumnCount = 4;
        tableLayoutPanel2.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 25F));
        tableLayoutPanel2.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 25F));
        tableLayoutPanel2.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 25F));
        tableLayoutPanel2.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 25F));
        tableLayoutPanel2.Controls.Add(FirstNameTextBox, 0, 0);
        tableLayoutPanel2.Controls.Add(LastNameTextBox, 1, 0);
        tableLayoutPanel2.Controls.Add(EmailTextBox, 2, 0);
        tableLayoutPanel2.Controls.Add(AddEmployeeBtn, 0, 1);
        tableLayoutPanel2.Controls.Add(deleteEmployeeBtn, 1, 1);
        tableLayoutPanel2.Controls.Add(updateEmployeeBtn, 2, 1);
        tableLayoutPanel2.Controls.Add(DepartmentComboBox, 3, 0);
        tableLayoutPanel2.Controls.Add(refreshDataBtn, 3, 1);
        tableLayoutPanel2.Location = new System.Drawing.Point(3, 363);
        tableLayoutPanel2.Name = "tableLayoutPanel2";
        tableLayoutPanel2.RowCount = 2;
        tableLayoutPanel2.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 50F));
        tableLayoutPanel2.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 50F));
        tableLayoutPanel2.Size = new System.Drawing.Size(800, 84);
        tableLayoutPanel2.TabIndex = 1;
        // 
        // FirstNameTextBox
        // 
        FirstNameTextBox.Location = new System.Drawing.Point(3, 3);
        FirstNameTextBox.Name = "FirstNameTextBox";
        FirstNameTextBox.PlaceholderText = "First name";
        FirstNameTextBox.Size = new System.Drawing.Size(194, 27);
        FirstNameTextBox.TabIndex = 1;
        // 
        // LastNameTextBox
        // 
        LastNameTextBox.Location = new System.Drawing.Point(203, 3);
        LastNameTextBox.Name = "LastNameTextBox";
        LastNameTextBox.PlaceholderText = "Last name";
        LastNameTextBox.Size = new System.Drawing.Size(194, 27);
        LastNameTextBox.TabIndex = 2;
        // 
        // EmailTextBox
        // 
        EmailTextBox.Location = new System.Drawing.Point(403, 3);
        EmailTextBox.Name = "EmailTextBox";
        EmailTextBox.PlaceholderText = "Email";
        EmailTextBox.Size = new System.Drawing.Size(194, 27);
        EmailTextBox.TabIndex = 3;
        // 
        // AddEmployeeBtn
        // 
        AddEmployeeBtn.Location = new System.Drawing.Point(3, 45);
        AddEmployeeBtn.Name = "AddEmployeeBtn";
        AddEmployeeBtn.Size = new System.Drawing.Size(194, 36);
        AddEmployeeBtn.TabIndex = 5;
        AddEmployeeBtn.Text = "Add";
        AddEmployeeBtn.UseVisualStyleBackColor = true;
        AddEmployeeBtn.Click += AddEmployeeBtn_Click;
        // 
        // deleteEmployeeBtn
        // 
        deleteEmployeeBtn.Location = new System.Drawing.Point(203, 45);
        deleteEmployeeBtn.Name = "deleteEmployeeBtn";
        deleteEmployeeBtn.Size = new System.Drawing.Size(194, 36);
        deleteEmployeeBtn.TabIndex = 6;
        deleteEmployeeBtn.Text = "Delete";
        deleteEmployeeBtn.UseVisualStyleBackColor = true;
        deleteEmployeeBtn.Click += deleteEmployeeBtn_Click;
        // 
        // updateEmployeeBtn
        // 
        updateEmployeeBtn.Location = new System.Drawing.Point(403, 45);
        updateEmployeeBtn.Name = "updateEmployeeBtn";
        updateEmployeeBtn.Size = new System.Drawing.Size(194, 36);
        updateEmployeeBtn.TabIndex = 7;
        updateEmployeeBtn.Text = "Update";
        updateEmployeeBtn.UseVisualStyleBackColor = true;
        updateEmployeeBtn.Click += updateEmployeeBtn_Click;
        // 
        // DepartmentComboBox
        // 
        DepartmentComboBox.ForeColor = System.Drawing.SystemColors.ControlText;
        DepartmentComboBox.FormattingEnabled = true;
        DepartmentComboBox.Location = new System.Drawing.Point(603, 3);
        DepartmentComboBox.Name = "DepartmentComboBox";
        DepartmentComboBox.Size = new System.Drawing.Size(194, 28);
        DepartmentComboBox.TabIndex = 4;
        DepartmentComboBox.Text = "Department";
        DepartmentComboBox.SelectedIndexChanged += DepartmentComboBox_SelectedIndexChanged;
        // 
        // refreshDataBtn
        // 
        refreshDataBtn.Location = new System.Drawing.Point(603, 45);
        refreshDataBtn.Name = "refreshDataBtn";
        refreshDataBtn.Size = new System.Drawing.Size(194, 36);
        refreshDataBtn.TabIndex = 8;
        refreshDataBtn.Text = "Refresh";
        refreshDataBtn.UseVisualStyleBackColor = true;
        refreshDataBtn.Click += refreshDataBtn_Click;
        // 
        // tableLayoutPanel3
        // 
        tableLayoutPanel3.ColumnCount = 4;
        tableLayoutPanel3.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 25F));
        tableLayoutPanel3.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 25F));
        tableLayoutPanel3.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 25.660378F));
        tableLayoutPanel3.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 24.528301F));
        tableLayoutPanel3.Controls.Add(FirstNameFilterTextBox, 0, 0);
        tableLayoutPanel3.Controls.Add(LastNameFilterTextBox, 1, 0);
        tableLayoutPanel3.Controls.Add(EmailFilterTextBox, 2, 0);
        tableLayoutPanel3.Controls.Add(DepartmentFilterComboBox, 3, 0);
        tableLayoutPanel3.Controls.Add(FilterBtn, 0, 1);
        tableLayoutPanel3.Controls.Add(ResetFilterBtn, 1, 1);
        tableLayoutPanel3.Location = new System.Drawing.Point(3, 453);
        tableLayoutPanel3.Name = "tableLayoutPanel3";
        tableLayoutPanel3.RowCount = 2;
        tableLayoutPanel3.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 50F));
        tableLayoutPanel3.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 50F));
        tableLayoutPanel3.Size = new System.Drawing.Size(795, 87);
        tableLayoutPanel3.TabIndex = 1;
        // 
        // FirstNameFilterTextBox
        // 
        FirstNameFilterTextBox.Location = new System.Drawing.Point(3, 3);
        FirstNameFilterTextBox.Name = "FirstNameFilterTextBox";
        FirstNameFilterTextBox.PlaceholderText = "First name filter";
        FirstNameFilterTextBox.Size = new System.Drawing.Size(192, 27);
        FirstNameFilterTextBox.TabIndex = 2;
        // 
        // LastNameFilterTextBox
        // 
        LastNameFilterTextBox.Location = new System.Drawing.Point(201, 3);
        LastNameFilterTextBox.Name = "LastNameFilterTextBox";
        LastNameFilterTextBox.PlaceholderText = "Last name filter";
        LastNameFilterTextBox.Size = new System.Drawing.Size(192, 27);
        LastNameFilterTextBox.TabIndex = 3;
        // 
        // EmailFilterTextBox
        // 
        EmailFilterTextBox.Location = new System.Drawing.Point(399, 3);
        EmailFilterTextBox.Name = "EmailFilterTextBox";
        EmailFilterTextBox.PlaceholderText = "Email filter";
        EmailFilterTextBox.Size = new System.Drawing.Size(197, 27);
        EmailFilterTextBox.TabIndex = 4;
        // 
        // DepartmentFilterComboBox
        // 
        DepartmentFilterComboBox.ForeColor = System.Drawing.SystemColors.ControlText;
        DepartmentFilterComboBox.FormattingEnabled = true;
        DepartmentFilterComboBox.Location = new System.Drawing.Point(602, 3);
        DepartmentFilterComboBox.Name = "DepartmentFilterComboBox";
        DepartmentFilterComboBox.Size = new System.Drawing.Size(190, 28);
        DepartmentFilterComboBox.TabIndex = 5;
        DepartmentFilterComboBox.Text = "Department filter";
        // 
        // FilterBtn
        // 
        FilterBtn.Location = new System.Drawing.Point(3, 46);
        FilterBtn.Name = "FilterBtn";
        FilterBtn.Size = new System.Drawing.Size(192, 38);
        FilterBtn.TabIndex = 6;
        FilterBtn.Text = "Filter";
        FilterBtn.UseVisualStyleBackColor = true;
        FilterBtn.Click += filterEmployeeBtn_Click;
        // 
        // ResetFilterBtn
        // 
        ResetFilterBtn.Location = new System.Drawing.Point(201, 46);
        ResetFilterBtn.Name = "ResetFilterBtn";
        ResetFilterBtn.Size = new System.Drawing.Size(192, 38);
        ResetFilterBtn.TabIndex = 7;
        ResetFilterBtn.Text = "Reset filter";
        ResetFilterBtn.UseVisualStyleBackColor = true;
        ResetFilterBtn.Click += ResetFilterBtn_Click;
        // 
        // EmployeesForm
        // 
        AutoScaleDimensions = new System.Drawing.SizeF(8F, 20F);
        AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
        ClientSize = new System.Drawing.Size(800, 588);
        Controls.Add(tableLayoutPanel3);
        Controls.Add(tableLayoutPanel1);
        Text = "Employees";
        tableLayoutPanel1.ResumeLayout(false);
        ((System.ComponentModel.ISupportInitialize)EmployeesGridView).EndInit();
        tableLayoutPanel2.ResumeLayout(false);
        tableLayoutPanel2.PerformLayout();
        tableLayoutPanel3.ResumeLayout(false);
        tableLayoutPanel3.PerformLayout();
        ResumeLayout(false);
    }

    private System.Windows.Forms.Button ResetFilterBtn;

    private System.Windows.Forms.ComboBox DepartmentFilterComboBox;
    private System.Windows.Forms.Button FilterBtn;

    private System.Windows.Forms.TextBox FirstNameFilterTextBox;
    private System.Windows.Forms.TextBox LastNameFilterTextBox;
    private System.Windows.Forms.TextBox EmailFilterTextBox;

    private System.Windows.Forms.TableLayoutPanel tableLayoutPanel3;

    private System.Windows.Forms.Button refreshDataBtn;

    private System.Windows.Forms.ComboBox DepartmentComboBox;

    private System.Windows.Forms.Button deleteEmployeeBtn;
    private System.Windows.Forms.Button AddEmployeeBtn;
    private System.Windows.Forms.TextBox LastNameTextBox;
    private System.Windows.Forms.TextBox EmailTextBox;

    private System.Windows.Forms.TextBox FirstNameTextBox;

    private System.Windows.Forms.Button updateEmployeeBtn;

    private System.Windows.Forms.TableLayoutPanel tableLayoutPanel2;

    private System.Windows.Forms.DataGridView EmployeesGridView;

    private System.Windows.Forms.TableLayoutPanel tableLayoutPanel1;

    #endregion
}