package org.plugface.core.factory;

import org.plugface.core.PluginContext;
import org.plugface.core.PluginManager;
import org.plugface.core.impl.DefaultPluginContext;
import org.plugface.core.impl.DefaultPluginManager;
import org.plugface.core.internal.AnnotationProcessor;
import org.plugface.core.internal.DependencyResolver;

public class PluginManagerFactory {

    public static PluginManager createDefaultPluginManager() {
        final DefaultPluginContext context = new DefaultPluginContext();
        final AnnotationProcessor processor = new AnnotationProcessor();
        final DependencyResolver resolver = new DependencyResolver(processor);
        return createPluginManager(context, processor, resolver);
    }

    public static PluginManager createPluginManager(PluginContext context, AnnotationProcessor processor, DependencyResolver resolver) {
        return new DefaultPluginManager(context, processor, resolver);
    }
}
