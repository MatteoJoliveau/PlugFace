package org.plugface.core.impl;

import org.plugface.core.PluginContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class DefaultPluginContext implements PluginContext {

    private final Map<String, Object> registry;

    public DefaultPluginContext() {
        registry = new HashMap<>();
    }

    public DefaultPluginContext(Map<String, Object> registry) {
        this.registry = registry;
    }

    @Override
    @Nullable
    public <T> T getPlugin(String pluginName) {
        if (registry.containsKey(pluginName)) {
            return (T) registry.get(pluginName);
        }
        return null;
    }

    @Override
    @Nullable
    public <T> T getPlugin(Class<T> pluginClass) {
        for (Object plugin : registry.values()) {
            if (pluginClass.equals(plugin.getClass())) {
                return (T) plugin;
            }
        }
        return null;
    }

    @Override
    public <T> void addPlugin(T plugin) {
        final String name = PluginUtils.getPluginName(plugin);
        if (registry.containsKey(name) || registry.containsValue(plugin)) {
            throw new IllegalArgumentException("Plugin already registered");
        }
        registry.put(name, plugin);
    }

    @Override
    @Nullable
    public <T> T removePlugin(T plugin) {
        final String name = PluginUtils.getPluginName(plugin);
        return removePlugin(name);
    }

    @Override
    @Nullable
    public <T> T removePlugin(String pluginName) {
        if (registry.containsKey(pluginName)) {
            return (T) registry.remove(pluginName);
        }
        return null;
    }

    @Override
    public boolean hasPlugin(String pluginName) {
        return registry.containsKey(pluginName);
    }

    @Override
    public <T> boolean hasPlugin(Class<T> pluginClass) {
        return getPlugin(pluginClass) != null;
    }
}
