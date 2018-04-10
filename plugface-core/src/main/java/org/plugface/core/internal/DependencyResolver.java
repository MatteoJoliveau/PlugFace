package org.plugface.core.internal;

/*-
 * #%L
 * PlugFace :: Core
 * %%
 * Copyright (C) 2017 - 2018 PlugFace
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
 * #L%
 */

import org.plugface.core.internal.di.Graph;
import org.plugface.core.internal.di.Node;

import java.util.Collection;

/**
 * Internal class used to create a dependency graph
 * for plugin dependency injection.
 */
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
