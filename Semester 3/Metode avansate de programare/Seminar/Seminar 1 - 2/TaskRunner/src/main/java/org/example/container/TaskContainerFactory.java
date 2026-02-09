package org.example.container;

import org.example.utils.Strategy;

public class TaskContainerFactory implements ContainerFactory {

    private static TaskContainerFactory instance = null;
//    private static TaskContainerFactory instance = new TaskContainerFactory();
    private TaskContainerFactory() {}

    public static TaskContainerFactory getInstance() {
        if(instance == null)
            instance = new TaskContainerFactory();
        return instance;
    }

    @Override
    public Container createContainer(Strategy strategy) {
        if(strategy == Strategy.LIFO)
            return new StackContainer();
        return null;
    }
}
