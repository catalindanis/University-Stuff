// FestivalMuzicaCSharpWFA/UI/ViewTickets.Designer.cs
using System.ComponentModel;
using System.Drawing;
using System.Windows.Forms;

namespace FestivalMuzicaCSharpWFA.UI
{
    partial class ViewTickets
    {
        private IContainer components = null;
        private DataGridView ticketsTable;
        private TextBox numberOfSeatsField;
        private Button updateTicketButton;
        private Label messageLabel;
        private Panel updatePanel;

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
            this.components = new Container();
            this.ticketsTable = new DataGridView();
            this.numberOfSeatsField = new TextBox();
            this.updateTicketButton = new Button();
            this.messageLabel = new Label();
            this.updatePanel = new Panel();

            // ticketsTable
            this.ticketsTable.AllowUserToAddRows = false;
            this.ticketsTable.AllowUserToDeleteRows = false;
            this.ticketsTable.ColumnHeadersHeightSizeMode = DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.ticketsTable.Columns.AddRange(new DataGridViewColumn[] {
                new DataGridViewTextBoxColumn { Name = "clientNameColumn", HeaderText = "Client", Width = 100 },
                new DataGridViewTextBoxColumn { Name = "showNameColumn", HeaderText = "Show", Width = 100 },
                new DataGridViewTextBoxColumn { Name = "numberOfSeatsColumn", HeaderText = "Number of seats", Width = 100 }
            });
            this.ticketsTable.Location = new Point(10, 10);
            this.ticketsTable.Size = new Size(512, 200);
            this.ticketsTable.ReadOnly = true;

            // updatePanel
            this.updatePanel.Location = new Point(10, 220);
            this.updatePanel.Size = new Size(512, 35);
            this.updatePanel.Controls.Add(this.numberOfSeatsField);
            this.updatePanel.Controls.Add(this.updateTicketButton);

            // numberOfSeatsField
            this.numberOfSeatsField.Location = new Point(0, 5);
            this.numberOfSeatsField.Size = new Size(150, 23);
            this.numberOfSeatsField.PlaceholderText = "Number of seats";

            // updateTicketButton
            this.updateTicketButton.Location = new Point(160, 3);
            this.updateTicketButton.Size = new Size(75, 27);
            this.updateTicketButton.Text = "Update";

            // messageLabel
            this.messageLabel.Location = new Point(10, 265);
            this.messageLabel.Size = new Size(512, 23);
            this.messageLabel.ForeColor = Color.Red;

            // ViewTickets
            this.AutoScaleMode = AutoScaleMode.Font;
            this.ClientSize = new Size(532, 303);
            this.Controls.Add(this.ticketsTable);
            this.Controls.Add(this.updatePanel);
            this.Controls.Add(this.messageLabel);
            this.Text = "ViewTickets";
        }
    }
}
