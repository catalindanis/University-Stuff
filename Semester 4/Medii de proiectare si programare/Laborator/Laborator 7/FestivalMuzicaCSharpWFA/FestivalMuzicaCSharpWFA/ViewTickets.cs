// FestivalMuzicaCSharpWFA/UI/ViewTickets.cs

using FestivalMuzicaClient;
using services.Utils;

namespace FestivalMuzicaCSharpWFA.UI
{
    public partial class ViewTickets : Form, IObserver
    {
        private readonly Controller _controller;
        public ViewTickets(Controller controller)
        {
            _controller = controller;
            InitializeComponent();
            _controller.Subscribe(this);
            updateTicketButton.Click += UpdateTicketClick;
            ticketsTable.SelectionMode = DataGridViewSelectionMode.FullRowSelect;
            ticketsTable.MultiSelect = false;
            LoadData();
        }

        private void LoadData()
        {
            var tickets = _controller.FindAllTickets().ToList();
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
                var tickets = _controller.FindAllTickets().ToList();
                var ticket = tickets[rowIndex];

                int numberOfSeats = int.Parse(numberOfSeatsField.Text);

                _controller.UpdateTicket(ticket.Id, ticket.Show.Id, ticket.ClientName, numberOfSeats);
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
            if (InvokeRequired)
            {
                BeginInvoke(new Action(LoadData));
                return;
            }

            LoadData();
        }

        protected override void OnFormClosed(FormClosedEventArgs e)
        {
            _controller.Unsubscribe(this);
            updateTicketButton.Click -= UpdateTicketClick;
            base.OnFormClosed(e);
        }
    }
}