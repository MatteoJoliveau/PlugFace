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

import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of {@link PluginContext} implementing the
 * management methods.
 * It has two registries for registering active {@link Plugin} entities and
 * {@link PluginManager} entities.
 */
public class DefaultPluginContext implements PluginContext {

    private final Object pluginLock = new Object();
    private final Object managerLock = new Object();

    /**
     * Registry for {@link Plugin}
     */
    private HashMap<String, Plugin> pluginRegistry = new HashMap<>();

    /**
     * Registry for {@link PluginManager}
     */
    private HashMap<String, PluginManager> pluginManagerRegistry = new HashMap<>();

    @Override
    public Plugin getPlugin(String pluginName) {
        if (hasPlugin(pluginName)) {
            synchronized (pluginLock) {
                return pluginRegistry.get(pluginName);
            }
        } else {
            throw new NoSuchPluginException(pluginName + " not found");
        }
    }

    @Override
    public void addPlugin(Plugin plugin) {
        synchronized (pluginLock) {
            pluginRegistry.put(plugin.getName(), plugin);
        }
    }

    @Override
    public boolean hasPlugin(String pluginName) {
        synchronized (pluginLock) {
            return pluginRegistry.containsKey(pluginName);
        }
    }

    @Override
    public Plugin removePlugin(String pluginName) {
        if (hasPlugin(pluginName)) {
            synchronized (pluginLock) {
                return pluginRegistry.remove(pluginName);
            }
        } else {
            throw new NoSuchPluginException(pluginName + " not found");
        }
    }

    @Override
    public Map<String, Plugin> getPluginMap() {
        synchronized (pluginLock) {
            return pluginRegistry;
        }
    }

    @Override
    public PluginManager getPluginManager(String managerName) throws NoSuchPluginManagerException {
        if (hasPluginManger(managerName)) {
            return pluginManagerRegistry.get(managerName);
        } else {
            throw new NoSuchPluginManagerException(managerName + " not found");
        }
    }

    @Override
    public void addPluginManager(PluginManager manager) {
        synchronized (managerLock) {
            pluginManagerRegistry.put(manager.getName(), manager);
        }
    }

    @Override
    public boolean hasPluginManger(String managerName) {
        synchronized (managerLock) {
            return pluginManagerRegistry.containsKey(managerName);
        }
    }

    @Override
    public PluginManager removePluginManager(String managerName) {
        if (hasPluginManger(managerName)) {
            synchronized (managerLock) {
                return pluginManagerRegistry.remove(managerName);
            }
        } else {
            throw new NoSuchPluginManagerException(managerName + " not found");
        }
    }

    @Override
    public Map<String, PluginManager> getPluginManagerMap() {
        synchronized (managerLock) {
            return pluginManagerRegistry;
        }
    }
}
