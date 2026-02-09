package container;

import models.Task;

public class QueueContainer implements Container {
    private Task[] tasks;
    private int start, finish;

    public QueueContainer() {
        tasks = new Task[10];
        start = 0;
        finish = 0;
    }

    @Override
    public void add(Task task) {
        if(finish == tasks.length) resize();

        tasks[finish++] = task;
    }

    private void resize() {
        Task[] newTasks = new Task[finish * 2];

        for(int i = 0; i < finish; i++) {
            newTasks[i] = tasks[i];
        }

        tasks = newTasks;
    }

    @Override
    public Task remove() { return tasks[start++]; }

    @Override
    public int size() { return finish - start; }

    @Override
    public boolean isEmpty() { return finish - start == 0; }
}

