package org.plugface;

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


import org.plugface.annotations.ExtensionMethod;
import org.plugface.impl.DefaultPluginManager;

import java.io.File;
import java.security.Permission;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * <a href="https://github.com/MatteoJoliveau/PlugFace/wiki/Plugin-Manager">Plugin Manager</a>
 * is used to manage and orchestrate plugins in an application. It allows basic configuration and
 * lifecycle management of plugins.
 *
 * @see DefaultPluginManager
 * @see AbstractPluginManager
 */
public interface PluginManager {

    /**
     * Configure a plugin by adding the parameters defined in the configuration
     * map to its {@link PluginConfiguration}
     *
     * @param plugin        the plugin to configure
     * @param configuration a map containing the new parameters
     */
    void configurePlugin(Plugin plugin, Map<String, Object> configuration);

    /**
     * Configure a plugin by adding the parameters defined in the configuration
     * map to its {@link PluginConfiguration}. It retrieves automatically the plugin
     * from the current context searching by name
     *
     * @param pluginName    the name of the plugin to configure
     * @param configuration a map containing the new parameters
     */
    void configurePlugin(String pluginName, Map<String, Object> configuration);

    /**
     * Starts a plugin by calling {@link Plugin#start()}
     *
     * @param plugin the plugin to start
     */
    void startPlugin(Plugin plugin);

    /**
     * Starts a plugin by retrieving the plugin
     * from the context and calling {@link Plugin#start()}
     *
     * @param pluginName the name of the plugin to start
     */
    void startPlugin(String pluginName);

    /**
     * Stops a plugin by calling {@link Plugin#stop()}
     *
     * @param plugin the plugin to stop
     */
    void stopPlugin(Plugin plugin);

    /**
     * Stops a plugin by retrieving the plugin
     * from the context and calling {@link Plugin#stop()}
     *
     * @param pluginName the name of the plugin to stop
     */
    void stopPlugin(String pluginName);

    /**
     * Starts all the plugin in the same context as
     * the manager
     */
    void startAll();

    /**
     * Stops all the plugin in the same context as
     * the manager
     */
    void stopAll();

    /**
     * Restarts a plugin by calling {@link Plugin#stop()} and {@link Plugin#start()}
     * sequencially
     *
     * @param plugin the name of the plugin to start
     */
    void restartPlugin(Plugin plugin);

    /**
     * Restarts a plugin by retrieving the plugin
     * from the context and calling {@link Plugin#stop()} and {@link Plugin#start()}
     * sequencially
     *
     * @param pluginName the name of the plugin to start
     */
    void restartPlugin(String pluginName);

    /**
     * Execute a method marked with {@link ExtensionMethod}
     * in a {@link Plugin}
     *
     * @param plugin     the plugin on which to invoke the method
     * @param methodName the name of the method
     * @param parameters optional parameters that the method can require
     * @return the object returned by the method
     */
    Object execExtension(Plugin plugin, String methodName, Object... parameters);

    /**
     * Execute a method marked with {@link ExtensionMethod}
     * in a {@link Plugin} after retrieving the plugin from the context
     *
     * @param pluginName the name of the plugin on which to invoke the method
     * @param methodName the name of the method
     * @param parameters optional parameters that the method can require
     * @return the object returned by the method
     */
    Object execExtension(String pluginName, String methodName, Object... parameters);

    /**
     * Returns the current context
     *
     * @return the context on which the manager lives in
     */
    PlugfaceContext getContext();

    /**
     * Loads all the plugins from the specified folder
     *
     * @param pluginFolder the folder from which to load plugins
     * @return a list of the loaded plugins
     */
    List<Plugin> loadPlugins(String pluginFolder);

    /**
     * Loads all the plugins from the predefined folder
     *
     * @return a list of the loaded plugins
     * @see #setPluginFolder(String)
     */
    List<Plugin> loadPlugins();

    /**
     * Loads all the plugins from the predefined folder
     *
     * @param autoregister true if the plugins must be automatically registered in the context
     * @return a list of the loaded plugins
     * @see #setPluginFolder(String)
     */
    List<Plugin> loadPlugins(boolean autoregister);

    /**
     * Loads all the plugins from the specified folder
     *
     * @param pluginFolder the folder from which to load plugins
     * @param autoregister true if the plugins must be automatically registered in the context
     * @return a list of the loaded plugins
     */
    List<Plugin> loadPlugins(String pluginFolder, boolean autoregister);

    /**
     * Returns the folder from which to load the plugins
     *
     * @return the folder from which to load the plugins
     */
    String getPluginFolder();

    /**
     * Sets the folder from which to load the plugins
     *
     * @param pluginFolder the folder from which to load the plugins
     */
    void setPluginFolder(String pluginFolder);

    /**
     * Returns the properties file that specifies the permissions for loaded plugins
     *
     * @return the properties file that specifies the permissions for loaded plugins
     */
    Properties getPermissions();

    /**
     * Sets the properties file that specifies the permissions for loaded plugins
     *
     * @param permissions the properties file that specifies the permissions for loaded plugins
     */
    void setPermissions(Properties permissions);

    /**
     * Check whether a plugin exposes a specific {@link ExtensionMethod}
     *
     * @param pluginName    the name of the plugin
     * @param extensionName the name of the extension method
     * @return true if the plugin exposes the method, false if it doesn't
     */
    boolean hasExtension(String pluginName, String extensionName);

    /**
     * Check whether a plugin exposes a specific {@link ExtensionMethod}
     *
     * @param plugin        the plugin
     * @param extensionName the name of the extension method
     * @return true if the plugin exposes the method, false if it doesn't
     */
    boolean hasExtension(Plugin plugin, String extensionName);

    /**
     * Returns the name of the manager
     *
     * @return the name of the manager
     */
    String getName();

    /**
     * If the {@link PluginStatus} of the plugin is not
     * "ERROR", sets it to "ENABLED"
     * meaning it is ready to be executed.
     *
     * @param pluginName the name of the plugin
     * @return the new status of the plugin
     */
    PluginStatus enablePlugin(String pluginName);

    /**
     * If the {@link PluginStatus} of the plugin is not
     * "ERROR", sets it to "ENABLED"
     * meaning it is ready to be executed.
     *
     * @param plugin the plugin
     * @return the new status of the plugin
     */
    PluginStatus enablePlugin(Plugin plugin);

    /**
     * If the {@link PluginStatus} of the plugin is not
     * "ERROR", sets it to "DISABLED"
     * meaning it can't be executed.
     *
     * @param plugin the plugin
     * @return the new status of the plugin
     */
    PluginStatus disablePlugin(Plugin plugin);

    /**
     * If the {@link PluginStatus} of the plugin is not
     * "ERROR", sets it to "DISABLED"
     * meaning it can't be executed.
     *
     * @param pluginName the name of the plugin
     * @return the new status of the plugin
     */
    PluginStatus disablePlugin(String pluginName);

    /**
     * Check whether the manager is in debug
     * mode or not.
     *
     * @return true if in debug mode, false if not
     */
    boolean isDebug();

    /**
     * Sets the debug mode.
     * If true, it will load plugins from a simple project
     * folder instead of a full fat Jar file
     *
     * @param debug true to enable debug mode, false to disable it
     */
    void setDebug(boolean debug);

}
