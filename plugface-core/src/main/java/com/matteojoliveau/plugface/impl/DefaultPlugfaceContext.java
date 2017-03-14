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

import com.matteojoliveau.plugface.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of {@link PlugfaceContext} implementing the
 * management methods.
 * It has two registries for registering active {@link Plugin} entities and
 * {@link PluginManager} entities.
 */
public class DefaultPlugfaceContext implements PlugfaceContext{

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
        if(hasPlugin(pluginName)) {
            return pluginRegistry.get(pluginName);
        } else {
            throw new NoSuchPluginException(pluginName + " not found");
        }
    }

    @Override
    public void addPlugin(Plugin plugin) {
        pluginRegistry.put(plugin.getName(), plugin);

    }

    @Override
    public boolean hasPlugin(String pluginName) {
        return pluginRegistry.containsKey(pluginName);
    }

    @Override
    public Plugin removePlugin(String pluginName) {
        if(hasPlugin(pluginName)) {
            return pluginRegistry.remove(pluginName);
        } else {
            throw new NoSuchPluginException(pluginName + " not found");
        }
    }

    @Override
    public Map<String, Plugin> getPluginMap() {
        return pluginRegistry;
    }

    @Override
    public PluginManager getPluginManager(String managerName) throws NoSuchPluginManagerException {
        if(hasPluginManger(managerName)) {
            return pluginManagerRegistry.get(managerName);
        } else {
            throw new NoSuchPluginManagerException(managerName + " not found");
        }
    }

    @Override
    public void addPluginManager(PluginManager manager) {
        pluginManagerRegistry.put(manager.getName(), manager);
    }

    @Override
    public boolean hasPluginManger(String managerName) {
        return pluginManagerRegistry.containsKey(managerName);
    }

    @Override
    public PluginManager removePluginManager(String managerName){
        if(hasPluginManger(managerName)) {
            return pluginManagerRegistry.remove(managerName);
        } else {
            throw new NoSuchPluginManagerException(managerName + " not found");
        }
    }

    @Override
    public Map<String, PluginManager> getPluginManagerMap() {
        return pluginManagerRegistry;
    }
}
