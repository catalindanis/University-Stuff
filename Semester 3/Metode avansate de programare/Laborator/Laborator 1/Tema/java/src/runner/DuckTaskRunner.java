package runner;

import container.Container;
import models.Task;

public class DuckTaskRunner implements TaskRunner {
    private final Container container;

    public DuckTaskRunner(Container container) {
        this.container = container;
    }

    @Override
    public void addTask(Task task) { this.container.add(task); }

    @Override
    public void executeOneTask() { this.container.remove().execute(); }

    @Override
    public void executeAll() {
        while(!this.container.isEmpty()) {
            this.container.remove().execute();
        }
    }

    @Override
    public boolean hasTask() { return !this.container.isEmpty(); }
}
