package org.example.container;

import org.example.model.Task;

public interface Container {
    Task remove();
    void add(Task task);
    int size();
    boolean isEmpty();
}
