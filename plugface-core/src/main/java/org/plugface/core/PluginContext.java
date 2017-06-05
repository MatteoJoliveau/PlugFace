package org.plugface.core;

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

import org.plugface.core.impl.DefaultPluginContext;

import java.util.Map;

/**
 * Base interface for a context that holds the references to all the {@link PluginManager} and
 * all the registered {@link Plugin} entities.
 * Implementations should contain registries that holds the objects.
 *
 * @see DefaultPluginContext
 */
public interface PluginContext {
    /*
        Plugins
    */

    /**
     * Return the plugin identified by the specified name.
     * ATTENTION: implementations should throw {@link NoSuchPluginException} if the plugin
     * is not in the registry.
     *
     * @param pluginName the name of the plugin to look for
     * @return the plugin identified by the provided name
     */
    Plugin getPlugin(String pluginName);

    /**
     * Add the provided plugin to the registry, identified by its name.
     *
     * @param plugin the plugin to register
     */
    void addPlugin(Plugin plugin);

    /**
     * Check if the registry contains the specified plugin.
     *
     * @param pluginName the name of the plugin to look for
     * @return true if the plugin exists in the context, false if not
     */
    boolean hasPlugin(String pluginName);

    /**
     * Remove the specified plugin from the registry.
     * ATTENTION: implementations should throw {@link NoSuchPluginException} if the plugin
     * is not in the registry.
     *
     * @param pluginName the name of the plugin to remove
     * @return the removed plugin
     */
    Plugin removePlugin(String pluginName);

    /**
     * Returns a {@link Map} of all the plugins registered in the
     * context, identified by their names.
     *
     * @return a {@link Map} of names-plugins registered in the
     * context
     */
    Map<String, Plugin> getPluginMap();

    /*
        Plugin Managers
     */

    /**
     * Return the manager identified by the specified name.
     * ATTENTION: implementations should throw {@link NoSuchPluginManagerException} if the manager
     * is not in the registry.
     *
     * @param managerName the name of the manager to look for
     * @return the manager identified by the provided name
     */
    PluginManager getPluginManager(String managerName);

    /**
     * Add the provided manager to the registry, identified by its name.
     *
     * @param manager the plugin to register
     */
    void addPluginManager(PluginManager manager);

    /**
     * Check if the registry contains the specified manager.
     *
     * @param managerName the name of the manager to look for
     * @return true if the manager exists in the context, false if not
     */
    boolean hasPluginManger(String managerName);

    /**
     * Remove the specified manager from the registry.
     * ATTENTION: implementations should throw {@link NoSuchPluginManagerException} if the manager
     * is not in the registry.
     *
     * @param managerName the name of the manager to remove
     * @return the removed manager
     */
    PluginManager removePluginManager(String managerName);

    /**
     * Returns a {@link Map} of all the managers registered in the
     * context, identified by their names.
     *
     * @return a {@link Map} of names-managers registered in the
     * context
     */
    Map<String, PluginManager> getPluginManagerMap();


}
