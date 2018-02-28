package org.plugface.core.impl;

final class PluginDependency {
    private final Class<?> dependencyClass;
    private final boolean optional;

    public PluginDependency(Class<?> dependencyClass, boolean optional) {
        this.dependencyClass = dependencyClass;
        this.optional = optional;
    }

    Class<?> getDependencyClass() {
        return dependencyClass;
    }

    boolean isOptional() {
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
