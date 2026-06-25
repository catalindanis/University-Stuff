using System.Data;
using Proiect.Service;

namespace Proiect;

// Fereastra administrativa minimalista pentru vizualizare inregistrari sterse.
public partial class AdminForm : Form
{
    private DataTable _table;
    
    // Input: fara parametri.
    // Return: nu returneaza nimic (constructor).
    // Initializeaza formularul si incarca inregistrarile sterse.
    public AdminForm()
    {
        InitializeComponent();
        InitializeFields();
        LoadDeletedEmployees();
        LoadStyle();
    }
    
    // Input: fara parametri.
    // Return: void.
    // Creeaza DataTable-ul cu coloanele pentru angajati stersi.
    private void InitializeFields()
    {
        _table = new DataTable();
        _table.Columns.Add("Id", typeof(long));
        _table.Columns.Add("First Name", typeof(string));
        _table.Columns.Add("Last Name", typeof(string));
        _table.Columns.Add("Email", typeof(string));
        _table.Columns.Add("Department", typeof(string));
    }
    
    // Input: fara parametri.
    // Return: void.
    // Incarca angajatii marcati ca stersi in DataTable.
    private void LoadDeletedEmployees()
    {
        foreach (var employee in EmployeesService.Instance.FindDeleted(isAdmin: true))
        {
            _table.Rows.Add(employee.Id,
                employee.FirstName,
                employee.LastName,
                employee.Email,
                employee.Department?.Name ?? "N/A"
            );
        }
        DeletedGridView.DataSource = _table;
    }
    
    // Input: fara parametri.
    // Return: void.
    // Configureaza stilul grilei si setarile de selectie.
    private void LoadStyle()
    {
        DeletedGridView.SelectionMode = DataGridViewSelectionMode.FullRowSelect;
        DeletedGridView.MultiSelect = false;
        DeletedGridView.ReadOnly = true;
    }
    
    // Input: object sender, EventArgs e.
    // Return: void.
    // Restaureaza angajatul selectat (soft deleted -> active).
    private void RestoreBtn_Click(object sender, EventArgs e)
    {
        if (DeletedGridView.SelectedRows.Count == 0 ||
            DeletedGridView.SelectedRows[0].Cells[0].Value == null ||
            DeletedGridView.SelectedRows[0].Cells[0].Value == DBNull.Value)
        {
            MessageBox.Show("Select an employee to restore.");
            return;
        }

        var result = MessageBox.Show(
            "Restore this employee?",
            "Confirm Restore",
            MessageBoxButtons.YesNo,
            MessageBoxIcon.Question,
            MessageBoxDefaultButton.Button2
        );

        if (result == DialogResult.No)
            return;

        var id = (long)DeletedGridView.SelectedRows[0].Cells[0].Value!;
        EmployeesService.Instance.RestoreById(id, isAdmin: true);
        
        _table.Rows.Clear();
        LoadDeletedEmployees();
        MessageBox.Show("Employee restored.");
    }

    // Input: object sender, EventArgs e.
    // Return: void.
    // Sterge definitiv angajatul selectat din baza de date.
    private void HardDeleteBtn_Click(object sender, EventArgs e)
    {
        if (DeletedGridView.SelectedRows.Count == 0 ||
            DeletedGridView.SelectedRows[0].Cells[0].Value == null ||
            DeletedGridView.SelectedRows[0].Cells[0].Value == DBNull.Value)
        {
            MessageBox.Show("Select an employee to permanently delete.");
            return;
        }

        var result = MessageBox.Show(
            "PERMANENTLY DELETE this employee? Cannot undo.",
            "Confirm Permanent Delete",
            MessageBoxButtons.YesNo,
            MessageBoxIcon.Warning,
            MessageBoxDefaultButton.Button2
        );

        if (result == DialogResult.No)
            return;

        var id = (long)DeletedGridView.SelectedRows[0].Cells[0].Value!;
        EmployeesService.Instance.HardDeleteById(id, isAdmin: true);
        
        _table.Rows.Clear();
        LoadDeletedEmployees();
        MessageBox.Show("Employee permanently deleted.");
    }

    // Input: object sender, EventArgs e.
    // Return: void.
    // Reincarca lista de angajati stersi.
    private void RefreshBtn_Click(object sender, EventArgs e)
    {
        _table.Rows.Clear();
        LoadDeletedEmployees();
    }
}
