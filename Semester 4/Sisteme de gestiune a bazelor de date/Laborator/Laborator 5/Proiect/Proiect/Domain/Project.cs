using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Proiect.Domain;

[Table("projects")]
public class Project(string name, string description, long departmentId) : IEntity<long>
{
    [Key]
    [Column("id")]
    [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
    public long Id { get; set; } = 0;
    [Required]
    [MaxLength(50)]
    [Column("name")]
    public string Name { get; set; } = name;
    [Required]
    [MaxLength(250)]
    [Column("description")]
    public string Description { get; set; } = description;
    [Column("department_id")]
    public long DepartmentId { get; set; } = departmentId;
    [ForeignKey("DepartmentId")]
    public virtual Department Department { get; set; }
}