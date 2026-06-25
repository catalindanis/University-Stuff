namespace Proiect.Domain;

public class Employee(long id, string firstName, string lastName, string email, long departmentId)
    : IEntity<long>
{
    public long Id { get; set; } = id;
    public string FirstName { get; set; } = firstName;
    public string LastName { get; set; } = lastName;
    public string Email { get; set; } = email;
    public long DepartmentId { get; set; } = departmentId;
}