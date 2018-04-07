package org.plugface.core.factory;

import org.plugface.core.PluginContext;
import org.plugface.core.PluginManager;
import org.plugface.core.impl.DefaultPluginContext;
import org.plugface.core.impl.DefaultPluginManager;
import org.plugface.core.internal.AnnotationProcessor;
import org.plugface.core.internal.DependencyResolver;

public class PluginManagers {

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
