package org.plugface.core.internal;

import org.plugface.core.CircularDependencyException;
import org.plugface.core.internal.tree.DependencyNode;

import java.util.*;

public class DependencyResolver {
    private final AnnotationProcessor processor;

    public DependencyResolver(AnnotationProcessor processor) {
        this.processor = processor;
    }

    public Collection<DependencyNode> resolve(Collection<Class<?>> pluginClasses) {
        Set<DependencyNode> nodes = new HashSet<>();
        Set<String> seen = new HashSet<>();
        for (Class<?> pluginClass : pluginClasses) {
            DependencyNode node = new DependencyNode(AnnotationProcessor.getPluginName(pluginClass), pluginClass, false);
            HashSet<DependencyNode> resolved = new HashSet<>();
            HashSet<DependencyNode> unresolved = new HashSet<>();
            resolve(node, resolved, unresolved);
            for (DependencyNode n : resolved) {
                if (!seen.contains(n.getName())) {
                    seen.add(n.getName());
                    nodes.add(n);
                }
            }
        }

        return nodes;
    }

    private void resolve(DependencyNode node, Set<DependencyNode> resolved, Set<DependencyNode> unresolved) {
        unresolved.add(node);
        Collection<DependencyNode> dependencies = processor.getDependencies(node.getDependencyClass());
        for (DependencyNode edge : dependencies) {
            if (unresolved.contains(edge)) {
                throw new CircularDependencyException("Circular reference detected: %s -> %s", node.getName(), edge.getName());
            }
            if (!resolved.contains(edge)) {
                resolve(edge, resolved, unresolved);
            }
            node.addDependency(edge);
        }
        resolved.add(node);
        unresolved.remove(node);
    }
}
