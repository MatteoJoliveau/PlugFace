package com.matteojoliveau.plugface;


public interface Plugin {

    abstract void start();

    abstract void stop();

    PluginConfiguration getPluginConfiguration();

    void setPluginConfiguration(PluginConfiguration configuration);

    String getName();

    void setName(String name);

    PlugfaceContext getContext();

}
