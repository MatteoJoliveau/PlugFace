package org.plugface.core.impl;

import org.junit.Before;
import org.junit.Test;
import org.plugface.core.PluginManager;
import org.plugface.core.PluginSource;
import org.plugface.core.factory.PluginManagers;
import org.plugface.core.plugins.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

public class ITPluginLoading {

    private PluginManager manager;

    @Before
    public void setUp() throws Exception {
        manager = PluginManagers.defaultPluginManager();
    }

    @Test
    public void shouldLoadAndResolveDependencies() throws Exception {
        final PluginSource source = new PluginSource() {
            @Override
            public Collection<Class<?>> load() {
                return newArrayList(DeepDependencyPlugin.class, SingleDependencyPlugin.class);
            }
        };
        final Collection<Object> plugins = manager.loadPlugins(source);

        assertEquals(5, plugins.size());
        assertNotNull(manager.getPlugin(OptionalPlugin.class));
        assertNotNull(manager.getPlugin(TestPlugin.class));
        assertNotNull(manager.getPlugin(DependencyPlugin.class));
        assertNotNull(manager.getPlugin(DeepDependencyPlugin.class));
        assertNotNull(manager.getPlugin(SingleDependencyPlugin.class));
    }



    private <T> List<T> newArrayList(T... elements) {
        ArrayList<T> list = new ArrayList<>(elements.length);
        list.addAll(Arrays.asList(elements));
        return list;
    }
}
