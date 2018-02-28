package org.plugface.spring;

import org.plugface.core.old.NoSuchPluginManagerException;
import org.plugface.core.old.Plugin;
import org.plugface.core.old.PluginManager;
import org.plugface.core.old.impl.DefaultPluginContext;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.Map;

public class SpringPluginContext extends DefaultPluginContext implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public Plugin getPlugin(String pluginName) {
        Plugin plugin = super.getPlugin(pluginName);
        if (plugin == null) {
            return applicationContext.getBean(pluginName, Plugin.class);
        } else {
            return plugin;
        }
    }

    @Override
    public boolean hasPlugin(String pluginName) {
        return super.hasPlugin(pluginName) || applicationContext.containsBean(pluginName);
    }

    @Override
    public Map<String, Plugin> getPluginMap() {
        Map<String, Plugin> pluginMap = new HashMap<>(super.getPluginMap());
        pluginMap.putAll(applicationContext.getBeansOfType(Plugin.class));
        return pluginMap;
    }

    @Override
    public PluginManager getPluginManager(String managerName) throws NoSuchPluginManagerException {
        if (hasPluginManger(managerName)) {
            if (super.hasPluginManger(managerName)) {
                return super.getPluginManager(managerName);
            } else {
                return applicationContext.getBean(managerName, PluginManager.class);
            }
        } else {
            throw new NoSuchPluginManagerException(managerName + " not found in current context.");
        }
    }

    @Override
    public boolean hasPluginManger(String managerName) {
        return super.hasPluginManger(managerName) || applicationContext.containsBean(managerName);
    }

    @Override
    public Map<String, PluginManager> getPluginManagerMap() {
        Map<String, PluginManager> pluginManagerMap = new HashMap<>(super.getPluginManagerMap());
        pluginManagerMap.putAll(applicationContext.getBeansOfType(PluginManager.class));
        return pluginManagerMap;
    }
}
