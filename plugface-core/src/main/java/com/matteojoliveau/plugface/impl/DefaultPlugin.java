package com.matteojoliveau.plugface.impl;

import com.matteojoliveau.plugface.PlugfaceContext;
import com.matteojoliveau.plugface.Plugin;
import com.matteojoliveau.plugface.PluginConfiguration;

public abstract class DefaultPlugin implements Plugin {

    private PluginConfiguration pluginConfiguration;
    private String name;
    private PlugfaceContext context;

    public DefaultPlugin(PlugfaceContext context) {
        this.context = context;
    }

    public DefaultPlugin(String pluginName, PlugfaceContext context) {
        this.context = context;
        this.name = pluginName;
    }

    public abstract void start();

    public abstract void stop();

    public PluginConfiguration getPluginConfiguration() {
        return pluginConfiguration;
    }

    public void setPluginConfiguration(PluginConfiguration configuration) {
        this.pluginConfiguration = configuration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PlugfaceContext getContext() {
        return context;
    }

    public void setContext(PlugfaceContext context) {
        this.context = context;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DefaultPlugin that = (DefaultPlugin) o;

        if (pluginConfiguration != null ? !pluginConfiguration.equals(that.pluginConfiguration) : that.pluginConfiguration != null)
            return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return context != null ? context.equals(that.context) : that.context == null;
    }

    @Override
    public int hashCode() {
        int result = pluginConfiguration != null ? pluginConfiguration.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (context != null ? context.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Plugin{" +
                " name='" + name + '\'' +
                ", context=" + context +
                '}';
    }
}
