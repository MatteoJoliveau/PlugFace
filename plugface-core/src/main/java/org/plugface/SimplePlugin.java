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
 * Abstract implementation of {@link Plugin} that extends
 * {@link DefaultPlugin} and remove the {@code execute()} functionality.
 * Useful for plugins that just need to run and forget.
 */
public abstract class SimplePlugin extends DefaultPlugin<Object, Object> {

    protected SimplePlugin(String name) {
        super(name);
    }

    /**
     * Throw {@link UnsupportedOperationException} if invoked
     *
     * @param parameters the parameters that the method accepts
     * @return nothing
     */
    @Override
    public final Object execute(Object parameters) {
        throw new UnsupportedOperationException("This plugin can't execute");
    }
}
