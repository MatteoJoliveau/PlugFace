package org.plugface.core.internal.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DependencyNode {
    private final String name;
    private final Class<?> selfClass;
    private final boolean optional;
    private final List<DependencyNode> dependencies = new ArrayList<>(0);

    public DependencyNode(String name, Class<?> selfClass, boolean optional) {
        this.name = name;
        this.selfClass = selfClass;
        this.optional = optional;
    }

    public String getName() {
        return name;
    }

    public Class<?> getDependencyClass() {
        return selfClass;
    }

    public DependencyNode addDependency(DependencyNode dependency) {
        dependencies.add(dependency);
        return this;
    }

    public DependencyNode addDependencies(Collection<DependencyNode> dependencies) {
        this.dependencies.addAll(dependencies);
        return this;
    }

    public List<DependencyNode> getDependencies() {
        return dependencies;
    }

    public boolean isOptional() {
        return optional;
    }

    @Override
    public String toString() {
        return "DependencyNode{" +
                "name='" + name + '\'' +
                ", selfClass=" + selfClass +
                ", optional=" + optional +
                ", dependencies=" + dependencies +
                '}';
    }
}
