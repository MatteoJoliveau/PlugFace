package org.plugface.impl;

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

import org.plugface.*;
import org.junit.Test;
import org.plugface.annotations.Requires;

import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class DefaultPluginTest {

    private Plugin plugin = new TestPlugin();

    @Test
    public void pluginConfiguration() throws Exception {
        PluginConfiguration config = mock(PluginConfiguration.class);

        plugin.setPluginConfiguration(config);

        assertEquals(config, plugin.getPluginConfiguration());
    }

    @Test
    public void name() throws Exception {
        assertEquals("testPlugin", plugin.getName());
    }

    @Test
    public void equals() throws Exception {
        assertTrue(plugin.equals(another()));
        assertFalse(plugin.equals(new Object()));
        assertTrue(plugin.equals(plugin));
    }

    @Test
    public void hashCodeTest() throws Exception {
        int pluginHashCode = plugin.hashCode();
        assertEquals(another().hashCode(), pluginHashCode);
    }

    @Test
    public void toStringTest() throws Exception {
        assertEquals(another().toString(), plugin.toString());

    }

    private Plugin another() {
        Plugin another = new DefaultPlugin<Void, Void>("testPlugin") {
            @Override
            public void start() {
                System.out.println("Starting test plugin");
            }

            @Override
            public void stop() {
                System.out.println("Stopping test plugin");
            }

            @Override
            public Void execute(Void parameters) {
                return null;
            }

        };


        return another;
    }

    @Test
    public void testExecute() throws Exception {
        Plugin<Collection<?>, Integer> plugin = new DefaultPlugin<Collection<?>, Integer>("plugin1") {
            @Override
            public void start() {
                System.out.println("Starting test plugin");
            }

            @Override
            public void stop() {
                System.out.println("Stopping test plugin");
            }

            @Override
            public Integer execute(Collection<?> parameters) {
                return parameters.size();
            }

        };

        Plugin plugin2 = new DefaultPlugin<Integer, Collection<? extends Number>>("plugin2") {
            @Override
            public void start() {
                System.out.println("Starting test plugin");
            }

            @Override
            public void stop() {
                System.out.println("Stopping test plugin");
            }

            @Override
            public  Collection<? extends Number> execute(Integer parameters) {
                return Collections.singletonList(parameters);
            }

        };

        Class<? extends Plugin> aClass = plugin.getClass();
        boolean annotationPresent = plugin.getClass().isAnnotationPresent(Requires.class);
        System.out.print(annotationPresent + aClass.toString());
        assertEquals(Integer.valueOf(2), plugin.execute(Collections.nCopies(2, 5)));

    }

    @Test
    public void testStatus() throws Exception {
        for (PluginStatus status: PluginStatus.values()) {
            plugin.setStatus(status);
            assertEquals(status, plugin.getStatus());
        }

    }
}

@Requires(requiredPlugins = {"ciao"})
class TestPlugin extends DefaultPlugin<Void, Void> {

    public TestPlugin() {
        super("testPlugin");
    }

    @Override
    public void start() {
        System.out.println("Starting test plugin");
    }

    @Override
    public void stop() {
        System.out.println("Stopping test plugin");
    }

    @Override
    public Void execute(Void parameters) {
        return null;
    }
}
