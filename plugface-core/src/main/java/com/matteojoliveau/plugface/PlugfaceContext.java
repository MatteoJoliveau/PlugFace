package com.matteojoliveau.plugface;

/*-
 * #%L
 * plugface-core
 * %%
 * Copyright (C) 2017 Matteo Joliveau
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
 * #L%
 */

import java.util.Map;

public interface PlugfaceContext {
    /*
        Plugins
    */
    Plugin getPlugin(String pluginName); //TODO: JavaDoc should specify that this method must throw NoSuchPluginException in body

    void addPlugin(String pluginName, Plugin plugin);

    boolean hasPlugin(String pluginName);

    Plugin removePlugin(String pluginName); //TODO: JavaDoc should specify that this method must throw NoSuchPluginException in body

    Map<String, Plugin> getPluginMap();

    /*
        Plugin Managers
     */

    PluginManager getPluginManager(String managerName); //TODO: JavaDoc should specify that this method must throw NoSuchPluginManagerException in body

    void addPluginManager(String managerName, PluginManager manager);

    boolean hasPluginManger(String managerName);

    PluginManager removePluginManager(String managerName); //TODO: JavaDoc should specify that this method must throw NoSuchPluginManagerException in body

    Map<String, PluginManager> getPluginManagerMap();


}
