package org.plugface.spring;

import org.plugface.core.impl.DefaultPluginContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringPluginContext extends DefaultPluginContext implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public <T> T getPlugin(String pluginName) {
        final T plugin = super.getPlugin(pluginName);
        if (plugin == null) {
            try {
                return (T) applicationContext.getBean(pluginName);
            } catch (NoSuchBeanDefinitionException e) {
                return null;
            }
        }
        return plugin;
    }

    @Override
    public <T> T getPlugin(Class<T> pluginClass) {
        final T plugin = super.getPlugin(pluginClass);
        if (plugin == null) {
            try {
                return applicationContext.getBean(pluginClass);
            } catch (NoSuchBeanDefinitionException e) {
                return null;
            }
        }
        return plugin;
    }


    @Override
    public <T> boolean hasPlugin(Class<T> pluginClass) {
        final boolean has = super.hasPlugin(pluginClass);
        if (!has) {
            try {
                applicationContext.getBean(pluginClass);
                return true;
            } catch (NoSuchBeanDefinitionException e) {
                return false;
            }
        } else {
            return true;
        }
    }

    @Override
    public boolean hasPlugin(String pluginName) {
        return super.hasPlugin(pluginName) || applicationContext.containsBean(pluginName);
    }

}
