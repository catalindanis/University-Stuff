package org.example.container;

import org.example.model.Task;

public class StackContainer implements Container {

    private Task[] tasks;
    private int size;

    public StackContainer() {
        this.tasks = new Task[10];
        this.size = 0;
    }

    @Override
    public Task remove() {
        return this.tasks[--this.size];
    }

    private void resize() {
        Task[] newTasks = new Task[this.tasks.length * 2];
        for(int i = 0; i < this.size; i++) {
            newTasks[i] = this.tasks[i];
        }
        this.tasks = newTasks;
    }

    @Override
    public void add(Task task) {
        if(this.size == this.tasks.length)
            this.resize();
        this.tasks[this.size++] = task;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }
}
