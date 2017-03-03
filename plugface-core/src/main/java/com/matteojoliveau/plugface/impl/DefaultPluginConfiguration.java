package com.matteojoliveau.plugface.impl;

import com.matteojoliveau.plugface.PluginConfiguration;

import java.util.HashMap;
import java.util.Map;

public class DefaultPluginConfiguration extends HashMap<String, Object> implements PluginConfiguration {
    @Override
    public void updateConfiguration(Map<String, Object> configuration) {
        putAll(configuration);
    }

    @Override
    public Object getParameter(String name) {
        return get(name);
    }

    @Override
    public void setParameter(String name, Object value) {
        put(name, value);
    }
}
