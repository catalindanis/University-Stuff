using FestivalMuzicaClient;
using FestivalMuzicaCSharp.Domain;
using FestivalMuzicaCSharpWFA.Utils;
using services.Utils;

namespace FestivalMuzicaCSharpWFA.UI
{
    public partial class FilteredShows : Form, IPropsReceiver, IObserver
    {
        private Dictionary<string, object> _props;
        private readonly Controller _controller;

        public FilteredShows(Controller controller)
        {
            _controller = controller;
            InitializeComponent();
            showsTable.SelectionMode = DataGridViewSelectionMode.FullRowSelect;
            showsTable.MultiSelect = false;
            _controller.Subscribe(this);
        }

        public void SetProps(Dictionary<string, object> props)
        {
            _props = props;
            LoadData();
        }

        private void LoadData()
        {
            if (_props == null || !_props.ContainsKey("showsFilter"))
                return;

            var showFilter = _props["showsFilter"] as ShowFilter;
            var shows = _controller.FindAll(showFilter).ToList();

            showsTable.Rows.Clear();
            foreach (var show in shows)
            {
                int soldSeats = _controller.GetNumberOfSoldSeatsForShow(show.Id);
                int rowIndex = showsTable.Rows.Add(
                    show.ArtistName,
                    show.Date,
                    show.Location,
                    show.RemainingSeats,
                    soldSeats
                );
                // Highlight row if no remaining seats
                if (show.RemainingSeats == 0)
                {
                    showsTable.Rows[rowIndex].DefaultCellStyle.BackColor = Color.LightCoral;
                }
            }
            
            showsTable.ClearSelection();
            showsTable.CurrentCell = null;
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
            base.OnFormClosed(e);
        }
    }
}