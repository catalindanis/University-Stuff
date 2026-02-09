package org.example.container;

import org.example.utils.Strategy;

public interface ContainerFactory {
    Container createContainer(Strategy strategy);
}
