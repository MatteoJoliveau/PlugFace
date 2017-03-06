package com.matteojoliveau.plugface.impl;

import com.matteojoliveau.plugface.PlugfaceContext;
import com.matteojoliveau.plugface.Plugin;
import com.matteojoliveau.plugface.PluginConfiguration;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class DefaultPluginTest {

    @Test
    public void pluginConfiguration() throws Exception {
        PlugfaceContext context = mock(PlugfaceContext.class);
        PluginConfiguration config = mock(PluginConfiguration.class);

        Plugin plugin = new DefaultPlugin("testPlugin", context) {
            @Override
            public void start() {
                System.out.println("Starting test plugin");
            }

            @Override
            public void stop() {
                System.out.println("Stopping test plugin");
            }
        };

        plugin.setPluginConfiguration(config);

        Assert.assertEquals(config, plugin.getPluginConfiguration());
    }

    @Test
    public void context() throws Exception {
        PlugfaceContext context = mock(PlugfaceContext.class);

        Plugin plugin = new DefaultPlugin("testPlugin", context) {
            @Override
            public void start() {
                System.out.println("Starting test plugin");
            }

            @Override
            public void stop() {
                System.out.println("Stopping test plugin");
            }
        };

        Assert.assertEquals(context, plugin.getContext());

        PlugfaceContext context2 = mock(PlugfaceContext.class);
        plugin.setContext(context2);

        Assert.assertEquals(context2, plugin.getContext());
    }

    @Test
    public void name() throws Exception {
        PlugfaceContext context = mock(PlugfaceContext.class);

        Plugin plugin = new DefaultPlugin("testPlugin", context) {
            @Override
            public void start() {
                System.out.println("Starting test plugin");
            }

            @Override
            public void stop() {
                System.out.println("Stopping test plugin");
            }
        };

        Assert.assertEquals("testPlugin", plugin.getName());

        plugin.setName("secondTestPlugin");

        Assert.assertEquals("secondTestPlugin", plugin.getName());
    }
}