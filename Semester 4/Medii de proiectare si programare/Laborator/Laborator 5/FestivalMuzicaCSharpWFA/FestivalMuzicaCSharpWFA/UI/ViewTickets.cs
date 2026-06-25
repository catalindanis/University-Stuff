// FestivalMuzicaCSharpWFA/UI/ViewTickets.cs

using FestivalMuzicaCSharpWFA.Service;
using FestivalMuzicaCSharpWFA.Utils;

namespace FestivalMuzicaCSharpWFA.UI
{
    public partial class ViewTickets : Form, IObserver
    {
        public ViewTickets()
        {
            InitializeComponent();
            ShowsService.Instance.Subscribe(this);
            updateTicketButton.Click += UpdateTicketClick;
            ticketsTable.SelectionMode = DataGridViewSelectionMode.FullRowSelect;
            ticketsTable.MultiSelect = false;
            LoadData();
        }

        private void LoadData()
        {
            var tickets = ShowsService.Instance.FindAllTickets().ToList();
            ticketsTable.Rows.Clear();
            foreach (var ticket in tickets)
            {
                var show = ticket.Show;
                string showName = $"{show.ArtistName} - {show.Date} - {show.Location}";
                ticketsTable.Rows.Add(ticket.ClientName, showName, ticket.NoSeats);
            }
            
            ticketsTable.ClearSelection();
            ticketsTable.CurrentCell = null;
        }

        private void UpdateTicketClick(object? sender, EventArgs e)
        {
            try
            {
                ValidateUpdateInputs();

                if (ticketsTable.SelectedRows.Count == 0)
                    throw new Exception("You must select a ticket first");

                int rowIndex = ticketsTable.SelectedRows[0].Index;
                var tickets = ShowsService.Instance.FindAllTickets().ToList();
                var ticket = tickets[rowIndex];

                int numberOfSeats = int.Parse(numberOfSeatsField.Text);

                ShowsService.Instance.UpdateTicket(ticket.Id, ticket.ClientName, numberOfSeats);
            }
            catch (Exception ex)
            {
                messageLabel.Text = ex.Message;
            }
        }

        private void ValidateUpdateInputs()
        {
            var errorMessage = "";

            if (ticketsTable.SelectedRows.Count == 0)
                errorMessage += "You must select a ticket first\n";

            if (!int.TryParse(numberOfSeatsField.Text, out _))
                errorMessage += "Please enter a valid integer\n";

            if (!string.IsNullOrEmpty(errorMessage))
                throw new Exception(errorMessage);
        }

        public void Update()
        {
            LoadData();
        }
    }
}