package org.plugface.spring;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.plugface.core.PluginManager;
import org.plugface.core.factory.PluginSources;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SpringPluginManagerTest {
    private AnnotationConfigApplicationContext context;
    private PluginManager sut;

    private void load(Class<?> config) throws Exception {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(config);
        context.refresh();
        this.context = context;
    }

    @Before
    public void setUp() throws Exception {
        load(SpringPluginContextTest.Config.class);
        sut = this.context.getBean(PluginManager.class);
    }

    @After
    public void tearDown() throws Exception {
        context.stop();
    }

    @Test
    public void shouldResolveMixedDependencies() throws Exception {
        sut.loadPlugins(PluginSources.classList(TestPlugin.class));
        final TestPlugin plugin = sut.getPlugin(TestPlugin.class);
        assertNotNull(plugin);
        assertEquals("Plugface: Hello Spring!", plugin.greet());
    }

    @Configuration
    @ImportAutoConfiguration(SpringPluginContextAutoConfiguration.class)
    static class Config {

        @Bean
        public TestBean testBean() {
            return new TestBean();
        }
    }

    public static class TestBean {
        public String greet() {
            return "Hello Spring!";
        }
    }




}