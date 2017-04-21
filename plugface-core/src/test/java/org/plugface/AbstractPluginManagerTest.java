package org.plugface;

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
import org.plugface.impl.DefaultPluginContext;
import org.plugface.impl.DefaultPluginManager;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

public class AbstractPluginManagerTest {

    private PluginContext context;
    private Plugin plugin;

    @Before
    public void setUp() throws Exception {
        this.context = mock(PluginContext.class);
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
        AbstractPluginManager manager = new DefaultPluginManager("managerOne", context);

        String testFolder = "test/folder";
        manager.setPluginFolder(testFolder);

        assertEquals(testFolder, manager.getPluginFolder());
    }

//    @Test TODO:why line 106 goes in return type error asking for a map?
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

        when(context.getPluginMap()).thenReturn(map);
        when(context.getPlugin("testPlugin")).thenReturn(plugin);

        when(plugin.isEnabled()).thenReturn(false);

        manager.startPlugin(plugin);
        manager.startPlugin("testPlugin");
        manager.startAll();

        verify(plugin, never()).start();

    }

    @Test
    public void getContext() throws Exception {
        AbstractPluginManager manager = new DefaultPluginManager("managerOne", context);

        assertEquals(context, manager.getContext());
    }

    @Test
    public void extensionsTest() throws Exception {
        AbstractPluginManager manager = new DefaultPluginManager("testManager", context);
        ClassLoader classLoader = getClass().getClassLoader();
        manager.setPluginFolder(new File(classLoader.getResource("plugins").getFile()).getAbsolutePath());
        List<Plugin> loaded = manager.loadPlugins(true);
        Plugin test = null;

        for (Plugin p : loaded) {
            if (p.getName().equals("anotherPlugin")) {
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

    @Test
    public void pluginApiImplementationCheckTest() throws Exception {
        Plugin iterablePlugin = new ManagerTestPlugin();
        Plugin randomPlugin = new SimplePlugin("randomPlugin") {

            @Override
            public void start() {

            }

            @Override
            public void stop() {

            }
        };

        PluginContext context2 = new DefaultPluginContext();
        PluginManager manager = new DefaultPluginManager("managerOne", context2);
        context2.addPlugin(iterablePlugin);
        context2.addPlugin(randomPlugin);

        boolean comparable = manager.isPluginImplementingApi(iterablePlugin, Comparable.class);
        boolean iterable = manager.isPluginImplementingApi(iterablePlugin.getName(), Iterable.class);
        List<Plugin> implementingPlugins = manager.getAllImplementingPlugin(Iterable.class);
        assertFalse(comparable);
        assertTrue(iterable);

        assertNotNull(implementingPlugins);
        assertEquals(1, implementingPlugins.size());
        assertEquals(iterablePlugin, implementingPlugins.get(0));
    }

    @Test
    public void pluginHealthCheckTest() throws Exception {
        Plugin iterablePlugin = new ManagerTestPlugin();
        Plugin randomPlugin = new SimplePlugin("randomPlugin") {

            @Override
            public void start() {

            }

            @Override
            public void stop() {

            }
        };

        randomPlugin.setStatus(PluginStatus.READY);
        randomPlugin.enable();

        PluginContext context2 = new DefaultPluginContext();
        PluginManager manager = new DefaultPluginManager("managerOne", context2);
        context2.addPlugin(iterablePlugin);
        context2.addPlugin(randomPlugin);

        manager.enablePlugin(iterablePlugin);
        manager.enablePlugin(randomPlugin);

        manager.startAll();

        for (Plugin p : context2.getPluginMap().values()) {
            assertEquals(PluginStatus.RUNNING, p.getStatus());
        }

        iterablePlugin.setStatus(PluginStatus.ERROR);
        randomPlugin.setStatus(PluginStatus.ERROR);
    }
}

class ManagerTestPlugin extends SimplePlugin implements Iterable {

    ManagerTestPlugin() {
        super("testPlugin");
        setStatus(PluginStatus.READY);
        enable();
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public Iterator iterator() {
        return null;
    }

}
