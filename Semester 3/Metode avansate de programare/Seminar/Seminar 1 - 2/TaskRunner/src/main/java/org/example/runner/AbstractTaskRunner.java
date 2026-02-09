package org.example.runner;

import org.example.model.Task;

public abstract class AbstractTaskRunner implements TaskRunner{
    protected TaskRunner taskRunner;

    public AbstractTaskRunner(TaskRunner taskRunner) {
        this.taskRunner = taskRunner;
    }

    @Override
    public void executeOneTask() {
        taskRunner.executeOneTask();
    }

    @Override
    public void executeAll() {
        while(taskRunner.hasTask()) {
            executeOneTask();
        }
    }

    @Override
    public void addTask(Task task) {
        taskRunner.addTask(task);
    }

    @Override
    public boolean hasTask() {
        return taskRunner.hasTask();
    }
}
