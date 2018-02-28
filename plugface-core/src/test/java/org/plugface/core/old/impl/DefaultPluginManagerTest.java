package org.plugface.core.old.impl;

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

import org.junit.Before;
import org.junit.Test;
import org.plugface.core.old.AbstractPluginManager;
import org.plugface.core.old.Plugin;
import org.plugface.core.old.PluginClassLoader;
import org.plugface.core.old.PluginContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class DefaultPluginManagerTest {

    private PluginContext context;
    private Plugin plugin;

    @Before
    public void setUp() throws Exception {
        this.context = mock(PluginContext.class);
        this.plugin = mock(Plugin.class);
    }

    @Test
    public void loadPlugins() throws IOException {
        AbstractPluginManager manager = new DefaultPluginManager("managerOne", context);
        ClassLoader classLoader = getClass().getClassLoader();
        Properties prop = new Properties();
        prop.load(new FileInputStream(classLoader.getResource("permissions.properties").getFile()));
        manager.setPermissions(prop);
        File file = new File(classLoader.getResource("plugins").getFile());
        manager.setPluginFolder(file.getAbsolutePath());
        List<Plugin> loaded = manager.loadPlugins();
        assertNotNull(loaded);
        assertTrue(loaded.get(0) != null);
        PluginClassLoader pcl = (PluginClassLoader) loaded.get(0).getClass().getClassLoader();
        assertEquals(manager.getPermissions(), pcl.getPermissionProperties());
    }

    @Test
    public void loadPluginsString() throws IOException {
        AbstractPluginManager manager = new DefaultPluginManager("managerOne", context);
        ClassLoader classLoader = getClass().getClassLoader();
        Properties prop = new Properties();
        prop.load(new FileInputStream(classLoader.getResource("permissions.properties").getFile()));
        manager.setPermissions(prop);
        File file = new File(classLoader.getResource("plugins").getFile());
        List<Plugin> loaded = manager.loadPlugins(file.getAbsolutePath());
        assertNotNull(loaded);
        assertTrue(loaded.get(0) != null);
    }


    @Test
    public void loadPluginsAutoregister() throws Exception {
        AbstractPluginManager manager = new DefaultPluginManager("managerOne", context);
        ClassLoader classLoader = getClass().getClassLoader();
        Properties prop = new Properties();
        prop.load(new FileInputStream(classLoader.getResource("permissions.properties").getFile()));
        manager.setPermissions(prop);
        File file = new File(classLoader.getResource("plugins").getFile());
        manager.setPluginFolder(file.getAbsolutePath());
        List<Plugin> loaded = manager.loadPlugins(true);
        assertNotNull(loaded);
        verify(context, atLeastOnce()).addPlugin(isA(Plugin.class));
        loaded = manager.loadPlugins(false);
        assertNotNull(loaded);

    }

    @Test
    public void loadPluginsAutoregisterString() throws Exception {
        AbstractPluginManager manager = new DefaultPluginManager("managerOne", context);
        ClassLoader classLoader = getClass().getClassLoader();
        Properties prop = new Properties();
        prop.load(new FileInputStream(classLoader.getResource("permissions.properties").getFile()));
        manager.setPermissions(prop);
        File file = new File(classLoader.getResource("plugins").getFile());
        List<Plugin> loaded = manager.loadPlugins(file.getAbsolutePath(), true);
        assertNotNull(loaded);
        verify(context, atLeastOnce()).addPlugin(isA(Plugin.class));
        loaded = manager.loadPlugins(file.getAbsolutePath(), false);
        assertNotNull(loaded);

    }


    //    @Test
    public void debugLoadPluginTest() throws Exception {
        AbstractPluginManager manager = new DefaultPluginManager("managerOne", context);
        manager.setDebug(true);
        ClassLoader classLoader = getClass().getClassLoader();
        Properties prop = new Properties();
        prop.load(new FileInputStream(classLoader.getResource("permissions.properties").getFile()));
        manager.setPermissions(prop);
        manager.setPluginFolder("C:\\Users\\Matteo\\IdeaProjects\\test2\\target\\classes");
        List<Plugin> loaded = manager.loadPlugins(true);
        assertNotNull(loaded);
        verify(context, atLeastOnce()).addPlugin(isA(Plugin.class));

    }
}


