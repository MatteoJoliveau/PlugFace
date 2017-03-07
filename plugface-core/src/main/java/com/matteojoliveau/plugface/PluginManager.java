package com.matteojoliveau.plugface;

import com.matteojoliveau.plugface.security.SandboxSecurityManager;
import com.matteojoliveau.plugface.security.SandboxSecurityPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.NotDirectoryException;
import java.security.Policy;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PluginManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(PluginManager.class);

    private PlugfaceContext context;
    private String name;
    private String pluginFolder;
    private static File propertiesFile;

    public PluginManager(PlugfaceContext context) {
        this.context = context;
        Policy.setPolicy(new SandboxSecurityPolicy());
        System.setSecurityManager(new SandboxSecurityManager());
        LOGGER.debug("Instantiating PluginManager");
    }

    public PluginManager(String managerName, PlugfaceContext context) {
        this(context);
        this.name = managerName;
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

    public PlugfaceContext getContext() {
        return context;
    }

    public List<Plugin> loadPlugins(String pluginFolder) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
        File folder = new File(pluginFolder);
        LOGGER.debug("Loading from supplied plugin folder");
        return load(folder);
    }

    public List<Plugin> loadPlugins() throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        File folder = new File(pluginFolder);
        LOGGER.debug("Loading from previously set plugin folder");
        if (!folder.isDirectory()) {
            throw new NotDirectoryException("Set a valid plugin directory.");
        }
        return load(folder);
    }

    private List<Plugin> load(File folder) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        List<Plugin> loadedPlugins = new ArrayList<>();
        File[] files = folder.listFiles();
        for (File file : files) {
            if (file.getName().endsWith(".jar")) {
                LOGGER.debug("Opening plugin jar file");
                JarFile pluginFile = new JarFile(file);
                Enumeration<JarEntry> entries = pluginFile.entries();

                URL[] urls = {new URL("jar:file:" + file.getPath() + "!/")};
                PluginClassLoader cl = new PluginClassLoader(urls);
                cl.setPropertiesFile(propertiesFile);

                while (entries.hasMoreElements()) {
                    LOGGER.debug("Loading plugin classes from jar");
                    JarEntry jarEntry = entries.nextElement();
                    if (jarEntry.isDirectory() || !jarEntry.getName().endsWith(".class")) {
                        continue;
                    }
                    String className = jarEntry.getName().substring(0, jarEntry.getName().length() - 6);
                    className = className.replace('/', '.');
                    Class<?> clazz = Class.forName(className, true, cl);

                    if (Plugin.class.isAssignableFrom(clazz)) {
                        LOGGER.debug("{} class loaded as Plugin", clazz.getSimpleName());
                        loadedPlugins.add((Plugin) clazz.newInstance());
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

    public File getPropertiesFile() {
        return propertiesFile;
    }

    public void setPropertiesFile(File propertiesFile) {
        PluginManager.propertiesFile = propertiesFile;
    }
    public void setPropertiesFile(String fileName) {
        PluginManager.propertiesFile = new File(fileName);
    }

    @Override
    public String toString() {
        return "PluginManager{" +
                " name='" + name + '\'' +
                ", context=" + context +
                '}';
    }


}
