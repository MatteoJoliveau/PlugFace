package org.plugface.core.internal;

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

import org.junit.Before;
import org.junit.Test;
import org.plugface.core.annotations.Plugin;
import org.plugface.core.internal.di.Node;
import org.plugface.core.plugins.DependencyPlugin;
import org.plugface.core.plugins.TestPlugin;

import java.util.Collection;

import static org.junit.Assert.*;

public class AnnotationProcessorTest {

    private AnnotationProcessor sut;

    @Before
    public void setUp() throws Exception {
        sut = new AnnotationProcessor();
    }

    @Test
    public void shouldGetPluginName() {
        final String name = sut.getPluginName(new TestPlugin());
        assertEquals("test", name);
    }

    @Test
    public void shouldReturnNullIfNoNameIsProvided() {
        final String name = sut.getPluginName(new EmptyPlugin());
        assertNull(name);
    }

    @Test
    public void shouldDetectDependencies() {
        final boolean hasDependencies = sut.hasDependencies(DependencyPlugin.class);
        assertTrue(hasDependencies);
    }

    @Test
    public void shouldReturnFalseOnNoDependencies() {
        final boolean hasDependencies = sut.hasDependencies(TestPlugin.class);
        assertFalse(hasDependencies);
    }

    @Test
    public void shouldRetrieveDependencies() {
        final Collection<Node<?>> dependencies = sut.getDependencies(DependencyPlugin.class);
        assertFalse(dependencies.isEmpty());
    }

    @Plugin(name = "")
    private static class EmptyPlugin {}
    

}
