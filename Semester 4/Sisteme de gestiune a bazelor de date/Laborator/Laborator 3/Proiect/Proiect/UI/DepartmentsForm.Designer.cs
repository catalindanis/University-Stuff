using System.ComponentModel;

namespace Proiect;

partial class DepartmentsForm
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
        DepartmentsGridView = new System.Windows.Forms.DataGridView();
        tableLayoutPanel1.SuspendLayout();
        ((System.ComponentModel.ISupportInitialize)DepartmentsGridView).BeginInit();
        SuspendLayout();
        // 
        // tableLayoutPanel1
        // 
        tableLayoutPanel1.ColumnCount = 1;
        tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 100F));
        tableLayoutPanel1.Controls.Add(DepartmentsGridView, 0, 0);
        tableLayoutPanel1.Location = new System.Drawing.Point(0, 0);
        tableLayoutPanel1.Name = "tableLayoutPanel1";
        tableLayoutPanel1.RowCount = 2;
        tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 80F));
        tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 20F));
        tableLayoutPanel1.Size = new System.Drawing.Size(801, 451);
        tableLayoutPanel1.TabIndex = 0;
        // 
        // DepartmentsGridView
        // 
        DepartmentsGridView.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
        DepartmentsGridView.Location = new System.Drawing.Point(3, 3);
        DepartmentsGridView.Name = "DepartmentsGridView";
        DepartmentsGridView.RowHeadersWidth = 51;
        DepartmentsGridView.Size = new System.Drawing.Size(795, 354);
        DepartmentsGridView.TabIndex = 0;
        DepartmentsGridView.Text = "dataGridView1";
        DepartmentsGridView.DoubleClick += OnSelectionChanged;
        // 
        // DepartmentsForm
        // 
        AutoScaleDimensions = new System.Drawing.SizeF(8F, 20F);
        AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
        ClientSize = new System.Drawing.Size(800, 450);
        Controls.Add(tableLayoutPanel1);
        Text = "Departments";
        tableLayoutPanel1.ResumeLayout(false);
        ((System.ComponentModel.ISupportInitialize)DepartmentsGridView).EndInit();
        ResumeLayout(false);
    }

    private System.Windows.Forms.DataGridView DepartmentsGridView;

    private System.Windows.Forms.TableLayoutPanel tableLayoutPanel1;

    #endregion
}