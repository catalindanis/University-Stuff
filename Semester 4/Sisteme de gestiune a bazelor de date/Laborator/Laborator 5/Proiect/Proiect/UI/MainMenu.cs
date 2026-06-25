namespace Proiect;

public partial class MainMenu : Form
{
    public MainMenu()
    {
        InitializeComponent();
    }

    private void Form1_Load(object sender, EventArgs e)
    {
    }

    private void button2_Click(object sender, EventArgs e)
    {
        var departmentsForm = new DepartmentsForm();
        departmentsForm.Show();
    }

    private void tableLayoutPanel1_Paint(object sender, PaintEventArgs e)
    {
    }

    private void employeesBtn_Click(object sender, EventArgs e)
    {
        var employeesForm = new EmployeesForm();
        employeesForm.Show();
    }

    private void adminBtn_Click(object sender, EventArgs e)
    {
        var adminForm = new AdminForm();
        adminForm.Show();
    }
}