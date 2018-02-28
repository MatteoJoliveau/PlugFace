package org.plugface.core;

import java.util.Collection;

public interface PluginManager {

    <T> void register(T plugin);

    <T> T getPlugin(String name);

    <T> T getPlugin(Class<T> pluginClass);

    <T> T removePlugin(String name);

    <T> T removePlugin(T plugin);

    Collection<Object> loadPlugins(PluginSource source) throws IllegalAccessException, InstantiationException;

}
