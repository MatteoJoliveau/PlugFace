package org.plugface.core.internal;

import org.plugface.core.internal.di.Graph;
import org.plugface.core.internal.di.Node;

import java.util.ArrayList;
import java.util.Collection;

public class DependencyResolver {
    private final AnnotationProcessor processor;

    public DependencyResolver(AnnotationProcessor processor) {
        this.processor = processor;
    }

    public Collection<Node<?>> resolve(Collection<Class<?>> pluginClasses) {

        final Graph graph = new Graph();
        for (Class<?> pluginClass : pluginClasses) {
            final Collection<Node<?>> deps = processor.getDependencies(pluginClass);
            graph.addEdges(new Node<>(pluginClass), deps);
        }

        return graph.resolve();
    }
}
