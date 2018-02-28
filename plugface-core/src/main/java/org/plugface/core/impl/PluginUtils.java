package org.plugface.core.impl;

import org.plugface.core.annotations.Plugin;
import org.plugface.core.annotations.Require;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;

class PluginUtils {

    static <T> String getPluginName(T plugin) {
        final Plugin annotation = getPluginAnnotation(plugin.getClass());
        final String name = annotation.value();
        return "".equalsIgnoreCase(name) ? null : name;
    }

    static boolean hasDependencies(Class<?> pluginClass) {
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

    static Collection<PluginDependency> getDependencies(Class<?> pluginClass) {
        getPluginAnnotation(pluginClass);
        final Constructor<?>[] constructors = pluginClass.getConstructors();
        if (constructors.length == 0) {
            throw new IllegalArgumentException(String.format("Class %s doesn't have a public constructor. Class: %s", pluginClass.getSimpleName(), pluginClass.getName()));
        }

        for (Constructor<?> constructor : constructors) {
            final Collection<PluginDependency> dependencies = new ArrayList<>();
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
                        final PluginDependency dep = new PluginDependency(param, ann.optional());
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
