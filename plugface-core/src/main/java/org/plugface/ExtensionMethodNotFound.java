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

import org.plugface.annotations.ExtensionMethod;

/**
 * An exception thrown when trying to execute a non existent {@link ExtensionMethod}.
 * Should be thrown when a {@link ExtensionMethod} is being retrieved from a
 * {@link PluginManager} but is not present in the extensions cache.
 */
public class ExtensionMethodNotFound extends RuntimeException {
    public ExtensionMethodNotFound() {
    }

    public ExtensionMethodNotFound(String message) {
        super(message);
    }

    public ExtensionMethodNotFound(String message, Throwable cause) {
        super(message, cause);
    }

    public ExtensionMethodNotFound(Throwable cause) {
        super(cause);
    }
}
