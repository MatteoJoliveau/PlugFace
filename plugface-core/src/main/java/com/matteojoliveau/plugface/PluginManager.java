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

import com.matteojoliveau.plugface.annotations.ExtensionMethod;
import com.matteojoliveau.plugface.security.SandboxSecurityPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.NotDirectoryException;
import java.security.Policy;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PluginManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(PluginManager.class);

    private PlugfaceContext context;
    private String name;
    private String pluginFolder;
    private File permissionsFile;
    private Map<Plugin, Map<String, Method>> extensions = new HashMap<>();

    public PluginManager(PlugfaceContext context) {
        this(UUID.randomUUID().toString(), context);
        this.context = context;

    }

    public PluginManager(String managerName, PlugfaceContext context) {
        context.addPluginManager(managerName, this);
        this.context = context;
        this.name = managerName;
        Policy.setPolicy(new SandboxSecurityPolicy());
        System.setSecurityManager(new SecurityManager());
        LOGGER.debug("Instantiating PluginManager");
    }

    public void configurePlugin(Plugin plugin, Map<String, Object> configuration) {
        PluginConfiguration config = plugin.getPluginConfiguration();
        config.updateConfiguration(configuration);
        plugin.setPluginConfiguration(config);
    }

    public void configurePlugin(String pluginName, Map<String, Object> configuration) {
        Plugin plugin = context.getPlugin(pluginName);
        configurePlugin(plugin, configuration);
    }

    public void startPlugin(Plugin plugin) {
        plugin.start();
    }

    public void startPlugin(String pluginName) {
        Plugin plugin = context.getPlugin(pluginName);
        plugin.start();
    }

    public void stopPlugin(Plugin plugin) {
        plugin.stop();
    }

    public void stopPlugin(String pluginName) {
        Plugin plugin = context.getPlugin(pluginName);
        plugin.stop();
    }

    public void startAll() {
        Map<String, Plugin> all = context.getPluginMap();
        for (Plugin p : all.values()) {
            p.start();
        }
    }

    public void stopAll() {
        Map<String, Plugin> all = context.getPluginMap();
        for (Plugin p : all.values()) {
            p.stop();
        }
    }

    public Object execExtension(String pluginName, String methodName, Object... parameters) {
        Plugin plugin = context.getPlugin(pluginName);
        Method method = null;
        try {
            method = extensions.get(plugin).get(methodName);
        } catch (NullPointerException e) {
            throw new ExtensionMethodNotFound(methodName + " not found");
        }
        Object returned = null;
        try {
            returned = method.invoke(plugin, parameters);
        } catch (IllegalAccessException | InvocationTargetException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }

        return returned;
    }

    public PlugfaceContext getContext() {
        return context;
    }

    public List<Plugin> loadPlugins(String pluginFolder) throws NotDirectoryException {
        File folder = new File(pluginFolder);
        LOGGER.debug("Loading from supplied plugin folder");
        if (!folder.isDirectory()) {
            throw new NotDirectoryException("Set a valid plugin directory.");
        }
        return load(folder);
    }

    public List<Plugin> loadPlugins() throws NotDirectoryException {
        File folder = new File(pluginFolder);
        LOGGER.debug("Loading from previously set plugin folder");
        if (!folder.isDirectory()) {
            throw new NotDirectoryException("Set a valid plugin directory.");
        }
        return load(folder);
    }

    private List<Plugin> load(File folder) {
        List<Plugin> loadedPlugins = new ArrayList<>();
        File[] files = folder.listFiles();
        assert files != null;
        for (File file : files) {
            if (file.getName().endsWith(".jar")) {
                LOGGER.debug("Opening plugin jar file");
                JarFile pluginFile = null;
                try {
                    pluginFile = new JarFile(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                assert pluginFile != null;
                Enumeration<JarEntry> entries = pluginFile.entries();

                URL[] urls = new URL[0];
                try {
                    urls = new URL[]{new URL("jar:file:" + file.getPath() + "!/")};
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                PluginClassLoader cl = new PluginClassLoader(urls);
                cl.setPermissionsFile(permissionsFile);

                while (entries.hasMoreElements()) {
                    LOGGER.debug("Loading plugin classes from jar");
                    JarEntry jarEntry = entries.nextElement();
                    if (jarEntry.isDirectory() || !jarEntry.getName().endsWith(".class")) {
                        continue;
                    }
                    String className = jarEntry.getName().substring(0, jarEntry.getName().length() - 6);
                    className = className.replace('/', '.');
                    Class<?> clazz = null;
                    try {
                        clazz = Class.forName(className, true, cl);
                    } catch (ClassNotFoundException e) {
                        System.err.println(e.getMessage());
                        e.printStackTrace();
                    }

                    if (Plugin.class.isAssignableFrom(clazz)) {
                        LOGGER.debug("{} class loaded as Plugin", clazz.getSimpleName());
                        try {
                            Plugin plugin = (Plugin) clazz.newInstance();
                            loadedPlugins.add(plugin);

                            Map<String, Method> methods = new HashMap<>();
                            for (Method method : clazz.getMethods()) {
                                if (method.isAnnotationPresent(ExtensionMethod.class)) {
                                    methods.put(method.getName(), method);
                                }
                            }
                            extensions.put(plugin, methods);
                        } catch (InstantiationException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return loadedPlugins;
    }

    public void registerPluginInContext(String pluginName, Plugin plugin) {
        context.addPlugin(pluginName, plugin);
    }

    public Plugin removePluginFromContext(String pluginName) {
        Plugin removed = context.removePlugin(pluginName);
        return removed;
    }

    public String getPluginFolder() {
        return pluginFolder;
    }

    public void setPluginFolder(String pluginFolder) {
        this.pluginFolder = pluginFolder;
    }

    public File getPermissionsFile() {
        return permissionsFile;
    }

    public void setPermissionsFile(File permissionsFile) {
        this.permissionsFile = permissionsFile;
    }

    public void setPermissionsFile(String fileName) {
        this.permissionsFile = new File(fileName);
    }

    public Map<Plugin, Map<String, Method>> getExtensions() {
        return extensions;
    }

    @Override
    public String toString() {
        return "PluginManager{" +
                " name='" + name + '\'' +
                ", context=" + context +
                '}';
    }


}
