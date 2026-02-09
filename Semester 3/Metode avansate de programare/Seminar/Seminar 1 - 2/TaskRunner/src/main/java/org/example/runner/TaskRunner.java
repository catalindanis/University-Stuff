package org.example.runner;

import org.example.model.Task;

public interface TaskRunner {
    void executeOneTask();

    void executeAll();

    void addTask(Task task);

    boolean hasTask();
}
