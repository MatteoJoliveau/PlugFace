package org.plugface.core.internal;

public final class PluginDependency {
    private final Class<?> dependencyClass;
    private final boolean optional;

    public PluginDependency(Class<?> dependencyClass, boolean optional) {
        this.dependencyClass = dependencyClass;
        this.optional = optional;
    }

    public Class<?> getDependencyClass() {
        return dependencyClass;
    }

    public boolean isOptional() {
        return optional;
    }

    @Override
    public String toString() {
        return "PluginDependency{" +
                "dependencyClass=" + dependencyClass +
                ", optional=" + optional +
                '}';
    }
}
