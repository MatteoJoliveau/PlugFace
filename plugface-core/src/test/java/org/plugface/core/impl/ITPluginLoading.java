package org.plugface.core.impl;

import org.junit.Before;
import org.junit.Test;
import org.plugface.core.MissingDependencyException;
import org.plugface.core.PluginSource;
import org.plugface.core.factory.PluginSources;
import org.plugface.core.internal.AnnotationProcessor;
import org.plugface.core.internal.DependencyResolver;
import org.plugface.core.plugins.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

public class ITPluginLoading {

    private DefaultPluginManager manager;

    @Before
    public void setUp() throws Exception {
        final DefaultPluginContext context = new DefaultPluginContext();
        final AnnotationProcessor annotationProcessor = new AnnotationProcessor();
        final DependencyResolver dependencyResolver = new DependencyResolver(annotationProcessor);
        manager = new DefaultPluginManager(context, annotationProcessor, dependencyResolver);

    }

    @Test
    public void shouldLoadAndResolveDependencies() throws Exception {
        final PluginSource source = new PluginSource() {
            @Override
            public Collection<Class<?>> load() {
                return newArrayList(DeepDependencyPlugin.class, DependencyPlugin.class, TestPlugin.class, SingleDependencyPlugin.class);
            }
        };
        final Collection<Object> plugins = manager.loadPlugins(source);

        assertEquals(4, plugins.size());
        assertNull(manager.getPlugin(OptionalPlugin.class));
    }

    @Test
    public void shouldFailIfNonOptionalPluginIsMissing() throws Exception {
        final PluginSource source = new PluginSource() {
            @Override
            public Collection<Class<?>> load() {
                return newArrayList(DeepDependencyPlugin.class, DependencyPlugin.class, SingleDependencyPlugin.class);
            }
        };

        try {
            manager.loadPlugins(source);
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof MissingDependencyException);
        }
    }

    @Test
    public void shouldLoadFromJar() throws Exception {
        final PluginSource source = PluginSources.jarSource("C:\\Users\\matte\\Software\\PlugFace\\plugface-core\\target\\test-classes\\plugins");

        final Collection<Object> plugins = manager.loadPlugins(source);

        System.out.println(plugins);
    }

    private <T> List<T> newArrayList(T... elements) {
        ArrayList<T> list = new ArrayList<>(elements.length);
        list.addAll(Arrays.asList(elements));
        return list;
    }
}
