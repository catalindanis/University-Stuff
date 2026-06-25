﻿namespace Proiect;

partial class MainMenu
{
    /// <summary>
    ///  Required designer variable.
    /// </summary>
    private System.ComponentModel.IContainer components = null;

    /// <summary>
    ///  Clean up any resources being used.
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
        departmentsBtn = new System.Windows.Forms.Button();
        employeesBtn = new System.Windows.Forms.Button();
        adminBtn = new System.Windows.Forms.Button();
        tableLayoutPanel1.SuspendLayout();
        SuspendLayout();
        // 
        // tableLayoutPanel1
        // 
        tableLayoutPanel1.ColumnCount = 1;
        tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 20F));
        tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 20F));
        tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 20F));
        tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 20F));
        tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 20F));
        tableLayoutPanel1.Controls.Add(departmentsBtn, 2, 3);
        tableLayoutPanel1.Controls.Add(employeesBtn, 0, 1);
        tableLayoutPanel1.Controls.Add(adminBtn, 4, 3);
        tableLayoutPanel1.Dock = System.Windows.Forms.DockStyle.Fill;
        tableLayoutPanel1.Location = new System.Drawing.Point(0, 0);
        tableLayoutPanel1.Name = "tableLayoutPanel1";
        tableLayoutPanel1.RowCount = 5;
        tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 20F));
        tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 20F));
        tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 20F));
        tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 20F));
        tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 20F));
        tableLayoutPanel1.Size = new System.Drawing.Size(282, 553);
        tableLayoutPanel1.TabIndex = 0;
        tableLayoutPanel1.Paint += tableLayoutPanel1_Paint;
        // 
        // departmentsBtn
        // 
        departmentsBtn.AutoSizeMode = System.Windows.Forms.AutoSizeMode.GrowAndShrink;
        departmentsBtn.Location = new System.Drawing.Point(3, 333);
        departmentsBtn.Name = "departmentsBtn";
        departmentsBtn.Size = new System.Drawing.Size(276, 84);
        departmentsBtn.TabIndex = 1;
        departmentsBtn.Text = "Departments";
        departmentsBtn.UseVisualStyleBackColor = true;
        departmentsBtn.Click += button2_Click;
        // 
        // employeesBtn
        // 
        employeesBtn.AutoSize = true;
        employeesBtn.Location = new System.Drawing.Point(3, 113);
        employeesBtn.Name = "employeesBtn";
        employeesBtn.Size = new System.Drawing.Size(276, 84);
        employeesBtn.TabIndex = 0;
        employeesBtn.Text = "Employees";
        employeesBtn.UseVisualStyleBackColor = true;
        employeesBtn.Click += employeesBtn_Click;
        // 
        // adminBtn
        // 
        adminBtn.AutoSizeMode = System.Windows.Forms.AutoSizeMode.GrowAndShrink;
        adminBtn.Location = new System.Drawing.Point(3, 433);
        adminBtn.Name = "adminBtn";
        adminBtn.Size = new System.Drawing.Size(276, 84);
        adminBtn.TabIndex = 2;
        adminBtn.Text = "Admin";
        adminBtn.UseVisualStyleBackColor = true;
        adminBtn.Click += adminBtn_Click;
        // 
        // MainMenu
        // 
        AutoScaleDimensions = new System.Drawing.SizeF(8F, 20F);
        AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
        ClientSize = new System.Drawing.Size(282, 553);
        Controls.Add(tableLayoutPanel1);
        Text = "Main menu";
        Load += Form1_Load;
        tableLayoutPanel1.ResumeLayout(false);
        tableLayoutPanel1.PerformLayout();
        ResumeLayout(false);
    }

    private System.Windows.Forms.Button departmentsBtn;

    private System.Windows.Forms.TableLayoutPanel tableLayoutPanel1;
    private System.Windows.Forms.Button employeesBtn;
    private System.Windows.Forms.Button adminBtn;

    #endregion
}