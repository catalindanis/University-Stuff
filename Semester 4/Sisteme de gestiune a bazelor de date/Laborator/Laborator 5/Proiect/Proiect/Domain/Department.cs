using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Proiect.Domain;

// Reprezinta un departament din companie.
[Table("departments")]
public class Department(string name, string location) : IEntity<long>
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
    [MaxLength(50)]
    [Column("location")]
    public string Location { get; set; } = location;
    
    public virtual ICollection<Employee> Employees { get; set; } = new List<Employee>();

    public virtual ICollection<Project> Projects { get; set; } = new List<Project>();
    // Input: object? obj.
    // Return: bool.
    // Verifica egalitatea cu alt obiect.
    // Incearca mai intai cast la Department.
    public override bool Equals(object? obj)
    {
        return Equals(obj as Department);
    }

    // Input: Department? other.
    // Return: bool.
    // Compara campurile Id, Name si Location.
    // Returneaza false daca obiectul este null.
    private bool Equals(Department? other)
    {
        return other != null &&
               Id == other.Id &&
               Name.Equals(other.Name) &&
               Location.Equals(other.Location);
    }

    // Input: fara parametri.
    // Return: int.
    // Genereaza hash code pe baza campurilor principale.
    // Este folosit in colectii de tip hash.
    public override int GetHashCode() => HashCode.Combine(Id, Name, Location);

    // Input: fara parametri.
    // Return: string.
    // Returneaza un text scurt pentru afisare in UI.
    // Formatul este "Nume (Locatie)".
    public override string ToString()
    {
        return $"{Name} ({Location})";
    }
}