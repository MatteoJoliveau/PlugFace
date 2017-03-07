package com.matteojoliveau.plugface;

import java.net.URL;
import java.net.URLClassLoader;

public class PluginClassLoader extends URLClassLoader {
    public PluginClassLoader(URL jarFileUrl) {
        super(new URL[] {jarFileUrl});
    }

    public PluginClassLoader(URL[] jarFileUrls) {
        super(jarFileUrls);
    }
}