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

import com.matteojoliveau.plugface.security.SandboxSecurityPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.Policy;
import java.util.*;


public abstract class AbstractPluginManager implements PluginManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractPluginManager.class);

    private final PlugfaceContext context;
    private final String name;
    private String pluginFolder;
    private File permissionsFile;
    private final Map<Plugin, Map<String, Method>> extensions = new HashMap<>();

    protected AbstractPluginManager(PlugfaceContext context) {
        this(UUID.randomUUID().toString(), context);
    }

    protected AbstractPluginManager(String managerName, PlugfaceContext context) {
        context.addPluginManager(managerName, this);
        this.context = context;
        this.name = managerName;
        Policy.setPolicy(new SandboxSecurityPolicy());
        System.setSecurityManager(new SecurityManager());
        LOGGER.debug("Instantiating DefaultPluginManager");
    }

    @Override
    public void configurePlugin(Plugin plugin, Map<String, Object> configuration) {
        PluginConfiguration config = plugin.getPluginConfiguration();
        config.updateConfiguration(configuration);
        plugin.setPluginConfiguration(config);
    }

    @Override
    public void configurePlugin(String pluginName, Map<String, Object> configuration) {
        Plugin plugin = context.getPlugin(pluginName);
        configurePlugin(plugin, configuration);
    }

    @Override
    public void startPlugin(Plugin plugin) {
        plugin.start();
    }

    @Override
    public void startPlugin(String pluginName) {
        Plugin plugin = context.getPlugin(pluginName);
        plugin.start();
    }

    @Override
    public void stopPlugin(Plugin plugin) {
        plugin.stop();
    }

    @Override
    public void stopPlugin(String pluginName) {
        Plugin plugin = context.getPlugin(pluginName);
        plugin.stop();
    }

    @Override
    public void startAll() {
        Map<String, Plugin> all = context.getPluginMap();
        for (Plugin p : all.values()) {
            p.start();
        }
    }

    @Override
    public void stopAll() {
        Map<String, Plugin> all = context.getPluginMap();
        for (Plugin p : all.values()) {
            p.stop();
        }
    }

    @Override
    public void restartPlugin(Plugin plugin) {
        plugin.stop();
        plugin.start();
    }

    @Override
    public void restartPlugin(String pluginName) {
        Plugin plugin = context.getPlugin(pluginName);
        restartPlugin(plugin);
    }

    @Override
    public Object execExtension(Plugin plugin, String methodName, Object... parameters) {
        Method method;
        if (!extensions.containsKey(plugin)) {
            throw new NoSuchPluginException(plugin.getName() + " has no ExtensionMethods registered");
        }
        if (!extensions.get(plugin).containsKey(methodName)) {
            throw new ExtensionMethodNotFound(methodName + " not found");
        }
        method = extensions.get(plugin).get(methodName);

        Object returned = null;
        try {
            returned = method.invoke(plugin, parameters);
        } catch (IllegalAccessException | InvocationTargetException e) {
            LOGGER.error("Can't invoke this method", e);
        }

        return returned;
    }

    @Override
    public Object execExtension(String pluginName, String methodName, Object... parameters) {
        Plugin plugin = context.getPlugin(pluginName);
        return execExtension(plugin, methodName, parameters);
    }

    @Override
    public PlugfaceContext getContext() {
        return context;
    }

    @Override
    public final List<Plugin> loadPlugins(String pluginFolder) {
        return loadPlugins(pluginFolder, false);
    }

    @Override
    public final List<Plugin> loadPlugins() {
        return loadPlugins(false);
    }
    @Override
    public final List<Plugin> loadPlugins(boolean autoregister) {
       return loadPlugins(pluginFolder, autoregister);
    }

    @Override
    public String getPluginFolder() {
        return pluginFolder;
    }

    @Override
    public void setPluginFolder(String pluginFolder) {
        this.pluginFolder = pluginFolder;
    }

    @Override
    public File getPermissionsFile() {
        return permissionsFile;
    }

    @Override
    public void setPermissionsFile(File permissionsFile) {
        this.permissionsFile = permissionsFile;
    }

    @Override
    public void setPermissionsFile(String fileName) {
        this.permissionsFile = new File(fileName);
    }


    @Override
    public boolean hasExtension(String pluginName, String extensionName) {
        return hasExtension(context.getPlugin(pluginName), extensionName);
    }

    @Override
    public boolean hasExtension(Plugin plugin, String extensionName) {
        return false;
    }

    @Override
    public String getName() {
        return name;
    }

    protected Map<Plugin, Map<String, Method>> getExtensions() {
        return extensions;
    }

    @Override
    public String toString() {
        return "AbstractPluginManager{" +
                "context=" + context +
                ", name='" + name + '\'' +
                ", pluginFolder='" + pluginFolder + '\'' +
                ", permissionsFile=" + permissionsFile +
                '}';
    }
}
