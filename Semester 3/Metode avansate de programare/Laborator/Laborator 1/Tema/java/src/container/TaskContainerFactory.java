package container;

import utils.ContainerStrategy;

public class TaskContainerFactory implements ContainerFactory{
    private static ContainerFactory factory;

    private TaskContainerFactory() {}

    public static ContainerFactory getInstance() {
        if(factory == null)
            factory = new TaskContainerFactory();

        return factory;
    }

    @Override
    public Container createContainer(ContainerStrategy strategy) {
        switch (strategy) {
            case LIFO -> { return new StackContainer(); }
            case FIFO -> { return new QueueContainer(); }
            default -> { return null; }
        }
    }
}
