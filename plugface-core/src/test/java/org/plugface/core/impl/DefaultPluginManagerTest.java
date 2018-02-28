package org.plugface.core.impl;

import org.junit.Before;
import org.junit.Test;
import org.plugface.core.PluginContext;
import org.plugface.core.PluginSource;
import org.plugface.core.annotations.Plugin;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class DefaultPluginManagerTest {

    private final PluginContext mockContext = mock(PluginContext.class);

    private final PluginSource mockSource = mock(PluginSource.class);

    private DefaultPluginManager manager;

    private final TestPlugin plugin = new TestPlugin();

    @Before
    public void setUp() {
        manager = new DefaultPluginManager(mockContext);

        when(mockContext.removePlugin(eq(plugin))).thenReturn(plugin);
        when(mockContext.removePlugin(eq("test"))).thenReturn(plugin);

        when(mockContext.getPlugin(eq(TestPlugin.class))).thenReturn(plugin);
        when(mockContext.getPlugin(eq("test"))).thenReturn(plugin);
        final Collection<Class<?>> plugins = new ArrayList<>();
        plugins.add(TestPlugin.class);
        when(mockSource.load()).thenReturn(plugins);
    }

    @Test
    public void shouldRegisterAPlugin() {
        manager.register(plugin);
        verify(mockContext).addPlugin(eq(plugin));
    }

    @Test
    public void shouldRemovePluginFromName() {
        final TestPlugin test = manager.removePlugin("test");
        assertEquals(plugin, test);
        verify(mockContext).removePlugin(eq("test"));
    }

    @Test
    public void shouldRemovePluginFromInstance() {
        final TestPlugin test = manager.removePlugin(plugin);
        assertEquals(plugin, test);
        verify(mockContext).removePlugin(eq(plugin));
    }

    @Test
    public void shouldRetrievePluginFromName() {
        final TestPlugin test = manager.getPlugin("test");
        assertEquals(plugin, test);
        verify(mockContext).getPlugin(eq("test"));
    }

    @Test
    public void shouldRetrievePluginFromClass() {
        final TestPlugin test = manager.getPlugin(TestPlugin.class);
        assertEquals(this.plugin, test);
        verify(mockContext).getPlugin(eq(TestPlugin.class));
    }

    @Test
    public void shouldLoadPluginsFromSource() throws InstantiationException, IllegalAccessException {
        final Collection<Object> plugins = manager.loadPlugins(mockSource);
        assertFalse(plugins.isEmpty());

        final Object test = plugins.iterator().next();
        assertEquals(TestPlugin.class, test.getClass());

        final TestPlugin fromContext = manager.getPlugin(TestPlugin.class);
        assertNotNull(fromContext);
    }

    @Plugin("test")
    public static class TestPlugin {}

}