using System.Data;
using System.Diagnostics;
using Proiect.Domain;
using Proiect.Service;
using Proiect.Utils;

namespace Proiect;

// Fereastra pentru afisarea departamentelor.
public partial class DepartmentsForm : Form
{
    // Schimba valoarea aici pentru a comuta intre cursor-based si offset-based pagination.
    private readonly bool _useCursorPagination = false;
    private DataTable _table = null!;
    private bool _dataLoaded;
    private int _currentPage;
    private int _pageSize;
    private int _totalPages;
    private int _totalRecords;
    private long? _lastId;
    private long? _nextCursor;
    private readonly Stack<long?> _cursorHistory = new();
    
    // Input: fara parametri.
    // Return: nu returneaza nimic (constructor).
    // Initializeaza formularul, structura tabelului si stilul.
    // Incarca datele departamentelor in grid.
    public DepartmentsForm()
    {
        InitializeComponent();
        InitializeFields();
        LoadData();
        LoadStyle();
    }
    
    // Input: fara parametri.
    // Return: void.
    // Creeaza DataTable-ul folosit ca sursa pentru grid.
    // Defineste coloanele afisate in UI.
    private void InitializeFields()
    { 
        _table = new DataTable();
        _table.Columns.Add("Id", typeof(long));
        _table.Columns.Add("Name", typeof(string));
        _table.Columns.Add("Location", typeof(string));
        _currentPage = 1;
        _pageSize = 10;
        _lastId = null;
        _nextCursor = null;
    }
    
    // Input: fara parametri.
    // Return: void.
    // Citeste departamentele din service.
    // Populeaza grid-ul cu datele incarcate.
    private void LoadData()
    {
        _dataLoaded = false;
        _table.Rows.Clear();

        Stopwatch sw = Stopwatch.StartNew();
        PaginatedResponse<Department> response = _useCursorPagination
            ? DepartmentsService.Instance.FindAllPaginatedByCursor(_lastId, _pageSize)
            : DepartmentsService.Instance.FindAllPaginated(_currentPage, _pageSize);
        sw.Stop();
        Console.WriteLine($"Page {_currentPage} - Loaded in {sw.ElapsedMilliseconds}ms");
        Console.WriteLine($"Method used: {(_useCursorPagination ? "Cursor-based" : "Offset-based")} pagination");
        
        _currentPage = response.PageNumber;
        _totalPages = response.TotalPages;
        _totalRecords = response.TotalRecords;
        PageInfoLabel.Text = $"Page {_currentPage} of {_totalPages} (Total: {_totalRecords} records)";

        _nextCursor = null;
        foreach (var department in response.Data)
        {
            _table.Rows.Add(department.Id,
                department.Name,
                department.Location);
            _nextCursor = department.Id;
        }

        _dataLoaded = true;
        DepartmentsGridView.DataSource = _table;
    }
    
        
    // Input: fara parametri.
    // Return: void.
    // Configureaza stilul si comportamentul DataGridView-ului.
    // Activeaza selectia pe rand intreg si mod read-only.
    private void LoadStyle()
    {
        DepartmentsGridView.SelectionMode = DataGridViewSelectionMode.FullRowSelect;
        DepartmentsGridView.MultiSelect = false;
        DepartmentsGridView.ReadOnly = true;
        PageSizeComboBox.SelectedIndex = 1;
    }
    
    // Input: object sender, EventArgs e.
    // Return: void.
    // Ruleaza cand selectia din grid se schimba.
    // Deschide formularul de angajati filtrat pe departamentul selectat.
    public void OnSelectionChanged(object sender, EventArgs e)
    {
        if(!_dataLoaded || DepartmentsGridView.SelectedRows.Count == 0 ||
           DepartmentsGridView.SelectedRows[0].Cells[0].Value == null ||
           DepartmentsGridView.SelectedRows[0].Cells[0].Value == DBNull.Value)
            return;
     
        var id = Convert.ToInt64(DepartmentsGridView.SelectedRows[0].Cells[0].Value);

        var filter = new EmployeeFilter();
        filter.DepartmentId = id;
        
        new EmployeesForm(false, filter).Show();
    }

    private void PreviousButton_Click(object sender, EventArgs e)
    {
        if(_currentPage <= 1)
            return;
        
        if (_useCursorPagination)
        {
            if (_cursorHistory.Count == 0)
                return;

            _lastId = _cursorHistory.Pop();
            LoadData();
            return;
        }

        _currentPage--;
        LoadData();
    }

    private void NextButton_Click(object sender, EventArgs e)
    {
        if (_currentPage >= _totalPages)
            return;
        
        if (_useCursorPagination)
        {
            if (_nextCursor == null)
                return;

            _cursorHistory.Push(_lastId);
            _lastId = _nextCursor;
            LoadData();
            return;
        }

        _currentPage++;
        LoadData();
    }

    private void PageSizeComboBox_SelectedIndexChanged(object sender, EventArgs e)
    {
        _pageSize = int.Parse(PageSizeComboBox.SelectedItem.ToString());
        _currentPage = 1;
        _lastId = null;
        _nextCursor = null;
        _cursorHistory.Clear();
        LoadData();
    }
}