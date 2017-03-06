package com.matteojoliveau.plugface.impl;


import com.matteojoliveau.plugface.PlugfaceContext;
import com.matteojoliveau.plugface.Plugin;
import com.matteojoliveau.plugface.PluginManager;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

import static org.mockito.Mockito.mock;

public class DefaultPlugfaceContextTest {

    @Test
    public void pluginRegistry() throws Exception {
        Plugin plugin = mock(Plugin.class);

        PlugfaceContext context = new DefaultPlugfaceContext();

        context.addPlugin("test", plugin);

        Assert.assertEquals(plugin, context.getPlugin("test"));
        Assert.assertTrue(context.hasPlugin("test"));

        Map<String, Plugin> registry = context.getPluginMap();
        Assert.assertEquals(plugin, registry.get("test"));

        context.removePlugin("test");

        Assert.assertFalse(context.hasPlugin("test"));
    }

    @Test
    public void managerRegistry() throws Exception {
        PluginManager manager = mock(PluginManager.class);

        PlugfaceContext context = new DefaultPlugfaceContext();

        context.addPluginManager("test", manager);

        Assert.assertEquals(manager, context.getPluginManager("test"));
        Assert.assertTrue(context.hasPluginManger("test"));

        Map<String, PluginManager> registry = context.getPluginManagerMap();
        Assert.assertEquals(manager, registry.get("test"));

        context.removePluginManager("test");

        Assert.assertFalse(context.hasPluginManger("test"));
    }
}