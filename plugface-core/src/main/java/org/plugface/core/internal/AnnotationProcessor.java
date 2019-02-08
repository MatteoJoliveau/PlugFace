package org.plugface.core.internal;

/*-
 * #%L
 * PlugFace :: Core
 * %%
 * Copyright (C) 2017 - 2018 PlugFace
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import org.plugface.core.annotations.Plugin;
import org.plugface.core.internal.di.Node;

import javax.inject.Inject;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Internal class used to process the {@link Plugin} annotation
 */
public class AnnotationProcessor {

    public static <T> String getPluginName(T plugin) {
        return getPluginName(plugin.getClass());
    }

    public static <T> String getPluginName(Class<T> plugin) {
        final Plugin annotation = getPluginAnnotation(plugin);
        final String name = annotation.name();
        return "".equalsIgnoreCase(name) ? null : name;
    }

    public boolean hasDependencies(Class<?> pluginClass) {
        getPluginAnnotation(pluginClass);

        final Constructor<?>[] constructors = pluginClass.getConstructors();

        if (constructors.length == 0) return false;

        for (Constructor<?> constructor : constructors) {
            final Inject annotation = constructor.getAnnotation(Inject.class);
            if (annotation != null) {
                return true;
            }
        }
        return false;
    }


    public Collection<Node<?>> getDependencies(Class<?> pluginClass) {
        final Constructor<?>[] constructors = pluginClass.getConstructors();
        if (constructors.length == 0) {
            throw new IllegalArgumentException(String.format("Class %s doesn't have a public constructor. Class: %s", pluginClass.getSimpleName(), pluginClass.getName()));
        }

        for (Constructor<?> constructor : constructors) {
            final Inject annotation = constructor.getAnnotation(Inject.class);
            if (annotation == null) {
                continue;
            }
            final Collection<Node<?>> dependencies = new ArrayList<>();
            final Class<?>[] parameterTypes = constructor.getParameterTypes();
            for (final Class<?> param : parameterTypes) {
                dependencies.add(new Node<>(param));

            }
            return dependencies;

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
