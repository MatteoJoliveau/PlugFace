package org.plugface.spring;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.plugface.core.PluginContext;
import org.plugface.core.annotations.Plugin;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.junit.Assert.assertNotNull;

public class SpringPluginContextTest {

    private AnnotationConfigApplicationContext context;
    private PluginContext sut;

    private void load(Class<?> config) throws Exception {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(config);
        context.refresh();
        this.context = context;
    }

    @Before
    public void setUp() throws Exception {

        load(SpringPluginContextTest.Config.class);
        sut = this.context.getBean(PluginContext.class);
        sut.addPlugin(new TestPlugin());
    }

    @Test
    public void shouldLoadFromBothPCandSC() {
        final TestPlugin plugin = sut.getPlugin(TestPlugin.class);
        final TestBean bean = sut.getPlugin(TestBean.class);

        assertNotNull(plugin);
        assertNotNull(bean);
    }

    @Test
    public void shouldLoadFromPC() {
        final TestPlugin plugin = sut.getPlugin(TestPlugin.class);

        assertNotNull(plugin);
    }

    @Test
    public void shouldLoadFromSC() {
        final TestBean bean = sut.getPlugin(TestBean.class);

        assertNotNull(bean);
    }

    @After
    public void tearDown() throws Exception {
        context.stop();
    }

    @Configuration
    @ImportAutoConfiguration(SpringPluginContextAutoConfiguration.class)
    static class Config {

        @Bean
        public TestBean testBean() {
            return new TestBean();
        }
    }

    static class TestBean {
        public String greet() {
            return "Hello Spring!";
        }
    }

    @Plugin("test")
    static class TestPlugin {

        public String greet() {
            return "Hello Plugface!";
        }
    }

}