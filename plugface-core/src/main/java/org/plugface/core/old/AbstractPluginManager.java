package org.plugface.core.old;

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

import org.plugface.core.old.annotations.ExtensionMethod;
import org.plugface.core.old.security.SandboxSecurityPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.Policy;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * An abstract implementation of {@link PluginManager} that provides basic
 * management functionalities.
 * It delegates the plugin loading logic to its subclasses
 */
public abstract class AbstractPluginManager implements PluginManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractPluginManager.class);

    /**
     * The context in which the manager lives
     */
    private final PluginContext context;

    /**
     * The unique identifier of the manager
     */
    private final String name;

    /**
     * The folder from which to load the plugins
     */
    private String pluginFolder;

    /**
     * The properties file from which to load the permissions
     * to give to the plugins
     */
    private Properties permissionsProperties;

    /**
     * If true, the manager is in
     * debug mode and will load plugins from a simple project
     * folder instead of a full fat Jar file
     */
    private boolean debug;

    private ThreadFactory hcThreadFactory = null;

    private Map<Plugin, List<String>> pluginDependencies = new HashMap<>();

    /**
     * A cache that holds all the extension methods associated to a {@link Plugin}
     *
     * @see ExtensionMethod
     */
    private final Map<Plugin, Map<String, Method>> extensions = new HashMap<>();
    private ScheduledExecutorService scheduledExecutorService;

    /**
     * Construct an {@link AbstractPluginManager} in a {@link PluginContext}
     * with the specified name, also settings the {@link SandboxSecurityPolicy}
     * and {@link SecurityManager} for the plugin sandbox
     *
     * @param managerName the name to give to the manager
     * @param context     the context in which the manager lives
     */
    protected AbstractPluginManager(String managerName, PluginContext context) {
        this.context = context;
        this.name = managerName;
        Policy.setPolicy(new SandboxSecurityPolicy());
        System.setSecurityManager(new SecurityManager());
        LOGGER.debug("Instantiating DefaultPluginManager");
        context.addPluginManager(this);
        pluginHealthCheck();
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
        PluginStatus pluginStatus = plugin.getStatus();
        if (plugin.isEnabled() && !pluginStatus.equals(PluginStatus.RUNNING)) {
            plugin.start();
            plugin.setStatus(PluginStatus.RUNNING);
        } else {
            LOGGER.warn("{} is {}. Can't be started", plugin.getName(), plugin.getStatus());
        }
    }

    @Override
    public void startPlugin(String pluginName) {
        Plugin plugin = context.getPlugin(pluginName);
        startPlugin(plugin);
    }

    @Override
    public void stopPlugin(Plugin plugin) {
        PluginStatus pluginStatus = plugin.getStatus();
        if (pluginStatus.equals(PluginStatus.RUNNING)) {
            plugin.stop();
            plugin.setStatus(PluginStatus.STOPPED);
        } else {
            LOGGER.warn("{} is not running. Can't be stopped", plugin.getName());
        }
    }

    @Override
    public void stopPlugin(String pluginName) {
        Plugin plugin = context.getPlugin(pluginName);
        stopPlugin(plugin);
    }

    @Override
    public void startAll() {
        Map<String, Plugin> all = context.getPluginMap();
        for (Plugin p : all.values()) {
            startPlugin(p);
        }
    }

    @Override
    public void stopAll() {
        Map<String, Plugin> all = context.getPluginMap();
        for (Plugin p : all.values()) {
            stopPlugin(p);
        }
    }

    @Override
    public void restartPlugin(Plugin plugin) {
        stopPlugin(plugin);
        startPlugin(plugin);
    }

    @Override
    public void restartPlugin(String pluginName) {
        Plugin plugin = context.getPlugin(pluginName);
        restartPlugin(plugin);
    }

    @Override
    public PluginStatus enablePlugin(String pluginName) {
        Plugin plugin = context.getPlugin(pluginName);
        return enablePlugin(plugin);
    }

    @Override
    public PluginStatus enablePlugin(Plugin plugin) {
        PluginStatus pluginStatus = plugin.getStatus();
        if (!pluginStatus.equals(PluginStatus.ERROR) && !plugin.isEnabled()) {
            plugin.enable();
            return plugin.getStatus();
        } else {
            return pluginStatus;
        }
    }

    @Override
    public Map<String, Boolean> enableAll() {
        HashMap<String, Boolean> outcome = new HashMap<>();
        for (Plugin plugin : context.getPluginMap().values()) {
            if (!plugin.getStatus().equals(PluginStatus.ERROR) && !plugin.isEnabled()) {
                plugin.enable();
                outcome.put(plugin.getName(), true);
            } else {
                outcome.put(plugin.getName(), false);
            }
        }
        return outcome;
    }

    @Override
    public PluginStatus disablePlugin(Plugin plugin) {
        PluginStatus pluginStatus = plugin.getStatus();
        if (!pluginStatus.equals(PluginStatus.ERROR) && plugin.isEnabled()) {
            plugin.disable();
            return plugin.getStatus();
        } else {
            return pluginStatus;
        }
    }

    @Override
    public PluginStatus disablePlugin(String pluginName) {
        Plugin plugin = context.getPlugin(pluginName);
        return disablePlugin(plugin);
    }

    @Override
    public Map<String, Boolean> disableAll() {
        HashMap<String, Boolean> outcome = new HashMap<>();
        for (Plugin plugin : context.getPluginMap().values()) {
            if (!plugin.getStatus().equals(PluginStatus.ERROR) && plugin.isEnabled()) {
                plugin.disable();
                outcome.put(plugin.getName(), true);
            } else {
                outcome.put(plugin.getName(), false);
            }
        }
        return outcome;
    }

    @Override
    @Deprecated
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
    @Deprecated
    public Object execExtension(String pluginName, String methodName, Object... parameters) {
        Plugin plugin = context.getPlugin(pluginName);
        return execExtension(plugin, methodName, parameters);
    }

    @Override
    public boolean isPluginImplementingApi(Plugin plugin, Class<?> apiClass) {
        return apiClass.isAssignableFrom(plugin.getClass());
    }

    @Override
    public boolean isPluginImplementingApi(String pluginName, Class<?> apiClass) {
        Plugin plugin = getContext().getPlugin(pluginName);
        return apiClass.isAssignableFrom(plugin.getClass());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> getAllImplementingPlugin(Class<T> apiClass) {
        List<T> plugins = new ArrayList<>();
        for (Plugin p : getContext().getPluginMap().values()) {
            if (apiClass.isAssignableFrom(p.getClass())) {
                plugins.add((T) p);
            }
        }
        return plugins;
    }

    @Override
    public PluginContext getContext() {
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
    public Properties getPermissions() {
        return permissionsProperties;
    }

    @Override
    public void setPermissions(Properties permissions) {
        this.permissionsProperties = permissions;
    }

    @Override
    @Deprecated
    public boolean hasExtension(String pluginName, String extensionName) {
        return hasExtension(context.getPlugin(pluginName), extensionName);
    }

    @Override
    @Deprecated
    public boolean hasExtension(Plugin plugin, String extensionName) {
        return false;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    @Override
    public String getName() {
        return name;
    }

    protected Map<Plugin, Map<String, Method>> getExtensions() {
        return extensions;
    }

    protected Map<Plugin, List<String>> getPluginDependencies() {
        return pluginDependencies;
    }

    protected void pluginHealthCheck() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(getHealthCheckThreadFactory());

        scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
            private final Logger CHECKLOGGER = LoggerFactory.getLogger(AbstractPluginManager.class);

            @Override
            public void run() {
                for (Plugin p : context.getPluginMap().values()) {
                    if (p.getStatus().equals(PluginStatus.ERROR)) {
                        CHECKLOGGER.error("HEALTH CHECK - Plugin {} is in error.", p.getName());
                        p.disable();
                    }
                }
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    public void stopHealthCheck() {
        if (!scheduledExecutorService.isShutdown()) {
            scheduledExecutorService.shutdown();
        }
    }

    private synchronized ThreadFactory getHealthCheckThreadFactory() {
        if (hcThreadFactory != null) {
            return hcThreadFactory;
        } else {
            return new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = Executors.defaultThreadFactory().newThread(r);
                    thread.setDaemon(true);
                    return thread;
                }
            };
        }
    }

    public synchronized void setHealthCheckThreadFactory(ThreadFactory threadFactory) {
        this.hcThreadFactory = threadFactory;
    }


    @Override
    public String toString() {
        return "AbstractPluginManager{" +
                "context=" + context +
                ", name='" + name + '\'' +
                ", pluginFolder='" + pluginFolder + '\'' +
                ", permissionsProperties=" + permissionsProperties +
                ", debug=" + debug +
                ", extensions=" + extensions +
                '}';
    }
}
