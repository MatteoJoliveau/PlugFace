package com.matteojoliveau.plugface.impl;

import com.matteojoliveau.plugface.*;

import java.util.HashMap;
import java.util.Map;

public class DefaultPlugfaceContext implements PlugfaceContext{
    HashMap<String, Plugin> pluginRegistry = new HashMap<>();
    HashMap<String, PluginManager> pluginManagerRegistry = new HashMap<>();

    @Override
    public Plugin getPlugin(String pluginName) throws NoSuchPluginException {
        return pluginRegistry.get(pluginName);
    }

    @Override
    public void addPlugin(String pluginName, Plugin plugin) {
        pluginRegistry.put(pluginName, plugin);

    }

    @Override
    public boolean hasPlugin(String pluginName) {
        return pluginRegistry.containsKey(pluginName);
    }

    @Override
    public Plugin removePlugin(String pluginName) throws NoSuchPluginException {
        return pluginRegistry.remove(pluginName);
    }

    @Override
    public Map<String, Plugin> getPluginMap() {
        return pluginRegistry;
    }

    @Override
    public PluginManager getPluginManager(String managerName) throws NoSuchPluginManagerException {
        return pluginManagerRegistry.get(managerName);
    }

    @Override
    public void addPluginManager(String managerName, PluginManager manager) {
        pluginManagerRegistry.put(managerName, manager);
    }

    @Override
    public boolean hasPluginManger(String managerName) {
        return pluginManagerRegistry.containsKey(managerName);
    }

    @Override
    public PluginManager removePluginManager(String managerName) throws NoSuchPluginManagerException {
        return pluginManagerRegistry.remove(managerName);
    }

    @Override
    public Map<String, PluginManager> getPluginManagerMap() {
        return pluginManagerRegistry;
    }
}
