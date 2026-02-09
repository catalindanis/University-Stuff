package container;

import models.Task;

public class StackContainer implements Container {
    private Task[] tasks;
    private int size;

    public StackContainer() {
        tasks = new Task[10];
        size = 0;
    }

    @Override
    public void add(Task task) {
        if(size == tasks.length) resize();

        tasks[size++] = task;
    }

    private void resize() {
        Task[] newTasks = new Task[size * 2];

        for(int i = 0; i < size; i++) {
            newTasks[i] = tasks[i];
        }

        tasks = newTasks;
    }

    @Override
    public Task remove() { return this.tasks[--size]; }

    @Override
    public int size() { return size; }

    @Override
    public boolean isEmpty() { return size == 0; }
}
