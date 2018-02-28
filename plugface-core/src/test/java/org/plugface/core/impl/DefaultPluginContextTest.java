package org.plugface.core.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.plugface.core.annotations.Plugin;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

@SuppressWarnings({"SuspiciousMethodCalls", "ResultOfMethodCallIgnored"})
public class DefaultPluginContextTest {

    private Map<String, Object> registry = new HashMap<>();

    private DefaultPluginContext context;

    private TestPlugin plugin = new TestPlugin();


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        context = new DefaultPluginContext(registry);

    }

    @Test
    public void shouldAddAPlugin() {
        context.addPlugin(plugin);
        assertTrue(registry.containsKey("test"));
        final Object test = registry.get("test");
        assertEquals(plugin, test);
    }

    @Test
    public void shouldRemovePluginFromName() {
        registry.put("test", plugin);
        final TestPlugin test = context.removePlugin("test");
        assertFalse(registry.containsKey("test"));
        assertEquals(plugin, test);
    }

    @Test
    public void shouldRemovePluginFromInstance() {
        registry.put("test", plugin);
        final TestPlugin test = context.removePlugin(plugin);
        assertFalse(registry.containsKey("test"));
        assertEquals(plugin, test);
    }

    @Test
    public void shouldRetrievePluginFromName() {
        registry.put("test", plugin);
        final TestPlugin test = context.getPlugin("test");
        assertTrue(registry.containsKey("test"));
        assertEquals(plugin, test);
    }

    @Test
    public void shouldRetrievePluginFromClass() {
        registry.put("test", plugin);
        final TestPlugin test = context.getPlugin(TestPlugin.class);
        assertTrue(registry.containsKey("test"));
        assertEquals(plugin, test);
    }

    @Plugin("test")
    private static class TestPlugin {}

}