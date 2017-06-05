package org.plugface.core.impl;

/*-
 * #%L
 * plugface-core
 * %%
 * Copyright (C) 2017 Matteo Joliveau
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


import org.junit.Test;
import org.plugface.core.*;

import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DefaultPluginContextTest {

    @Test
    public void pluginRegistry() throws Exception {
        Plugin plugin = mock(Plugin.class);
        when(plugin.getName()).thenReturn("test");

        PluginContext context = new DefaultPluginContext();

        context.addPlugin(plugin);

        try {
            context.getPlugin("fail");
            fail("Should have thrown NoSuchPluginException");
        } catch (Exception e) {
            assertTrue(e instanceof NoSuchPluginException);
        }

        try {
            context.removePlugin("fail");
            fail("Should have thrown NoSuchPluginException");
        } catch (Exception e) {
            assertTrue(e instanceof NoSuchPluginException);
        }

        assertEquals(plugin, context.getPlugin("test"));
        assertTrue(context.hasPlugin("test"));

        Map<String, Plugin> registry = context.getPluginMap();
        assertEquals(plugin, registry.get("test"));

        Plugin removed = context.removePlugin("test");
        assertEquals(plugin, removed);
        assertFalse(context.hasPlugin("test"));
    }

    @Test
    public void managerRegistry() throws Exception {
        PluginManager manager = mock(PluginManager.class);
        when(manager.getName()).thenReturn("test");

        PluginContext context = new DefaultPluginContext();

        context.addPluginManager(manager);

        try {
            context.getPluginManager("fail");
            fail("Should have thrown NoSuchPluginManagerException");
        } catch (Exception e) {
            assertTrue(e instanceof NoSuchPluginManagerException);
        }

        try {
            context.removePluginManager("fail");
            fail("Should have thrown NoSuchPluginManagerException");
        } catch (Exception e) {
            assertTrue(e instanceof NoSuchPluginManagerException);
        }

        assertEquals(manager, context.getPluginManager("test"));
        assertTrue(context.hasPluginManger("test"));

        Map<String, PluginManager> registry = context.getPluginManagerMap();
        assertEquals(manager, registry.get("test"));

        PluginManager removed = context.removePluginManager("test");
        assertEquals(manager, removed);
        assertFalse(context.hasPluginManger("test"));
    }
}
