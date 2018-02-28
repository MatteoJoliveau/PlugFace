package org.plugface.core;

import javax.annotation.Nullable;
import java.util.Collection;

public interface PluginContext {
    @Nullable
    <T> T getPlugin(String pluginName);

    @Nullable
    <T> T getPlugin(Class<T> pluginClass);

    <T> void addPlugin(T plugin);

    @Nullable
    <T> T removePlugin(T plugin);

    @Nullable
    <T> T removePlugin(String pluginName);

    boolean hasPlugin(String pluginName);

    <T> boolean hasPlugin(Class<T> pluginClass);


}
