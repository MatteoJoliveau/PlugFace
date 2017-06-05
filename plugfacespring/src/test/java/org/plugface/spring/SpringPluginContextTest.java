package org.plugface.spring;

import org.junit.After;
import org.junit.Test;
import org.plugface.core.DefaultPlugin;
import org.plugface.core.Plugin;
import org.plugface.core.PluginManager;
import org.plugface.core.impl.DefaultPluginManager;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class SpringPluginContextTest {

    private AnnotationConfigApplicationContext context;

    public void load(Class<?> config) throws Exception {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(config);
        context.refresh();
        this.context = context;
    }

    @After
    public void tearDown() throws Exception {
        context.stop();
    }

    @Test
    public void loadPluginFromSpringContext() throws Exception {
        SpringPluginContext context = loadContext();
        Plugin testPlugin = context.getPlugin("testPlugin");
        assertEquals("test", testPlugin.execute("test"));
    }

    @Test
    public void loadPluginAsNormalPlugins() throws Exception {
        SpringPluginContext context = loadContext();
        context.addPlugin(new DefaultPlugin<String, String>("testPlugin") {
            @Override
            public void start() {

            }

            @Override
            public void stop() {

            }

            @Override
            public String execute(String parameter) {
                return parameter+parameter;
            }
        });
        Plugin testPlugin = context.getPlugin("testPlugin");
        assertEquals("testtest", testPlugin.execute("test"));
    }

    @Test
    public void searchPluginInSpringContext() throws Exception {
        SpringPluginContext context = loadContext();
        context.hasPlugin("testPlugin");
    }

    @Test
    public void searchPluginInPluginContext() throws Exception {
        SpringPluginContext context = loadContext();
        context.addPlugin(new DefaultPlugin<String, String>("testInternalPlugin") {
            @Override
            public void start() {

            }

            @Override
            public void stop() {

            }

            @Override
            public String execute(String parameter) {
                return null;
            }
        });
        context.hasPlugin("testInternalPlugin");
    }

    @Test
    public void getAllPluginsFromPcAndSc() throws Exception {
        SpringPluginContext context = loadContext();
        context.addPlugin(new DefaultPlugin<String, String>("testInternalPlugin") {
            @Override
            public void start() {

            }

            @Override
            public void stop() {

            }

            @Override
            public String execute(String parameter) {
                return null;
            }
        });

        Map<String, Plugin> pluginMap = context.getPluginMap();
        assertThat(pluginMap.size(), is(2));
        assertTrue(pluginMap.containsKey("testInternalPlugin"));
        assertTrue(pluginMap.containsKey("testPlugin"));
    }

    @Test
    public void loadPluginManagerFromSpringContext() throws Exception {
        SpringPluginContext context = loadContext();
        PluginManager manager = context.getPluginManager("springDefaultPluginManager");
        assertTrue(manager instanceof DefaultPluginManager);
    }

    @Test
    public void loadPluginManagerFromPluginContext() throws Exception {
        SpringPluginContext context = loadContext();
        context.addPluginManager(new DefaultPluginManager("testManager", context));
        PluginManager testManager = context.getPluginManager("testManager");
        assertTrue(testManager instanceof DefaultPluginManager);
    }

    @Test
    public void getAllPluginManagersFromPcAndSc() throws Exception {
        SpringPluginContext context = loadContext();
        context.addPluginManager(new DefaultPluginManager("testPlugin", context));

        Map<String, PluginManager> pluginManagerMap = context.getPluginManagerMap();
        assertThat(pluginManagerMap.size(), is(2));
        assertTrue(pluginManagerMap.containsKey("springDefaultPluginManager"));
        assertTrue(pluginManagerMap.containsKey("testPlugin"));
    }

    private SpringPluginContext loadContext() throws Exception{
        load(Config.class);
        return this.context.getBean(SpringPluginContext.class);
    }

    @Configuration
    @ImportAutoConfiguration(SpringPluginContextAutoConfiguration.class)
    static class Config {
        @Bean public Plugin testPlugin() {
            return new DefaultPlugin<String, String>("testPlugin") {
                @Override
                public void start() {

                }

                @Override
                public void stop() {

                }

                @Override
                public String execute(String parameter) {
                    return parameter;
                }
            };
        }
    }
}