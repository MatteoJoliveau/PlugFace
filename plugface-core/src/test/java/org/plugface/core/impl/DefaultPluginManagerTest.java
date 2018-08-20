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
import org.mockito.ArgumentMatchers;
import org.plugface.core.PluginContext;
import org.plugface.core.PluginManager;
import org.plugface.core.PluginSource;
import org.plugface.core.internal.DependencyResolver;
import org.plugface.core.internal.di.Node;
import org.plugface.core.plugins.*;
import org.plugface.core.internal.AnnotationProcessor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class DefaultPluginManagerTest {

    private final PluginContext mockContext = mock(PluginContext.class);

    private final AnnotationProcessor mockProcessor = mock(AnnotationProcessor.class);

    private final PluginSource mockSource = mock(PluginSource.class);

    private final DependencyResolver mockResolver = mock(DependencyResolver.class);

    private DefaultPluginManager manager;

    private final TestPlugin plugin = new TestPlugin();

    @Before
    public void setUp() throws Exception {
        manager = new DefaultPluginManager(mockContext, mockProcessor, mockResolver);

        when(mockContext.removePlugin(eq(plugin))).thenReturn(plugin);
        when(mockContext.removePlugin(eq("test"))).thenReturn(plugin);

        when(mockContext.getPlugin(eq(TestPlugin.class))).thenReturn(plugin);
        when(mockContext.getPlugin(eq("test"))).thenReturn(plugin);
        final Collection<Class<?>> plugins = new ArrayList<>();
        plugins.add(TestPlugin.class);
        when(mockSource.load()).thenReturn(plugins);
        final Collection<Node<?>> nodes = new ArrayList<>();
        nodes.add(new Node<>(TestPlugin.class));
        when(mockResolver.resolve(ArgumentMatchers.<Class<?>>anyCollection())).thenReturn(nodes);
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
    public void shouldLoadPluginsFromSource() throws Exception {
        final Collection<Object> plugins = manager.loadPlugins(mockSource);
        assertFalse(plugins.isEmpty());

        final Object test = plugins.iterator().next();
        assertEquals(TestPlugin.class, test.getClass());

        final TestPlugin fromContext = manager.getPlugin(TestPlugin.class);
        assertNotNull(fromContext);
    }

    @Test
    public void shouldNotLoadAPluginTwice() throws Exception {
        manager.loadPlugins(mockSource);
        this.shouldLoadPluginsFromSource();
    }

}
