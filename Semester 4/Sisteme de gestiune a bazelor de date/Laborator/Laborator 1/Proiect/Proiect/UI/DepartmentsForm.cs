using System.Data;
using Proiect.Domain;
using Proiect.Service;

namespace Proiect;

// Fereastra pentru afisarea departamentelor.
public partial class DepartmentsForm : Form
{
    private DataTable _table;
    private bool _dataLoaded;
    
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
    }
    
    // Input: fara parametri.
    // Return: void.
    // Citeste departamentele din service.
    // Populeaza grid-ul cu datele incarcate.
    private void LoadData()
    {
        _dataLoaded = false;
        foreach (var department in DepartmentsService.Instance.FindAll())
        {
            _table.Rows.Add(department.Id,
                department.Name,
                department.Location);
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
     
        var id = (long) DepartmentsGridView.SelectedRows[0].Cells[0].Value;
        var department = DepartmentsService.Instance.FindById(id);
        if (department == null) return;

        var filter = new EmployeeFilter();
        filter.DepartmentId = department.Id;
        
        var employeesForm = new EmployeesForm(false, filter);
        employeesForm.Show();
    }

    // Input: object sender, EventArgs e.
    // Return: void.
    // Handler-ul standard pentru SelectionChanged din designer.
    // Momentan nu este implementat.
    private void DepartmentsGridView_SelectionChanged(object sender, EventArgs e)
    {
        throw new System.NotImplementedException();
    }
}