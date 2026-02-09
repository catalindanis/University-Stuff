package container;

import utils.ContainerStrategy;

public interface ContainerFactory {
    public Container createContainer(ContainerStrategy strategy);
}
