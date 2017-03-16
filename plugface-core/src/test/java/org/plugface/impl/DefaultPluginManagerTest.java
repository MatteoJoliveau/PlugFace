package org.plugface.impl;

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
import org.mockito.Mockito;
import org.plugface.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class DefaultPluginManagerTest {

    private PlugfaceContext context;
    private Plugin plugin;

    @Before
    public void setUp() throws Exception {
        this.context = mock(PlugfaceContext.class);
        this.plugin = mock(Plugin.class);
    }

    @Test
    public void configurePlugin() throws Exception {
        PluginConfiguration pluginConfig = mock(PluginConfiguration.class);
        Map<String, Object> config = new HashMap<>();
        when(plugin.getPluginConfiguration()).thenReturn(pluginConfig);

        AbstractPluginManager manager = new DefaultPluginManager("manager", context);

        manager.configurePlugin(plugin, config);

        verify(pluginConfig).updateConfiguration(eq(config));
        verify(plugin).setPluginConfiguration(eq(pluginConfig));
    }

    @Test
    public void configurePluginByName() throws Exception {
        Plugin plugin = mock(Plugin.class);
        PluginConfiguration pluginConfig = mock(PluginConfiguration.class);
        Map<String, Object> config = new HashMap<>();
        when(plugin.getPluginConfiguration()).thenReturn(pluginConfig);
        when(context.getPlugin("test")).thenReturn(plugin);

        AbstractPluginManager manager = new DefaultPluginManager("manager", context);

        manager.configurePlugin("test", config);

        verify(pluginConfig).updateConfiguration(eq(config));
        verify(plugin).setPluginConfiguration(eq(pluginConfig));

    }

    @Test
    public void setFolder() {
        AbstractPluginManager manager = new DefaultPluginManager(context);

        String testFolder = "test/folder";
        manager.setPluginFolder(testFolder);

        assertEquals(testFolder, manager.getPluginFolder());
    }

    @Test
    public void loadPlugins() throws IOException {
        AbstractPluginManager manager = new DefaultPluginManager(context);
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
        AbstractPluginManager manager = new DefaultPluginManager(context);
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
        AbstractPluginManager manager = new DefaultPluginManager(context);
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
        AbstractPluginManager manager = new DefaultPluginManager(context);
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

    @Test
    public void enabledPluginOperations() throws Exception {
        Map<String, Plugin> map = new HashMap<>();
        map.put("testPlugin", plugin);

        AbstractPluginManager manager = new DefaultPluginManager("managerOne", context);

        when(context.getPlugin("testPlugin")).thenReturn(plugin);
        when(context.getPluginMap()).thenReturn(map);

        when(plugin.getStatus()).thenReturn(PluginStatus.STOPPED);
        when(plugin.isEnabled()).thenReturn(true);

        manager.startPlugin(plugin);
        manager.startPlugin("testPlugin");
        manager.startAll();

        when(plugin.getStatus()).thenReturn(PluginStatus.RUNNING);

        manager.stopPlugin(plugin);
        manager.stopPlugin("testPlugin");
        manager.stopAll();

        verify(plugin, times(3)).start();
        verify(plugin, times(3)).stop();
        verify(plugin, times(6)).setStatus(isA(PluginStatus.class));

        verify(context, times(2)).getPlugin("testPlugin");

    }

    @Test
    public void disabledPluginOperations() throws Exception {
        Map<String, Plugin> map = new HashMap<>();
        map.put("testPlugin", plugin);

        AbstractPluginManager manager = new DefaultPluginManager("managerOne", context);

        when(context.getPlugin("testPlugin")).thenReturn(plugin);
        when(context.getPluginMap()).thenReturn(map);

        when(plugin.isEnabled()).thenReturn(false);

        manager.startPlugin(plugin);
        manager.startPlugin("testPlugin");
        manager.startAll();

        verify(plugin, never()).start();

    }

    @Test
    public void getContext() throws Exception {
        AbstractPluginManager manager = new DefaultPluginManager(context);

        assertEquals(context, manager.getContext());
    }

    @Test
    public void extensionsTest() throws Exception {
        AbstractPluginManager manager = new DefaultPluginManager("testManager", context);
        ClassLoader classLoader = getClass().getClassLoader();
        manager.setPluginFolder(new File(classLoader.getResource("plugins").getFile()).getAbsolutePath());
        List<Plugin> loaded = manager.loadPlugins(true);
        Plugin test = null;

        for (Plugin p: loaded) {
            if (p.getName().equals("anotherPlugin")){
                test = p;
            }
        }

        when(context.getPlugin("anotherPlugin")).thenReturn(test);

        String s2 = (String) manager.execExtension("anotherPlugin", "test");
        String s1 = (String) manager.execExtension(test, "test");
        assertNotNull(s1);
        assertNotNull(s2);
        assertEquals("Test", s1);
        assertEquals(s1, s2);

        try {
            manager.execExtension(test, "fail");
        } catch (Exception e) {
            assertTrue(e instanceof ExtensionMethodNotFound);
            assertEquals("fail not found", e.getMessage());
        }
    }

    @Test
    public void toStringTest() throws Exception {
        PluginManager manager = new DefaultPluginManager("testManager", context);
        PluginManager manager2 = new DefaultPluginManager("testManager", context);
        assertNotNull(manager.toString());
        assertEquals(manager2.toString(), manager.toString());
    }

//    @Test
    public void debugLoadPluginTest() throws Exception {
        AbstractPluginManager manager = new DefaultPluginManager(context);
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

    //    @Test
//    public void test() {
//        PlugfaceContext context = new DefaultPlugfaceContext();
//        AbstractPluginManager manager = new DefaultPluginManager(context);
//
//        ClassLoader classLoader = getClass().getClassLoader();
//        manager.setPluginFolder(new File(classLoader.getResource("plugins").getFile()).getAbsolutePath());
//        manager.loadPlugins(true);
//        Plugin test = context.getPlugin("testPlugin");
//        Plugin another = context.getPlugin("anotherPlugin");
//
//        Map<String, Object> conf = new HashMap<>();
//        conf.put("otherPlugin", another);
//        conf.put("passingString", "Hello! I'm a string travelling across plugins!");
//        manager.configurePlugin(test, conf);
//
//        test.start();
//
//    }
}
