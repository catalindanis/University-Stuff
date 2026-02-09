namespace taskrunner;

public abstract class Task
{
    public string TaskId { get; set; }
    public string Description { get; set; }

    protected Task(string taskId, string description)
    {
        TaskId = taskId;
        Description = description;
    }

    public override string ToString()
    {
        return $"{TaskId} {Description}";
    }

    public override bool Equals(object obj)
    {
        if (ReferenceEquals(this, obj))
            return true;

        if (obj is not Task other)
            return false;

        return string.Equals(TaskId, other.TaskId) &&
               string.Equals(Description, other.Description);
    }

    public override int GetHashCode()
    {
        return HashCode.Combine(TaskId, Description);
    }

    public abstract void Execute();
}