namespace Proiect;

partial class AdminForm
{
    private System.ComponentModel.IContainer components = null;

    protected override void Dispose(bool disposing)
    {
        if (disposing && (components != null))
        {
            components.Dispose();
        }
        base.Dispose(disposing);
    }

    private void InitializeComponent()
    {
        tableLayoutPanel1 = new System.Windows.Forms.TableLayoutPanel();
        DeletedGridView = new System.Windows.Forms.DataGridView();
        tableLayoutPanel2 = new System.Windows.Forms.TableLayoutPanel();
        RestoreBtn = new System.Windows.Forms.Button();
        HardDeleteBtn = new System.Windows.Forms.Button();
        RefreshBtn = new System.Windows.Forms.Button();
        
        tableLayoutPanel1.SuspendLayout();
        ((System.ComponentModel.ISupportInitialize)DeletedGridView).BeginInit();
        tableLayoutPanel2.SuspendLayout();
        SuspendLayout();
        
        tableLayoutPanel1.ColumnCount = 1;
        tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 100F));
        tableLayoutPanel1.Controls.Add(DeletedGridView, 0, 0);
        tableLayoutPanel1.Controls.Add(tableLayoutPanel2, 0, 1);
        tableLayoutPanel1.Dock = System.Windows.Forms.DockStyle.Fill;
        tableLayoutPanel1.Location = new System.Drawing.Point(0, 0);
        tableLayoutPanel1.Name = "tableLayoutPanel1";
        tableLayoutPanel1.RowCount = 2;
        tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 85F));
        tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 15F));
        tableLayoutPanel1.Size = new System.Drawing.Size(700, 450);
        tableLayoutPanel1.TabIndex = 0;
        
        DeletedGridView.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
        DeletedGridView.Location = new System.Drawing.Point(3, 3);
        DeletedGridView.Name = "DeletedGridView";
        DeletedGridView.RowHeadersWidth = 51;
        DeletedGridView.Size = new System.Drawing.Size(694, 375);
        DeletedGridView.TabIndex = 0;
        
        tableLayoutPanel2.ColumnCount = 3;
        tableLayoutPanel2.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 33.33F));
        tableLayoutPanel2.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 33.33F));
        tableLayoutPanel2.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 33.34F));
        tableLayoutPanel2.Controls.Add(RestoreBtn, 0, 0);
        tableLayoutPanel2.Controls.Add(HardDeleteBtn, 1, 0);
        tableLayoutPanel2.Controls.Add(RefreshBtn, 2, 0);
        tableLayoutPanel2.Dock = System.Windows.Forms.DockStyle.Fill;
        tableLayoutPanel2.Location = new System.Drawing.Point(3, 384);
        tableLayoutPanel2.Name = "tableLayoutPanel2";
        tableLayoutPanel2.RowCount = 1;
        tableLayoutPanel2.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 100F));
        tableLayoutPanel2.Size = new System.Drawing.Size(694, 63);
        tableLayoutPanel2.TabIndex = 1;
        
        RestoreBtn.Location = new System.Drawing.Point(3, 3);
        RestoreBtn.Name = "RestoreBtn";
        RestoreBtn.Size = new System.Drawing.Size(225, 52);
        RestoreBtn.TabIndex = 0;
        RestoreBtn.Text = "Restore";
        RestoreBtn.UseVisualStyleBackColor = true;
        RestoreBtn.Click += RestoreBtn_Click;
        
        HardDeleteBtn.Location = new System.Drawing.Point(234, 3);
        HardDeleteBtn.Name = "HardDeleteBtn";
        HardDeleteBtn.Size = new System.Drawing.Size(225, 52);
        HardDeleteBtn.TabIndex = 1;
        HardDeleteBtn.Text = "Permanent Delete";
        HardDeleteBtn.UseVisualStyleBackColor = true;
        HardDeleteBtn.Click += HardDeleteBtn_Click;
        
        RefreshBtn.Location = new System.Drawing.Point(465, 3);
        RefreshBtn.Name = "RefreshBtn";
        RefreshBtn.Size = new System.Drawing.Size(226, 52);
        RefreshBtn.TabIndex = 2;
        RefreshBtn.Text = "Refresh";
        RefreshBtn.UseVisualStyleBackColor = true;
        RefreshBtn.Click += RefreshBtn_Click;
        
        AutoScaleDimensions = new System.Drawing.SizeF(8F, 20F);
        AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
        ClientSize = new System.Drawing.Size(700, 450);
        Controls.Add(tableLayoutPanel1);
        Text = "Admin - Deleted Employees";
        
        tableLayoutPanel1.ResumeLayout(false);
        ((System.ComponentModel.ISupportInitialize)DeletedGridView).EndInit();
        tableLayoutPanel2.ResumeLayout(false);
        ResumeLayout(false);
    }

    private System.Windows.Forms.TableLayoutPanel tableLayoutPanel1;
    private System.Windows.Forms.DataGridView DeletedGridView;
    private System.Windows.Forms.TableLayoutPanel tableLayoutPanel2;
    private System.Windows.Forms.Button RestoreBtn;
    private System.Windows.Forms.Button HardDeleteBtn;
    private System.Windows.Forms.Button RefreshBtn;
}
