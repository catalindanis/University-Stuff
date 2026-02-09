package container;

import models.Task;

public interface Container {
    void add(Task task);
    Task remove();
    int size();
    boolean isEmpty();
}
