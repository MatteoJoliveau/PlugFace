package org.plugface.core;

import java.util.Collection;

public interface PluginSource {
    Collection<Class<?>> load();
}
