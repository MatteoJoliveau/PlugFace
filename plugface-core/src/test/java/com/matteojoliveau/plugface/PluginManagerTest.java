package com.matteojoliveau.plugface;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class PluginManagerTest {

    @Test
    public void configurePlugin() throws Exception {
        PlugfaceContext context = mock(PlugfaceContext.class);
        Plugin plugin = mock(Plugin.class);
        PluginConfiguration pluginConfig = mock(PluginConfiguration.class);
        Map<String, Object> config = new HashMap<>();
        when(plugin.getPluginConfiguration()).thenReturn(pluginConfig);

        PluginManager manager = new PluginManager("manager", context);

        manager.configurePlugin(plugin, config);

        verify(pluginConfig).updateConfiguration(eq(config));
        verify(plugin).setPluginConfiguration(eq(pluginConfig));
    }

    @Test
    public void configurePluginByName() throws Exception {
        PlugfaceContext context = mock(PlugfaceContext.class);
        Plugin plugin = mock(Plugin.class);
        PluginConfiguration pluginConfig = mock(PluginConfiguration.class);
        Map<String, Object> config = new HashMap<>();
        when(plugin.getPluginConfiguration()).thenReturn(pluginConfig);
        when(context.getPlugin("test")).thenReturn(plugin);

        PluginManager manager = new PluginManager("manager", context);

        manager.configurePlugin("test", config);

        verify(pluginConfig).updateConfiguration(eq(config));
        verify(plugin).setPluginConfiguration(eq(pluginConfig));

    }

    @Test
    public void registerPluginInContext() throws Exception {
        PlugfaceContext context = mock(PlugfaceContext.class);
        Plugin plugin = mock(Plugin.class);

        PluginManager manager = new PluginManager("manager", context);

        manager.registerPluginInContext("test", plugin);

        verify(context).addPlugin(eq("test"), eq(plugin));
    }

    @Test
    public void removePluginFromContext() {
        PlugfaceContext context = mock(PlugfaceContext.class);
        Plugin plugin = mock(Plugin.class);

        when(context.removePlugin("test")).thenReturn(plugin);

        PluginManager manager = new PluginManager("manager", context);

        Plugin returned = manager.removePluginFromContext("test");

        Assert.assertEquals(plugin, returned);
        verify(context).removePlugin(eq("test"));
    }

    @Test
    public void loadPlugins() {
        PlugfaceContext context = mock(PlugfaceContext.class);

        PluginManager manager = new PluginManager(context);
        ClassLoader classLoader = getClass().getClassLoader();
        manager.setPermissionsFile(classLoader.getResource("permissions.properties").getFile());
        File file = new File(classLoader.getResource("plugins").getFile());
        manager.setPluginFolder(file.getAbsolutePath());
        List<Plugin> loaded = null;
        try {
            loaded = manager.loadPlugins();
        } catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        Assert.assertTrue(loaded.get(0) != null);
    }

    @Test
    public void setFolder() {
        PlugfaceContext context = mock(PlugfaceContext.class);

        PluginManager manager = new PluginManager(context);

        String testFolder = "test/folder";
        manager.setPluginFolder(testFolder);

        Assert.assertEquals(testFolder, manager.getPluginFolder());
    }
}