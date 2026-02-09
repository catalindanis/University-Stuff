package org.example.runner;

import org.example.container.Container;
import org.example.utils.Strategy;
import org.example.model.Task;
import org.example.container.TaskContainerFactory;

public class StrategyTaskRunner implements TaskRunner {
    private Container container;

    public StrategyTaskRunner(Strategy strategy) {
        this.container = TaskContainerFactory.getInstance().createContainer(strategy);
    }

    @Override
    public void executeOneTask() {
        if(!this.container.isEmpty()) {
            this.container.remove().execute();
        }
    }

    @Override
    public void executeAll() {
        while(this.hasTask())
            this.executeOneTask();
    }

    @Override
    public void addTask(Task task) {
        this.container.add(task);
    }

    @Override
    public boolean hasTask() {
        return !this.container.isEmpty();
    }
}
