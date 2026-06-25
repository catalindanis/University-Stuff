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
        paginationPanel = new System.Windows.Forms.Panel();
        PreviousButton = new System.Windows.Forms.Button();
        NextButton = new System.Windows.Forms.Button();
        PageSizeComboBox = new System.Windows.Forms.ComboBox();
        PageInfoLabel = new System.Windows.Forms.Label();
        tableLayoutPanel1.SuspendLayout();
        ((System.ComponentModel.ISupportInitialize)DepartmentsGridView).BeginInit();
        paginationPanel.SuspendLayout();
        SuspendLayout();
        // 
        // tableLayoutPanel1
        // 
        tableLayoutPanel1.ColumnCount = 1;
        tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 100F));
        tableLayoutPanel1.Controls.Add(DepartmentsGridView, 0, 0);
        tableLayoutPanel1.Controls.Add(paginationPanel, 0, 1);
        tableLayoutPanel1.Location = new System.Drawing.Point(0, 0);
        tableLayoutPanel1.Name = "tableLayoutPanel1";
        tableLayoutPanel1.RowCount = 2;
        tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 85F));
        tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 15F));
        tableLayoutPanel1.Size = new System.Drawing.Size(801, 451);
        tableLayoutPanel1.TabIndex = 0;
        // 
        // DepartmentsGridView
        // 
        DepartmentsGridView.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
        DepartmentsGridView.Location = new System.Drawing.Point(3, 3);
        DepartmentsGridView.Name = "DepartmentsGridView";
        DepartmentsGridView.RowHeadersWidth = 51;
        DepartmentsGridView.Size = new System.Drawing.Size(795, 368);
        DepartmentsGridView.TabIndex = 0;
        DepartmentsGridView.Text = "dataGridView1";
        DepartmentsGridView.DoubleClick += OnSelectionChanged;
        // 
        // paginationPanel
        // 
        paginationPanel.Controls.Add(PreviousButton);
        paginationPanel.Controls.Add(NextButton);
        paginationPanel.Controls.Add(PageSizeComboBox);
        paginationPanel.Controls.Add(PageInfoLabel);
        paginationPanel.Dock = System.Windows.Forms.DockStyle.Fill;
        paginationPanel.Location = new System.Drawing.Point(3, 386);
        paginationPanel.Name = "paginationPanel";
        paginationPanel.Size = new System.Drawing.Size(795, 62);
        paginationPanel.TabIndex = 1;
        // 
        // PreviousButton
        // 
        PreviousButton.Location = new System.Drawing.Point(10, 10);
        PreviousButton.Name = "PreviousButton";
        PreviousButton.Size = new System.Drawing.Size(100, 35);
        PreviousButton.TabIndex = 0;
        PreviousButton.Text = "← Previous";
        PreviousButton.UseVisualStyleBackColor = true;
        PreviousButton.Click += PreviousButton_Click;
        // 
        // NextButton
        // 
        NextButton.Location = new System.Drawing.Point(115, 10);
        NextButton.Name = "NextButton";
        NextButton.Size = new System.Drawing.Size(100, 35);
        NextButton.TabIndex = 1;
        NextButton.Text = "Next →";
        NextButton.UseVisualStyleBackColor = true;
        NextButton.Click += NextButton_Click;
        // 
        // PageSizeComboBox
        // 
        PageSizeComboBox.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
        PageSizeComboBox.FormattingEnabled = true;
        PageSizeComboBox.Items.AddRange(new object[] { "5", "10", "25", "50" });
        PageSizeComboBox.Location = new System.Drawing.Point(220, 10);
        PageSizeComboBox.Name = "PageSizeComboBox";
        PageSizeComboBox.Size = new System.Drawing.Size(80, 23);
        PageSizeComboBox.TabIndex = 2;
        PageSizeComboBox.SelectedIndexChanged += PageSizeComboBox_SelectedIndexChanged;
        // 
        // PageInfoLabel
        // 
        PageInfoLabel.AutoSize = true;
        PageInfoLabel.Location = new System.Drawing.Point(310, 15);
        PageInfoLabel.Name = "PageInfoLabel";
        PageInfoLabel.Size = new System.Drawing.Size(156, 15);
        PageInfoLabel.TabIndex = 3;
        PageInfoLabel.Text = "Page 1 of 1 (Total: 0 records)";
        // 
        // DepartmentsForm
        // 
        AutoScaleDimensions = new System.Drawing.SizeF(7F, 15F);
        AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
        ClientSize = new System.Drawing.Size(800, 450);
        Controls.Add(tableLayoutPanel1);
        Margin = new System.Windows.Forms.Padding(3, 2, 3, 2);
        Text = "Departments";
        tableLayoutPanel1.ResumeLayout(false);
        ((System.ComponentModel.ISupportInitialize)DepartmentsGridView).EndInit();
        paginationPanel.ResumeLayout(false);
        paginationPanel.PerformLayout();
        ResumeLayout(false);
    }

    private System.Windows.Forms.DataGridView DepartmentsGridView;
    private System.Windows.Forms.TableLayoutPanel tableLayoutPanel1;
    private System.Windows.Forms.Panel paginationPanel;
    private System.Windows.Forms.Button PreviousButton;
    private System.Windows.Forms.Button NextButton;
    private System.Windows.Forms.ComboBox PageSizeComboBox;
    private System.Windows.Forms.Label PageInfoLabel;

    #endregion
}