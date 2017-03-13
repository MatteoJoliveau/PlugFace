package com.matteojoliveau.plugface.impl;

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

import com.matteojoliveau.plugface.PlugfaceContext;
import com.matteojoliveau.plugface.Plugin;
import com.matteojoliveau.plugface.PluginConfiguration;

public abstract class DefaultPlugin<I, O> implements Plugin<I, O> {

    private String name;
    private PluginConfiguration pluginConfiguration;

    @Override
    public PluginConfiguration getPluginConfiguration() {
        return pluginConfiguration;
    }

    @Override
    public void setPluginConfiguration(PluginConfiguration configuration) {
        this.pluginConfiguration = configuration;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Plugin<?, ?>)) return false;
        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;

        DefaultPlugin<?, ?> that = (DefaultPlugin<?, ?>) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return pluginConfiguration != null ? pluginConfiguration.equals(that.pluginConfiguration) : that.pluginConfiguration == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (pluginConfiguration != null ? pluginConfiguration.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DefaultPlugin{" +
                "name='" + name + '\'' +
                ", pluginConfiguration=" + pluginConfiguration +
                '}';
    }
}
