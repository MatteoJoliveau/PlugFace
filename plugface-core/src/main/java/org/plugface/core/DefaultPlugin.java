package org.plugface.core;

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

import org.plugface.core.impl.DefaultPluginConfiguration;

/**
 * Abstract implementation of {@link Plugin} that provides
 * base utility and lifecycle methods
 *
 * @param <I> the input type for {@link #execute(Object)}
 * @param <O> the output type for {@link #execute(Object)}
 */
public abstract class DefaultPlugin<I, O> implements Plugin<I, O> {

    /**
     * The unique identifier of the plugin
     */
    private final String name;

    /**
     * The configuration for the plugin
     */
    private PluginConfiguration pluginConfiguration = new DefaultPluginConfiguration();

    /**
     * The current status of the plugin
     */
    private PluginStatus pluginStatus;

    private boolean pluginEnabled;

    /**
     * Construct a {@link DefaultPlugin} with the specified
     * name.
     * @param name the name of the plugin
     */
    protected DefaultPlugin(String name) {
        this.name = name;
        this.pluginStatus = PluginStatus.READY;
        this.pluginEnabled = false;
    }

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

    public PluginStatus getStatus() {
        return pluginStatus;
    }

    public void setStatus(PluginStatus pluginStatus) {
        this.pluginStatus = pluginStatus;
    }

    public void enable() {
        this.pluginEnabled = true;
    }

    public void disable() {
        this.pluginEnabled = false;
    }

    public boolean isEnabled() {
        return pluginEnabled;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Plugin<?, ?>)) return false;
        if (this == o) return true;

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
                ", pluginStatus=" + pluginStatus +
                ", pluginEnabled=" + pluginEnabled +
                '}';
    }
}
