package org.plugface.core.internal;

import org.plugface.core.annotations.Plugin;
import org.plugface.core.annotations.Require;
import org.plugface.core.internal.tree.DependencyNode;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;

public class AnnotationProcessor {

    public static <T> String getPluginName(T plugin) {
        return getPluginName(plugin.getClass());
    }

    public static <T> String getPluginName(Class<T> plugin) {
        final Plugin annotation = getPluginAnnotation(plugin);
        final String name = annotation.value();
        return "".equalsIgnoreCase(name) ? null : name;
    }

    public boolean hasDependencies(Class<?> pluginClass) {
        getPluginAnnotation(pluginClass);

        final Constructor<?>[] constructors = pluginClass.getConstructors();

        if (constructors.length == 0) return false;

        for (Constructor<?> constructor : constructors) {
            final Annotation[][] parameterAnnotations = constructor.getParameterAnnotations();
            if (parameterAnnotations.length == 0) return false;

            for (Annotation[] annotations : parameterAnnotations) {
                if (annotations.length != 0) {
                    for (Annotation ann : annotations) {
                        if (Require.class.equals(ann.annotationType())) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public Collection<DependencyNode> getDependencies(Class<?> pluginClass) {
        final String name = getPluginName(pluginClass);
        final Constructor<?>[] constructors = pluginClass.getConstructors();
        if (constructors.length == 0) {
            throw new IllegalArgumentException(String.format("Class %s doesn't have a public constructor. Class: %s", pluginClass.getSimpleName(), pluginClass.getName()));
        }

        for (Constructor<?> constructor : constructors) {
            final Collection<DependencyNode> dependencies = new ArrayList<>();
            final Class<?>[] parameterTypes = constructor.getParameterTypes();
            final Annotation[][] parameterAnnotations = constructor.getParameterAnnotations();
            boolean hasRequire = false;
            for (int i = 0; i < parameterTypes.length; i++) {
                final Class<?> param = parameterTypes[i];
                final Annotation[] annotations = parameterAnnotations[i];
                for (Annotation annotation : annotations) {
                    if (Require.class.equals(annotation.annotationType())) {
                        hasRequire = true;
                        Require ann = (Require) annotation;
                        final DependencyNode dep = new DependencyNode(getPluginName(param), param, ann.optional());
                        dependencies.add(dep);

                    }
                }
            }
            if (hasRequire) {
                return dependencies;
            }

        }
        return new ArrayList<>();
    }

    private static Plugin getPluginAnnotation(Class<?> pluginClass) {
        final Plugin annotation = pluginClass.getAnnotation(Plugin.class);
        if (annotation == null) {
            throw new IllegalArgumentException(String.format("Class %s is not a valid @Plugin class. Class: %s", pluginClass.getSimpleName(), pluginClass.getName()));
        }
        return annotation;
    }
}
