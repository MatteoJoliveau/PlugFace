package org.plugface.core.factory;

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

import org.plugface.core.PluginSource;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PluginSources {

    /**
     * Load plugins from JAR files located at the given path
     *
     * @param pluginDirectoryPath the path to the directory where the JAR files are located
     * @return a list of loaded {@link Class} objects, never null
     */
    public static PluginSource jarSource(final String pluginDirectoryPath) {
        return jarSource(URI.create(pluginDirectoryPath));
    }

    /**
     * Load plugins from JAR files located at the given {@link URI}
     *
     * @param pluginUri the {@link URI} to the directory where the JAR files are located
     * @return a list of loaded {@link Class} objects, never null
     */
    public static PluginSource jarSource(final URI pluginUri) {
        return jarSource(pluginUri, new SecurityClassLoaderCreator());
    }

    /**
     * Load plugins from JAR files located at the given {@link URI} and loads it using given class loader
     *
     * @param pluginUri the {@link URI} to the directory where the JAR files are located
     * @param classLoaderCreator the factory that will create {@link ClassLoader}
     * @return a list of loaded {@link Class} objects, never null
     */
    public static PluginSource jarSource(final URI pluginUri, final ClassLoaderCreator classLoaderCreator) {
        return new PluginSource() {
            @Override
            public Collection<Class<?>> load() throws IOException, ClassNotFoundException {
                final ArrayList<Class<?>> plugins = new ArrayList<>();
                final Path path = Paths.get(pluginUri);
                if (!Files.exists(path)) {
                    throw new IllegalArgumentException("Path " + pluginUri + " does not exist");
                }

                if (!Files.isDirectory(path)) {
                    throw new IllegalArgumentException("Path " + pluginUri + " is not a directory");
                }
                final Map<Path, URL> jarUrls = new HashMap<>();
                for (Path filePath : Files.newDirectoryStream(path)) {
                    if (filePath.getFileName().toString().endsWith(".jar")) {
                        jarUrls.put(filePath, filePath.toUri().toURL());
                    }
                }
                URL[] jars = jarUrls.values().toArray(new URL[0]);
                final ClassLoader cl = classLoaderCreator.createClassLoader(jars);
                for (Path jarPaht: jarUrls.keySet()) {
                    final File file = jarPaht.toAbsolutePath().toFile();
                    final JarFile jar = new JarFile(file);
                    for (Enumeration<JarEntry> entries = jar.entries(); entries.hasMoreElements();) {
                        final JarEntry entry = entries.nextElement();
                        if (entry.isDirectory() || !entry.getName().endsWith(".class")) {
                            continue;
                        }
                        String className = entry.getName().substring(0, entry.getName().length() - 6);
                        className = className.replace('/', '.');
                        Class<?> clazz = Class.forName(className, true, cl);
                        plugins.add(clazz);
                    }
                }
                return plugins;
            }
        };

    }

    /**
     * Load plugins from the given list of {@link Class}. Mostly useful for testing and debugging
     *
     * @param classes a list of classes to load
     * @return the same list given as input, never null
     */
    public static PluginSource classList(final Class<?>... classes) {
        return new PluginSource() {
            @Override
            public Collection<Class<?>> load() throws Exception {
                return new ArrayList<Class<?>>() {{
                    addAll(Arrays.asList(classes));
                }};
            }

        };
    }
}
