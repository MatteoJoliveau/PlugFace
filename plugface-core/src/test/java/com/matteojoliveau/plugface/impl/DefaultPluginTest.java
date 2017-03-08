package com.matteojoliveau.plugface.impl;

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
