using System.ComponentModel;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Runtime.CompilerServices;
using Microsoft.EntityFrameworkCore;

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
    [MaxLength(10)]
    [Column("phone_number")]
    public string? PhoneNumber { get; set; }
    [Column("department_id")]
    public long DepartmentId { get; set; } = departmentId;
    [Column("salary")]
    [Precision(18, 2)]
    public decimal Salary { get; set; } = 50000;
    [ForeignKey("DepartmentId")]
    public virtual Department Department { get; set; }
    [Timestamp]
    public byte[] RowVersion { get; set; }
    [Column("is_deleted")]
    public bool IsDeleted { get; set; } = false;
}