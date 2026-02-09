namespace taskrunner.runner;

public interface ITaskRunner
{
    void ExecuteOneTask();   // execută un task din colecția de task-uri
    void ExecuteAll();       // execută toate task-urile
    void AddTask(Task t);    // adaugă un task în colecția de task-uri
    bool HasTask();          // verifică dacă mai sunt task-uri de executat
}