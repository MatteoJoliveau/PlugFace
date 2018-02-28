package org.plugface.core.impl;

import org.plugface.core.MissingDependencyException;
import org.plugface.core.PluginContext;
import org.plugface.core.PluginManager;
import org.plugface.core.PluginSource;
import org.plugface.core.internal.AnnotationProcessor;
import org.plugface.core.internal.DependencyResolver;
import org.plugface.core.internal.tree.DependencyNode;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class DefaultPluginManager implements PluginManager {

    private final PluginContext context;
    private final AnnotationProcessor annotationProcessor;
    private final DependencyResolver dependencyResolver;

    public DefaultPluginManager(PluginContext context, AnnotationProcessor annotationProcessor, DependencyResolver dependencyResolver) {
        this.context = context;
        this.annotationProcessor = annotationProcessor;
        this.dependencyResolver = dependencyResolver;
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
    public Collection<Object> loadPlugins(PluginSource source) throws Exception {
        final Collection<Class<?>> pluginClasses = source.load();
        final Collection<Object> loaded = new ArrayList<>();

        if (pluginClasses.isEmpty()) {
            return loaded;
        }

        for (Class<?> pluginClass : pluginClasses) {
            if (context.hasPlugin(pluginClass)) {
                pluginClasses.remove(pluginClass);
            }
        }

        Collection<DependencyNode> nodes = dependencyResolver.resolve(pluginClasses);
        createShallowPlugins(nodes, loaded, pluginClasses);
        createPlugins(nodes, loaded, pluginClasses);
        return loaded;
    }

    private void createShallowPlugins(Collection<DependencyNode> nodes, Collection<Object> loaded, Collection<Class<?>> available) throws IllegalAccessException, InstantiationException {
        final ArrayList<DependencyNode> removed = new ArrayList<>();
        for (DependencyNode node : nodes) {
            if (node.getDependencies().isEmpty()) {
                removed.add(node);
                if (!context.hasPlugin(node.getName()) && available.contains(node.getDependencyClass())) {
                    final Object plugin = node.getDependencyClass().newInstance();
                    context.addPlugin(plugin);
                    loaded.add(plugin);
                }
            }
        }
        nodes.removeAll(removed);
    }

    private void createPlugins(Collection<DependencyNode> nodes, Collection<Object> loaded, Collection<Class<?>> available) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        for (DependencyNode node : nodes) {
            createPlugin(node, loaded, available);
        }
    }

    private void createPlugin(DependencyNode node, Collection<Object> loaded, Collection<Class<?>> available) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        if (!context.hasPlugin(node.getName())) {
            final List<DependencyNode> dependencies = node.getDependencies();
            if (dependencies.isEmpty()) {
                final Object plugin = node.getDependencyClass().newInstance();
                context.addPlugin(plugin);
                loaded.add(plugin);
            } else {

                final Class<?> nodeClass = node.getDependencyClass();
                final HashMap<Class<?>, Object> params = new HashMap<>();
                for (DependencyNode dependency : dependencies) {
                    final Class<?> dependencyClass = dependency.getDependencyClass();
                    if (!available.contains(dependencyClass) && !context.hasPlugin(dependency.getName())) {
                        if (!dependency.isOptional()) {
                            throw new MissingDependencyException("Plugin declared %s as dependency by %s but none was found.", nodeClass.getSimpleName(), dependencyClass.getSimpleName());
                        } else {
                            params.put(dependencyClass, null);
                        }
                    } else if (context.hasPlugin(dependency.getName())) {
                        final Object p = context.getPlugin(dependency.getName());
                        params.put(dependencyClass, p);
                    } else {
                        createPlugin(dependency, loaded, available);
                    }
                }
                final Constructor<?>[] constructors = nodeClass.getConstructors();
                for (Constructor<?> constructor : constructors) {
                    final List<Object> p = new ArrayList<>();
                    final Class<?>[] parameterTypes = constructor.getParameterTypes();
                    for (Class<?> parameterType : parameterTypes) {
                        if (params.containsKey(parameterType)) {
                            final Object dep = params.get(parameterType);
                            p.add(dep);
                        }
                    }
                    if (parameterTypes.length == p.size()) {
                        final Object plugin = constructor.newInstance(p.toArray());
                        context.addPlugin(plugin);
                        loaded.add(plugin);
                        break;
                    }
                }

            }
        }
    }

}
