using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Proiect.Domain;

[Table("customers")]
public class Customer(string firstName, string lastName, string email, string phoneNumber) : IEntity<long>
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
    public string? PhoneNumber { get; set; } = phoneNumber;
}