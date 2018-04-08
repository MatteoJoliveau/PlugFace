package org.plugface.core.impl;

/*-
 * #%L
 * PlugFace :: Core
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

import org.junit.Before;
import org.junit.Test;
import org.plugface.core.PluginManager;
import org.plugface.core.PluginSource;
import org.plugface.core.factory.PluginManagers;
import org.plugface.core.plugins.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

public class ITPluginLoading {

    private PluginManager manager;

    @Before
    public void setUp() throws Exception {
        manager = PluginManagers.defaultPluginManager();
    }

    @Test
    public void shouldLoadAndResolveDependencies() throws Exception {
        final PluginSource source = new PluginSource() {
            @Override
            public Collection<Class<?>> load() {
                return newArrayList(DeepDependencyPlugin.class, SingleDependencyPlugin.class);
            }
        };
        final Collection<Object> plugins = manager.loadPlugins(source);

        assertEquals(5, plugins.size());
        assertNotNull(manager.getPlugin(OptionalPlugin.class));
        assertNotNull(manager.getPlugin(TestPlugin.class));
        assertNotNull(manager.getPlugin(DependencyPlugin.class));
        assertNotNull(manager.getPlugin(DeepDependencyPlugin.class));
        assertNotNull(manager.getPlugin(SingleDependencyPlugin.class));
    }



    private <T> List<T> newArrayList(T... elements) {
        ArrayList<T> list = new ArrayList<>(elements.length);
        list.addAll(Arrays.asList(elements));
        return list;
    }
}
