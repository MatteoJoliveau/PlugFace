package org.plugface;

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

/**
 * Defines a Plugin for PlugFace.
 * <p>
 * It is the main entry point of all plugins.
 * It defines the base utility and lifecycle methods commons to all plugins.
 * </p>
 *
 * @param <Input>  the input type for {@link #execute(Object)}
 * @param <Output> the output type for {@link #execute(Object)}
 * @see DefaultPlugin
 * @see SimplePlugin
 */
public interface Plugin<Input, Output> {

    /**
     * Starts the execution of the plugin.
     * Equivalent to a {@code public static void main()} for a standard application.
     */
    void start();

    /**
     * Stops the execution of the plugin.
     * Should contain the logic to gracefully stops the plugin.
     */
    void stop();

    /**
     * Execute operations given parameter of type {@link Input} and returning
     * an object of type {@link Output}
     *
     * @param parameters the parameters that the method accepts
     * @return and object of type {@link Output}
     */
    Output execute(Input parameters);

    /**
     * Returns the {@link PluginConfiguration} of the plugin
     *
     * @return the {@link PluginConfiguration} of the plugin
     */
    PluginConfiguration getPluginConfiguration();

    /**
     * Sets the {@link PluginConfiguration} of the plugin
     *
     * @param configuration the {@link PluginConfiguration} for the plugin
     */
    void setPluginConfiguration(PluginConfiguration configuration);

    /**
     * Returns the plugin name
     *
     * @return the plugin name
     */
    String getName();

    /**
     * Returns the current {@link PluginStatus} of the plugin
     *
     * @return the current {@link PluginStatus} of the plugin
     */
    PluginStatus getStatus();

    /**
     * Sets the {@link PluginStatus} of the plugin
     * @param pluginStatus the new {@link PluginStatus} of the plugin
     */
    void setStatus(PluginStatus pluginStatus);

    /**
     * Sets a private field to {@link PluginStatus#ENABLED}
     */
    void enable();

    /**
     * Sets a private field to {@link PluginStatus#DISABLED}
     */
    void disable();

    /**
     * Check whether the plugin is enabled or not
     * @return true if the plugin is {@link PluginStatus#ENABLED}, false if it is {@link PluginStatus#DISABLED}
     */
    boolean isEnabled();

}
