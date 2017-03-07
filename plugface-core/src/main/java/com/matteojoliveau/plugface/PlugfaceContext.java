package com.matteojoliveau.plugface;

import java.util.Map;

public interface PlugfaceContext {
    /*
        Plugins
    */
    Plugin getPlugin(String pluginName) throws NoSuchPluginException;

    void addPlugin(String pluginName, Plugin plugin);

    boolean hasPlugin(String pluginName);

    Plugin removePlugin(String pluginName) throws NoSuchPluginException;

    Map<String, Plugin> getPluginMap();

    /*
        Plugin Managers
     */

    PluginManager getPluginManager(String managerName) throws NoSuchPluginManagerException ;

    void addPluginManager(String managerName, PluginManager manager);

    boolean hasPluginManger(String managerName);

    PluginManager removePluginManager(String managerName) throws NoSuchPluginManagerException ;

    Map<String, PluginManager> getPluginManagerMap();


}
