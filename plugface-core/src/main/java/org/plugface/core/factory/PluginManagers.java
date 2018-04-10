package org.plugface.core.factory;

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

import org.plugface.core.PluginContext;
import org.plugface.core.PluginManager;
import org.plugface.core.impl.DefaultPluginContext;
import org.plugface.core.impl.DefaultPluginManager;
import org.plugface.core.internal.AnnotationProcessor;
import org.plugface.core.internal.DependencyResolver;

public class PluginManagers {

    /**
     * A default {@link PluginManager} configured with internal dependencies for {@link PluginContext}, {@link AnnotationProcessor} and {@link DependencyResolver}
     *
     * @return a fully configured {@link DefaultPluginManager}
     */
    public static PluginManager defaultPluginManager() {
        final DefaultPluginContext context = new DefaultPluginContext();
        final AnnotationProcessor processor = new AnnotationProcessor();
        final DependencyResolver resolver = new DependencyResolver(processor);
        return newPluginManager(context, processor, resolver);
    }

    public static PluginManager newPluginManager(PluginContext context, AnnotationProcessor processor, DependencyResolver resolver) {
        return new DefaultPluginManager(context, processor, resolver);
    }
}
