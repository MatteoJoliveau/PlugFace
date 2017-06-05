package org.plugface.core;

/*-
 * #%L
 * plugface-core
 * %%
 * Copyright (C) 2017 PlugFace
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
import org.junit.Ignore;
import org.junit.Test;
import org.plugface.core.*;
import org.plugface.core.impl.DefaultPluginContext;
import org.plugface.core.impl.DefaultPluginManager;

import java.io.File;

import static org.junit.Assert.*;

@Ignore
public class PluginStatusIT {
    private PluginContext context = new DefaultPluginContext();
    private PluginManager manager;
    @Before
    public void setUp() throws Exception {
        this.manager = new DefaultPluginManager("firstManager", context);
        manager.loadPlugins(new File(getClass().getClassLoader().getResource("plugins").getFile()).getAbsolutePath(),true);
        context.getPlugin("testPlugin").getPluginConfiguration().setParameter("otherPlugin", context.getPlugin("anotherPlugin"));
    }

    @Test
    public void loadingStatusTest() throws Exception {
        for (Plugin p: context.getPluginMap().values()) {
            assertNotNull(p);
            assertEquals(PluginStatus.READY, p.getStatus());
            assertFalse(p.isEnabled());
        }
    }

    @Test
    public void enableTest() throws Exception {
        for (Plugin p: context.getPluginMap().values()) {
            if (!p.getName().equals("dependentPlugin")) {
                assertNotNull(p);
                manager.enablePlugin(p);
                assertTrue(p.isEnabled());
            }
        }
    }

    @Test
    public void startStopTest() throws Exception {
        for (Plugin p: context.getPluginMap().values()) {
            if (!p.getName().equals("dependentPlugin")) {
                assertNotNull(p);
                manager.enablePlugin(p);
                assertTrue(p.isEnabled());
                manager.startPlugin(p);
                assertEquals(PluginStatus.RUNNING, p.getStatus());
                manager.stopPlugin(p);
                assertEquals(PluginStatus.STOPPED, p.getStatus());

                manager.disablePlugin(p);
                manager.startPlugin(p);
                assertFalse(p.isEnabled());
                assertNotEquals(PluginStatus.RUNNING, p.getStatus());
            }
        }
    }

    @Test(expected = MissingDependencyException.class)
    public void missingDependenciesTest() throws Exception {
        for (Plugin p: context.getPluginMap().values()) {
            assertNotNull(p);
            manager.enablePlugin(p);
        }

    }
}