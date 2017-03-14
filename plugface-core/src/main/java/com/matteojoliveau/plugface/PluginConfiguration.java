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

/**
 * Subclass of {@link Map} that holds a custom configuration for a plugin
 * It is basically just a thin wrapper around the {@link Map} class.
 */
public interface PluginConfiguration extends Map<String, Object> {

    /**
     * Update the configuration by adding a new set of parameters from the
     * provided {@link Map}
     * @param configuration map of parameters to add
     */
    void updateConfiguration(Map<String, Object> configuration);

    /**
     * Alternative to {@link Map#get(Object)}, returns a parameter from his name
     * @param name the key associated to the parameter
     * @param <T> the type of the parameter
     * @return the parameter
     */
    <T> T getParameter(String name);

    /**
     * Alternative to {@link Map#put(Object, Object)}, sets a parameter
     * under the specified key
     * @param name the key associated to the parameter
     * @param value the parameter
     * @param <T> the type of the parameter
     */
    <T> void setParameter(String name, T value);
}
