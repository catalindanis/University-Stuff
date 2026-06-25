using System.Data;
using System.Text.RegularExpressions;
using Proiect.Domain;
using Proiect.Service;
using Proiect.Utils;

namespace Proiect;

// Fereastra pentru operatii CRUD si filtrare pe angajati.
public partial class EmployeesForm : Form
{
    private DataTable _table;
    private bool _crudActive;
    private EmployeeFilter? _filter;
    
    // Input: fara parametri.
    // Return: nu returneaza nimic (constructor).
    // Deschide formularul cu CRUD activ.
    // Nu aplica filtre initiale.
    public EmployeesForm() : this(true, null)
    {
    }

    // Input: bool crudActive.
    // Return: nu returneaza nimic (constructor).
    // Controleaza vizibilitatea sectiunii CRUD.
    // Nu aplica filtre initiale.
    public EmployeesForm(bool crudActive) : this(crudActive, null)
    {
    }

    // Input: bool crudActive, EmployeeFilter? filter.
    // Return: nu returneaza nimic (constructor).
    // Initializeaza formularul cu setari de CRUD si filtru.
    // Incarca datele si stilul controalelor.
    public EmployeesForm(bool crudActive, EmployeeFilter? filter)
    {
        _crudActive = crudActive;
        _filter = filter ?? new EmployeeFilter();
        InitializeComponent();
        InitializeFields();
        LoadData();
        LoadStyle();
    }
    
    // Input: fara parametri.
    // Return: void.
    // Configureaza DataTable-ul si sursele pentru ComboBox.
    // Aplica si valorile initiale din filtru.
    private void InitializeFields()
    {
        _table = new DataTable();
        
        _table.Columns.Add("Id", typeof(long));
        _table.Columns.Add("First Name", typeof(string));
        _table.Columns.Add("Last Name", typeof(string));
        _table.Columns.Add("Email", typeof(string));
        _table.Columns.Add("Department", typeof(string));
        
        DepartmentComboBox.DataSource = DepartmentsService.Instance.FindAll();

        FirstNameFilterTextBox.Text = _filter.FirstName ?? "";
        LastNameFilterTextBox.Text = _filter.LastName ?? "";
        EmailFilterTextBox.Text = _filter.Email ?? "";
        DepartmentFilterComboBox.DataSource = DepartmentsService.Instance.FindAll();
        if (_filter.DepartmentId != null)
        {
            Console.WriteLine(_filter.DepartmentId);
            DepartmentFilterComboBox.SelectedItem = DepartmentsService.Instance.FindById( (long) _filter.DepartmentId);
        }
        else
        {
            DepartmentFilterComboBox.SelectedIndex = -1;
            DepartmentFilterComboBox.Text = "Select a filter department";
        }
    }

    // Input: fara parametri.
    // Return: void.
    // Incarca angajatii (cu filtru) in DataTable.
    // Leaga DataTable-ul la DataGridView.
    private void LoadData()
    {
        foreach (var employee in EmployeesService.Instance.FindAll(_filter))
        {
            _table.Rows.Add(employee.Id,
                employee.FirstName,
                employee.LastName,
                employee.Email,
                // DepartmentsService.Instance.FindById(employee.DepartmentId)?.Name
                employee.Department.Name
                );
        }
        
        EmployeesGridView.DataSource = _table;
    }
    
    // Input: fara parametri.
    // Return: void.
    // Aplica stilul grilei si setarile de selectie.
    // Ascunde zona CRUD cand formularul este in mod read-only.
    private void LoadStyle()
    {
        ResetDepartmentComboBox();
        EmployeesGridView.SelectionMode = DataGridViewSelectionMode.FullRowSelect;
        EmployeesGridView.MultiSelect = false;
        EmployeesGridView.ReadOnly = true;
        
        if(!_crudActive)
            tableLayoutPanel2.Visible = false;
    }

    // Input: object sender, EventArgs e.
    // Return: void.
    // Valideaza campurile si adauga un angajat nou.
    // Reincarca datele dupa inserare.
    private void AddEmployeeBtn_Click(object sender, EventArgs e)
    {
        if (!ValidateInput())
            return;
            
        var firstName = FirstNameTextBox.Text;
        var lastName = LastNameTextBox.Text;
        var email = EmailTextBox.Text;
        var department = ((Department) DepartmentComboBox.SelectedItem!).Id;
        
        EmployeesService.Instance.Save(firstName, lastName, email, department);

        ClearInputs();
        refreshDataBtn_Click(null, null);
    }

    // Input: fara parametri.
    // Return: bool.
    // Verifica daca input-ul este complet si corect.
    // Afiseaza mesaje de eroare cand validarea esueaza.
    private bool ValidateInput()
    {
        var message = "Some fields are missing:\n\n";
        
        if(string.IsNullOrEmpty(FirstNameTextBox.Text))
            message += "- First name\n";
        if(string.IsNullOrEmpty(LastNameTextBox.Text))            
            message += "- Last name\n";
        if(string.IsNullOrEmpty(EmailTextBox.Text))
            message += "- Email\n";
        if(DepartmentComboBox.SelectedIndex == -1)
            message += "- Department\n";

        if (message != "Some fields are missing:\n\n")
        {
            MessageBox.Show(message);
            return false;
        }
        
        message = "Some fields are not valid:\n\n";
        
        if(!Validator.ContainsOnlyLetters(FirstNameTextBox.Text))
            message += "- First name should contain only letters\n";
        
        if(!Validator.ContainsOnlyLetters(LastNameTextBox.Text))
            message += "- Last name should contain only letters\n";
        
        Regex emailRegex = new(
            @"^[A-Za-z0-9.!#$%&'*+/=?^_`{|}~-]+@[A-Za-z0-9-]+(?:\.[A-Za-z0-9-]+)+$",
        RegexOptions.Compiled);
        
        if (string.IsNullOrWhiteSpace(EmailTextBox.Text) || !emailRegex.IsMatch(EmailTextBox.Text))
            message += "- Email format is invalid\n";
        
        if (message != "Some fields are not valid:\n\n")
        {   
            MessageBox.Show(message);
            return false;
        }

        return true;
    }

    // Input: fara parametri.
    // Return: void.
    // Goleste campurile de editare pentru angajat.
    // Reseteaza si selectia departamentului.
    private void ClearInputs()
    {
        FirstNameTextBox.Text = "";
        LastNameTextBox.Text = "";
        EmailTextBox.Text = "";
        ResetDepartmentComboBox();
    }

    // Input: fara parametri.
    // Return: void.
    // Scoate selectia curenta din DepartmentComboBox.
    // Seteaza textul placeholder pentru utilizator.
    private void ResetDepartmentComboBox()
    {
        DepartmentComboBox.SelectedIndex = -1;
        DepartmentComboBox.Text = "Select a department";
    }

    // Input: object sender, EventArgs e.
    // Return: void.
    // Reincarca datele din grila.
    // Curata tabelul local inainte de refill.
    private void refreshDataBtn_Click(object sender, EventArgs e)
    {
        _table.Rows.Clear();
        LoadData();
    }

    // Input: object sender, EventArgs e.
    // Return: void.
    // Ruleaza cand se schimba selectia departamentului.
    // Momentan nu are logica suplimentara.
    private void DepartmentComboBox_SelectedIndexChanged(object sender, EventArgs e)
    {
    }

    // Input: object sender, EventArgs e.
    // Return: void.
    // Cere confirmare si sterge angajatul selectat.
    // Reincarca grila dupa stergere.
    private void deleteEmployeeBtn_Click(object sender, EventArgs e)
    {
        if(EmployeesGridView.SelectedRows[0].Cells[0].Value == null ||
           EmployeesGridView.SelectedRows[0].Cells[0].Value == DBNull.Value)
        {
            MessageBox.Show("Please select an employee to delete.");
            return;
        }
        
        var result = MessageBox.Show(
            "Are you sure you want to delete the selected employee?",
            "Confirm Delete",
            MessageBoxButtons.YesNo,
            MessageBoxIcon.Warning,
            MessageBoxDefaultButton.Button2
        );

        if (result == DialogResult.No)
            return;

        var id = (long) EmployeesGridView.SelectedRows[0].Cells[0].Value!;
        EmployeesService.Instance.DeleteById(id);
        refreshDataBtn_Click(null, null);
    }

    // Input: object sender, EventArgs e.
    // Return: void.
    // Valideaza campurile si actualizeaza angajatul selectat.
    // Reincarca datele dupa update.
    private void updateEmployeeBtn_Click(object sender, EventArgs e)
    {
        if (!ValidateInput())
            return;
        
        if(EmployeesGridView.SelectedRows[0].Cells[0].Value == null ||
           EmployeesGridView.SelectedRows[0].Cells[0].Value == DBNull.Value)
        {
            MessageBox.Show("Please select an employee to delete.");
            return;
        }
        
        var id = (long) EmployeesGridView.SelectedRows[0].Cells[0].Value!;
            
        var firstName = FirstNameTextBox.Text;
        var lastName = LastNameTextBox.Text;
        var email = EmailTextBox.Text;
        var department = ((Department) DepartmentComboBox.SelectedItem!).Id;
        
        EmployeesService.Instance.Update(id, firstName, lastName, email, department);

        ClearInputs();
        refreshDataBtn_Click(null, null);
    }
    
    // Input: object sender, EventArgs e.
    // Return: void.
    // Preia valorile filtrelor din UI.
    // Reincarca grila cu datele filtrate.
    private void filterEmployeeBtn_Click(object sender, EventArgs e)
    {
        _filter.FirstName = FirstNameFilterTextBox.Text ?? "";
        _filter.LastName = LastNameFilterTextBox.Text ?? "";
        _filter.Email = EmailFilterTextBox.Text ?? "";
        if (DepartmentFilterComboBox.SelectedIndex != -1)
            _filter.DepartmentId = ((Department)DepartmentFilterComboBox.SelectedItem).Id;
        else
            _filter.DepartmentId = null;
        
        refreshDataBtn_Click(null, null);
    }

    // Input: object sender, EventArgs e.
    // Return: void.
    // Reseteaza toate campurile de filtrare.
    // Reaplica filtrul gol pentru a afisa toate datele.
    private void ResetFilterBtn_Click(object sender, EventArgs e)
    {
        FirstNameFilterTextBox.Text = "";
        LastNameFilterTextBox.Text = "";
        EmailFilterTextBox.Text = "";
        DepartmentFilterComboBox.SelectedIndex = -1;
        DepartmentFilterComboBox.Text = "Select a filter department";
        filterEmployeeBtn_Click(null, null);
    }
}