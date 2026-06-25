using Proiect.Config;
using Proiect.Domain;

namespace Proiect.Utils;

public static class AuditTrailLogger
{
    public static string ResolveActorName()
    {
        return string.IsNullOrWhiteSpace(Environment.UserName) ? "unknown" : Environment.UserName;
    }

    public static void LogDelete(ApplicationDBContext context, string entityName, long entityId, string deleteType)
    {
        context.auditLogs.Add(new AuditLog(entityName, entityId, ResolveActorName(), deleteType));
    }
}

