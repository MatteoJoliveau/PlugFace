package com.matteojoliveau.plugface;

import java.util.Map;

public interface PluginConfiguration extends Map<String, Object> {

    void updateConfiguration(Map<String, Object> configuration);

    <T> T getParameter(String name);

    <T> void setParameter(String name, T value);
}
