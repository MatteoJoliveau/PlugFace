package org.plugface.spring;

/*-
 * #%L
 * PlugFace :: Spring
 * %%
 * Copyright (C) 2017 - 2018 PlugFace
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
