using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Proiect.Domain;

[Table("employees")]
public class Employee(string firstName, string lastName, string email, long departmentId)
    : IEntity<long>
{
    [Key]
    [Column("id")]
    [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
    public long Id { get; set; } = 0;
    
    [Required]
    [MaxLength(50)]
    [Column("first_name")]
    public string FirstName { get; set; } = firstName;
    [Required]
    [MaxLength(50)]
    [Column("last_name")]
    public string LastName { get; set; } = lastName;
    [Required]
    [MaxLength(100)]
    [Column("email")]
    public string Email { get; set; } = email;
    [Column("department_id")]
    public long DepartmentId { get; set; } = departmentId;
    [Column("salary")]
    public decimal Salary { get; set; } = 50000;
    [ForeignKey("DepartmentId")]
    public virtual Department Department { get; set; }
}