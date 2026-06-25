using System.ComponentModel;

// FestivalMuzicaCSharpWFA/UI/FilteredShows.Designer.cs
namespace FestivalMuzicaCSharpWFA.UI
{
    partial class FilteredShows
    {
        private System.ComponentModel.IContainer components = null;
        private System.Windows.Forms.DataGridView showsTable;

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
            this.showsTable = new System.Windows.Forms.DataGridView();
            ((System.ComponentModel.ISupportInitialize)(this.showsTable)).BeginInit();
            this.SuspendLayout();
            // 
            // showsTable
            // 
            this.showsTable.AllowUserToAddRows = false;
            this.showsTable.AllowUserToDeleteRows = false;
            this.showsTable.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.showsTable.Columns.AddRange(new System.Windows.Forms.DataGridViewColumn[] {
                new System.Windows.Forms.DataGridViewTextBoxColumn { Name = "artistColumn", HeaderText = "Artist", Width = 100 },
                new System.Windows.Forms.DataGridViewTextBoxColumn { Name = "dateColumn", HeaderText = "Date", Width = 100 },
                new System.Windows.Forms.DataGridViewTextBoxColumn { Name = "locationColumn", HeaderText = "Location", Width = 100 },
                new System.Windows.Forms.DataGridViewTextBoxColumn { Name = "remainingSeatsColumn", HeaderText = "Remaining Seats", Width = 100 },
                new System.Windows.Forms.DataGridViewTextBoxColumn { Name = "soldSeatsColumn", HeaderText = "Sold Seats", Width = 100 }
            });
            this.showsTable.Dock = System.Windows.Forms.DockStyle.Fill;
            this.showsTable.Location = new System.Drawing.Point(0, 0);
            this.showsTable.Name = "showsTable";
            this.showsTable.ReadOnly = true;
            this.showsTable.Size = new System.Drawing.Size(532, 220);
            this.showsTable.TabIndex = 0;
            // 
            // FilteredShows
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(7F, 15F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(532, 220);
            this.Controls.Add(this.showsTable);
            this.Name = "FilteredShows";
            this.Text = "Filtered Shows";
            ((System.ComponentModel.ISupportInitialize)(this.showsTable)).EndInit();
            this.ResumeLayout(false);
        }
    }
}
