package runner;

import models.Task;

public interface TaskRunner {
    void addTask(Task task);
    void executeOneTask();
    void executeAll();
    boolean hasTask();
}
