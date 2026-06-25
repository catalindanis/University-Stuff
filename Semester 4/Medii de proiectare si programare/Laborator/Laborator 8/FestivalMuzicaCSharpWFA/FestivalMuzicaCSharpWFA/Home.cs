using FestivalMuzicaClient;
using FestivalMuzicaCSharp.Domain;
using FestivalMuzicaCSharpWFA.Utils;
using services.Utils;

namespace FestivalMuzicaCSharpWFA.UI;

public partial class Home : Form, IPropsReceiver, IObserver
{
    private readonly Controller _controller;
    private List<Form> openedForms = new();
    private Dictionary<string, object>? props;

    public Home(Controller controller)
    {
        _controller = controller;
        InitializeComponent();
        _controller.Subscribe(this);
        InitializeTable();
        searchButton.Click += SearchButtonClick;
        buyButton.Click += BuyButtonClick;
        viewAllTicketsButton.Click += ViewAllTicketsButtonClick;
        logoutButton.Click += LogoutClick;
        LoadData();
    }

    private void InitializeTable()
    {
        showsTable.SelectionMode = DataGridViewSelectionMode.FullRowSelect;
        showsTable.MultiSelect = false; 
        showsTable.AutoGenerateColumns = false;
        showsTable.Columns.Clear();
        showsTable.Columns.Add(new DataGridViewTextBoxColumn { DataPropertyName = "ArtistName", HeaderText = "Artist" });
        showsTable.Columns.Add(new DataGridViewTextBoxColumn { DataPropertyName = "Date", HeaderText = "Date" });
        showsTable.Columns.Add(new DataGridViewTextBoxColumn { DataPropertyName = "Location", HeaderText = "Location" });
        showsTable.Columns.Add(new DataGridViewTextBoxColumn { DataPropertyName = "RemainingSeats", HeaderText = "Remaining Seats" });
        showsTable.Columns.Add(new DataGridViewTextBoxColumn { DataPropertyName = "SoldSeats", HeaderText = "Sold Seats" });
        showsTable.RowPrePaint += (s, e) => {
            var row = showsTable.Rows[e.RowIndex];
            if (row.DataBoundItem is ShowViewModel showVm && showVm.RemainingSeats == 0)
                row.DefaultCellStyle.BackColor = Color.LightCoral;
            else
                row.DefaultCellStyle.BackColor = Color.White;
        };
        datePickerField.Format = DateTimePickerFormat.Custom;
        datePickerField.CustomFormat = " "; // Blank

        datePickerField.ValueChanged += (s, e) => {
            datePickerField.Format = DateTimePickerFormat.Short;
        };
    }

    private void LoadData()
    {
        var shows = _controller.FindAll();
        var showViewModels = shows.Select(s => new ShowViewModel(s, _controller.GetNumberOfSoldSeatsForShow(s.Id))).ToList();
        showsTable.DataSource = showViewModels;
        showsTable.ClearSelection();
        showsTable.CurrentCell = null;
    }

    private void BuyButtonClick(object? sender, EventArgs e)
    {
        try
        {
            ValidateBuyInputs();
            var selectedRow = showsTable.CurrentRow;
            if (selectedRow?.DataBoundItem is not ShowViewModel showVm)
                throw new Exception("You must select a show first");
            string client = clientsNameField.Text;
            int numberOfSeats = int.Parse(numberOfSeatsField.Text);
            _controller.BookTicketForShow(showVm.Id, client, numberOfSeats);
            ResetFields();
            //LoadData();
            messageLabel.Text = "Ticket bought successfully";
        }
        catch (Exception ex)
        {
            messageLabel.Text = ex.Message;
        }
    }

    private void ViewAllTicketsButtonClick(object? sender, EventArgs e)
    {
        var ticketsForm = new ViewTickets(_controller);
        openedForms.Add(
            Navigator.NavigateTo(ticketsForm, "Tickets", true)
        );
    }

    private void SearchButtonClick(object? sender, EventArgs e)
    {
        try
        {
            ValidateSearchInputs();

            var filter = new ShowFilter(
                artistSearchField.Text,
                datePickerField.Format != DateTimePickerFormat.Custom ? DateOnly.FromDateTime(datePickerField.Value) : null,
                null,
                null
            );

            var props = new Dictionary<string, object>
            {
                { "showsFilter", filter }
            };

            var filteredShowsForm = new FilteredShows(_controller);
            openedForms.Add(
                    Navigator.NavigateTo(filteredShowsForm, "Shows (filtered)", true, props)
                );
            openedForms.Add(filteredShowsForm);
        }
        catch (Exception ex)
        {
            messageLabel.Text = ex.Message;
        }
    }

    private void ValidateBuyInputs()
    {
        string client = clientsNameField.Text;
        string numberOfSeats = numberOfSeatsField.Text;
        string errorMessage = string.Empty;
        if (showsTable.CurrentRow?.DataBoundItem is not ShowViewModel)
            errorMessage += "You must select a show first\n";
        if (string.IsNullOrWhiteSpace(client))
            errorMessage += "Client name cannot be empty\n";
        if (!int.TryParse(numberOfSeats, out _))
            errorMessage += "Please enter a valid integer\n";
        if (!string.IsNullOrEmpty(errorMessage))
            throw new Exception(errorMessage);
    }

    private void ValidateSearchInputs()
    {
        // Add validation if needed
    }

    private void LogoutClick(object? sender, EventArgs e)
    {
        foreach (var form in openedForms)
        {
            form.Close();
        }

        openedForms.Clear();
        this.Close();
    }

    public void SetProps(Dictionary<string, object> props)
    {
        this.props = props;
        LoadData();
    }

    private void ResetFields()
    {
        datePickerField.Format = DateTimePickerFormat.Custom;
        datePickerField.CustomFormat = " ";
        messageLabel.Text = string.Empty;
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

    private class ShowViewModel
    {
        public long Id { get; }
        public string ArtistName { get; }
        public DateOnly Date { get; }
        public string Location { get; }
        public int RemainingSeats { get; }
        public int SoldSeats { get; }
        public ShowViewModel(Show show, int soldSeats)
        {
            Id = show.Id;
            ArtistName = show.ArtistName;
            Date = show.Date;
            Location = show.Location;
            RemainingSeats = show.RemainingSeats;
            SoldSeats = soldSeats;
        }
    }

    protected override void OnFormClosed(FormClosedEventArgs e)
    {
        _controller.Logout();
        _controller.Unsubscribe(this);
        searchButton.Click -= SearchButtonClick;
        buyButton.Click -= BuyButtonClick;
        viewAllTicketsButton.Click -= ViewAllTicketsButtonClick;
        logoutButton.Click -= LogoutClick;

        base.OnFormClosed(e);
    }
}