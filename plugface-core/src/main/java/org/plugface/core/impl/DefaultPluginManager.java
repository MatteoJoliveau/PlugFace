package org.plugface.core.impl;

import org.plugface.core.MissingDependencyException;
import org.plugface.core.PluginContext;
import org.plugface.core.PluginManager;
import org.plugface.core.PluginSource;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class DefaultPluginManager implements PluginManager {

    private final PluginContext context;

    public DefaultPluginManager(PluginContext context) {
        this.context = context;
    }

    @Override
    public <T> void register(T plugin) {
        context.addPlugin(plugin);
    }

    @Override
    public <T> T getPlugin(String name) {
        return context.getPlugin(name);
    }

    @Override
    public <T> T getPlugin(Class<T> pluginClass) {
        return context.getPlugin(pluginClass);
    }

    @Override
    public <T> T removePlugin(String name) {
        return context.removePlugin(name);
    }

    @Override
    public <T> T removePlugin(T plugin) {
        return context.removePlugin(plugin);
    }

    @Override
    public  Collection<Object> loadPlugins(PluginSource source) throws IllegalAccessException, InstantiationException {
        final Collection<Class<?>> pluginClasses = source.load();
        final Collection<Object> loaded = new ArrayList<>();

        if (pluginClasses.isEmpty()) {
            return loaded;
        }

        for (Class<?> pluginClass : pluginClasses) {
            if (context.hasPlugin(pluginClass)) {
                continue;
            }
            final boolean hasDependencies = PluginUtils.hasDependencies(pluginClass);
            if (hasDependencies) {
                final Collection<PluginDependency> dependencies = PluginUtils.getDependencies(pluginClass);
                handleDependencies(dependencies, pluginClasses);
                final List<Class<?>> parameterTypes = new ArrayList<>();
                final List<Object> parameters = new ArrayList<>();

                for (PluginDependency dependency : dependencies) {
                    final Class<?> dependencyClass = dependency.getDependencyClass();
                    parameterTypes.add(dependencyClass);
                    final boolean isOptional = dependency.isOptional();
                    if (!isOptional && !context.hasPlugin(dependencyClass)) {
                        throw new MissingDependencyException("Plugin %s has a dependency on %s but the latter could not be found", pluginClass.getSimpleName(), dependencyClass.getSimpleName());
                    } else if (isOptional && !context.hasPlugin(dependencyClass)) {
                        parameters.add(null);
                    } else if (context.hasPlugin(dependencyClass)) {
                        parameters.add(context.getPlugin(dependencyClass));
                    }
                }
                try {
                    final Constructor<?> constructor = pluginClass.getConstructor((Class<?>[]) parameterTypes.toArray());
                    final Object plugin = constructor.newInstance(parameters.toArray());
                    context.addPlugin(plugin);
                } catch (NoSuchMethodException | InvocationTargetException e) {
                    e.printStackTrace(); // TODO DEAL WITH THEM
                }
            } else {
                final Object plugin = pluginClass.newInstance();
                register(plugin);
                loaded.add(plugin);
            }
        }
        return loaded;
    }

    private void handleDependencies(Collection<PluginDependency> dependencies, Collection<Class<?>> pluginClasses) throws IllegalAccessException, InstantiationException {
        for (PluginDependency dependency : dependencies) {
            final Class<?> depClass = dependency.getDependencyClass();
            if (!context.hasPlugin(depClass) && pluginClasses.contains(depClass)) {
                if (PluginUtils.hasDependencies(depClass)) {
                    handleDependencies(PluginUtils.getDependencies(depClass), pluginClasses);
                } else {
                    final Object depInst = depClass.newInstance();
                    context.addPlugin(depInst);
                }
            }

        }
    }

}
