using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Proiect.Domain;

[Table("audit_logs")]
public class AuditLog(string entityName, long entityId, string deletedBy, string deleteType)
    : IEntity<long>
{
    [Key]
    [Column("id")]
    [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
    public long Id { get; set; } = 0;

    [Required]
    [MaxLength(100)]
    [Column("entity_name")]
    public string EntityName { get; set; } = entityName;

    [Column("entity_id")]
    public long EntityId { get; set; } = entityId;

    [Required]
    [MaxLength(100)]
    [Column("deleted_by")]
    public string DeletedBy { get; set; } = deletedBy;

    [Required]
    [MaxLength(20)]
    [Column("delete_type")]
    public string DeleteType { get; set; } = deleteType;

    [Column("deleted_at")]
    public DateTime DeletedAt { get; set; } = DateTime.UtcNow;
}

