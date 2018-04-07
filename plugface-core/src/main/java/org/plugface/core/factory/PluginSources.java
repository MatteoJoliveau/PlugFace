package org.plugface.core.factory;

import org.plugface.core.PluginSource;
import org.plugface.core.internal.PluginClassLoader;

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

    public static PluginSource jarSource(final String pluginDirectoryPath) {
        return jarSource(URI.create(pluginDirectoryPath));
    }

    public static PluginSource jarSource(final URI pluginUri) {
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
                final PluginClassLoader cl = new PluginClassLoader(jarUrls.values().toArray(new URL[]{}));
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
