package org.plugface.core;

/*-
 * #%L
 * PlugFace :: Core
 * %%
 * Copyright (C) 2017 - 2018 PlugFace
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

import java.util.Objects;

public final class PluginRef<T> {

    private final T ref;
    private final String name;
    private final Class<T> type;

    private PluginRef(T ref, String name, Class<T> type) {
        this.ref = Objects.requireNonNull(ref, "Plugin reference cannot be null");
        this.name = Objects.requireNonNull(name, "Plugin name cannot be null");
        this.type = Objects.requireNonNull(type, "Plugin type cannot be null");
    }

    @SuppressWarnings("unchecked")
    public static <T> PluginRef<T> of(T ref, String name) {
        return new PluginRef<>(ref, name, (Class<T>) ref.getClass());
    }

    public T get() {
        return ref;
    }

    public String getName() {
        return name;
    }

    public Class<T> getType() {
        return type;
    }


}
