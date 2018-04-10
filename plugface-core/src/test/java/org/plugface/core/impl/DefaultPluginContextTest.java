package org.plugface.core.impl;

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

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.plugface.core.PluginRef;
import org.plugface.core.plugins.OptionalPlugin;
import org.plugface.core.plugins.TestInterface;
import org.plugface.core.plugins.TestPlugin;
import org.plugface.core.plugins.TestSuperclass;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@SuppressWarnings({"SuspiciousMethodCalls", "ResultOfMethodCallIgnored"})
public class DefaultPluginContextTest {

    private Map<String, Object> registry = new HashMap<>();

    private DefaultPluginContext context;

    private TestPlugin plugin = new TestPlugin();

    private OptionalPlugin optionalPlugin = new OptionalPlugin();


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

    @Test
    public void shouldRetrievePluginFromInterface() {
        registry.put("test", plugin);
        final TestInterface test = context.getPlugin(TestInterface.class);
        assertTrue(registry.containsKey("test"));
        assertEquals(plugin, test);
    }

    @Test
    public void shouldRetrievePluginFromSuperclass() {
        registry.put("test", plugin);
        final TestSuperclass test = context.getPlugin(TestSuperclass.class);
        assertTrue(registry.containsKey("test"));
        assertEquals(plugin, test);
    }

    @Test
    public void shouldRetrieveAllThePlugins() throws Exception {
        registry.put("test", plugin);
        registry.put("optional", optionalPlugin);

        final List<PluginRef> all = (List<PluginRef>) context.getAllPlugins();
        assertFalse(all.isEmpty());

        for (int i = 0; i < all.size(); i++) {
            switch (i) {
                case 0: {
                    final PluginRef ref = all.get(i);
                    assertEquals("test", ref.getName());
                    assertEquals(plugin, ref.get());
                    assertEquals(TestPlugin.class, ref.getType());
                    break;
                }
                case 1: {
                    final PluginRef ref = all.get(i);
                    assertEquals("optional", ref.getName());
                    assertEquals(optionalPlugin, ref.get());
                    assertEquals(OptionalPlugin.class, ref.getType());
                    break;
                }
            }
        }
    }
}
