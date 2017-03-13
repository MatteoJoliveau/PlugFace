package com.matteojoliveau.plugface;

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

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public interface PluginManager {
    void configurePlugin(Plugin plugin, Map<String, Object> configuration);

    void configurePlugin(String pluginName, Map<String, Object> configuration);

    void startPlugin(Plugin plugin);

    void startPlugin(String pluginName);

    void stopPlugin(Plugin plugin);

    void stopPlugin(String pluginName);

    void startAll();

    void stopAll();

    void restartPlugin(Plugin plugin);

    void restartPlugin(String pluginName);

    Object execExtension(Plugin plugin, String methodName, Object... parameters);

    Object execExtension(String pluginName, String methodName, Object... parameters);

    PlugfaceContext getContext();

    List<Plugin> loadPlugins(String pluginFolder);

    List<Plugin> loadPlugins();

    List<Plugin> loadPlugins(boolean autoregister);

    List<Plugin> loadPlugins(String pluginFolder, boolean autoregister);

    String getPluginFolder();

    void setPluginFolder(String pluginFolder);

    File getPermissionsFile();

    void setPermissionsFile(File permissionsFile);

    void setPermissionsFile(String fileName);

    boolean hasExtension(String pluginName, String extensionName);

    boolean hasExtension(Plugin plugin, String extensionName);

    String getName();
}
