package org.plugface.spring;

import org.junit.After;
import org.junit.Test;
import org.mockito.Mockito;
import org.plugface.core.PluginContext;
import org.plugface.core.PluginManager;
import org.plugface.core.impl.DefaultPluginManager;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SpringPluginContextAutoConfigurationTest {

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

    @Configuration
    @ImportAutoConfiguration(SpringPluginContextAutoConfiguration.class)
    static class EmptyConfiguration {

    }

    @Test
    public void contextWithEmptyConfig() throws Exception {
        load(EmptyConfiguration.class);
        PluginContext pluginContext = context.getBean(PluginContext.class);
        assertTrue(pluginContext instanceof SpringPluginContext);
    }

    @Test
    public void contextWithCustomConfiguration() throws Exception {
        load(CustomConfiguration.class);
        PluginContext pluginContext = context.getBean(PluginContext.class);
        assertFalse(pluginContext instanceof SpringPluginContext);
    }

    @Test
    public void managerWithEmptyConfig() throws Exception {
        load(EmptyConfiguration.class);
        PluginManager manager = context.getBean(PluginManager.class);
        assertTrue(manager instanceof DefaultPluginManager);
    }

    @Test
    public void managerWithCustomConfig() throws Exception {
        load(CustomConfiguration.class);
        PluginManager manager = context.getBean(PluginManager.class);
        assertFalse(manager instanceof DefaultPluginManager);
    }

    static class CustomConfiguration extends EmptyConfiguration {

        @Bean public PluginManager anotherPluginManager() {
            return Mockito.mock(PluginManager.class);
        }

        @Bean
        public PluginContext anotherPluginContext() {
            return Mockito.mock(PluginContext.class);
        }
    }
}