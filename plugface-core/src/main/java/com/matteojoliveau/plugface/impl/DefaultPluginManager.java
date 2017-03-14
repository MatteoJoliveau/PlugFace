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
import java.nio.file.InvalidPathException;
import java.security.Policy;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Concrete implementation of {@link PluginManager} that provides a default
 * plugin loading logic.
 */
public final class DefaultPluginManager extends AbstractPluginManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultPluginManager.class);

    /**
     * Construct a {@link DefaultPluginManager} from the specified {@link PlugfaceContext}
     * with a randomly generated {@link UUID} as the name
     *
     * @param context the context in which the manager lives
     */
    public DefaultPluginManager(PlugfaceContext context) {
        super(UUID.randomUUID().toString(), context);

    }

    /**
     * Construct a {@link DefaultPluginManager} in a {@link PlugfaceContext}
     * with the specified name, also settings the {@link SandboxSecurityPolicy}
     * and {@link SecurityManager} for the plugin sandbox
     *
     * @param managerName the name to give to the manager
     * @param context     the context in which the manager lives
     */
    public DefaultPluginManager(String managerName, PlugfaceContext context) {
        super(managerName, context);
    }

    @Override
    public final List<Plugin> loadPlugins(String pluginFolder, boolean autoregister) {
        File folder = new File(pluginFolder);
        LOGGER.debug("Loading from supplied plugin folder");
        if (!folder.isDirectory()) {
            throw new InvalidPathException(pluginFolder, "Set a valid plugin directory");
        }
        List<Plugin> loaded = load(folder);
        if (autoregister) {
            for (Plugin p : loaded) {
                getContext().addPlugin(p);
            }
            return loaded;
        } else {
            return loaded;
        }
    }

    /**
     * Load all the plugins from the folder, checking the jar files name
     * and error checking the process.
     * <p>
     *     Loading occurs by finding all the jar files in the folder, getting
     *     all the {@link JarEntry} in the file and checking if the loaded classes
     *     are a valid implementation of {@link Plugin}.
     *     If so, add the class to the plugin list and check if there is any method
     *     marked with {@link com.matteojoliveau.plugface.annotations.ExtensionMethod}, then add them
     *     to the extension list. Extension methods will be accessible through Java reflection.
     * </p>
     *
     * @param folder the folder from which to load the plugins
     * @return the list of all the valid plugins found
     */
    private List<Plugin> load(File folder) {
        List<Plugin> loadedPlugins = new ArrayList<>();
        File[] files = folder.listFiles();
        assert files != null;
        for (File file : files) {
            if (file.getName().endsWith(".jar")) {
                LOGGER.debug("Opening plugin jar file");
                try (JarFile pluginFile = new JarFile(file)) {

                    Enumeration<JarEntry> entries = pluginFile.entries();

                    URL[] urls = new URL[0];
                    try {
                        urls = new URL[]{new URL("jar:file:" + file.getPath() + "!/")};
                    } catch (MalformedURLException e) {
                        LOGGER.error("Malformed URL from filepath {}", file.getPath(), e);
                    }
                    PluginClassLoader cl = new PluginClassLoader(urls);
                    cl.setPermissionsFile(getPermissionsFile());

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
                            LOGGER.error("Couldn't find class by name {}", className, e);
                        }

                        assert clazz != null;
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
                                getExtensions().put(plugin, methods);
                            } catch (InstantiationException | IllegalAccessException e) {
                                LOGGER.error("Error instanciating new Plugin class", e);
                            }
                        }
                    }
                    cl.close();
                } catch (IOException e) {
                    LOGGER.error("Plugin Jar file can't be loaded", e);
                }

            }
        }
        return loadedPlugins;
    }

    @Override
    public String toString() {
        return "DefaultPluginManager{" +
                " name='" + getName() + '\'' +
                ", context=" + getContext() +
                '}';
    }
}
