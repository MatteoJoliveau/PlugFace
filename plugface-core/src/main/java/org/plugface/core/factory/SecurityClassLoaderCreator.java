package org.plugface.core.factory;

import org.plugface.core.internal.PluginClassLoader;

import java.net.URL;

public class SecurityClassLoaderCreator implements ClassLoaderCreator {
    @Override
    public ClassLoader createClassLoader(URL[] jars) {
        return new PluginClassLoader(jars);
    }
}
