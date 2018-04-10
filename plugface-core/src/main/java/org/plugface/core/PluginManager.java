package org.plugface.core;

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

import javax.annotation.Nullable;
import java.util.Collection;

public interface PluginManager {

    /**
     * Register a plugin in the context
     *
     * @param plugin the plugin object to register
     * @param <T>    the inferred type of the plugin
     */
    <T> void register(T plugin);

    /**
     * Register a plugin in the context with the given name
     *
     * @param name   the name to associate with the plugin
     * @param plugin the plugin object to register
     * @param <T>    the inferred type of the plugin
     */
    <T> void register(String name, T plugin);

    /**
     * Return an instance of the plugin that is registered with the given name
     *
     * @param name the name of the plugin
     * @param <T>  the inferred type of the plugin
     * @return the plugin object or <code>null</code> if none exists
     */
    @Nullable
    <T> T getPlugin(String name);

    /**
     * Return an instance of the plugin matching the given type
     * given type
     *
     * @param pluginClass type the plugin must match; can be an interface or superclass. Cannot be <code>null</code>
     * @param <T>         the inferred type of the plugin
     * @return the plugin object or <code>null</code> if none exists
     */
    @Nullable
    <T> T getPlugin(Class<T> pluginClass);

    /**
     * Return the complete list of all the plugins currently registered
     *
     * @return a {@link Collection} of all the plugins
     */
    Collection<PluginRef> getAllPlugins();

    /**
     * Remove a plugin with the given name from the context
     *
     * @param name the name associated with the plugin
     * @param <T>  the inferred type of the plugin
     * @return the removed plugin, or <code>null</code> if none is exists
     */
    @Nullable
    <T> T removePlugin(String name);

    /**
     * Remove a plugin from the context
     *
     * @param plugin the plugin instance to remove
     * @param <T>    the inferred type of the plugin
     * @return the removed plugin, or <code>null</code> if none is exists
     */
    @Nullable
    <T> T removePlugin(T plugin);

    /**
     * Load a set of plugin retrieved from the give {@link PluginSource},
     * instantiating the objects, resolving any dependency and registering them
     * in the context
     * @param source the <code>PluginSource</code> to use for loading
     * @return a list of loaded plugins, never <code>null</code>
     * @throws Exception an exception that occurred during loading, see {@link PluginSource#load()}
     */
    Collection<Object> loadPlugins(PluginSource source) throws Exception;

}
