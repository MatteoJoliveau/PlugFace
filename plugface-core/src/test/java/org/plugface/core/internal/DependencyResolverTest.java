package org.plugface.core.internal;

/*-
 * #%L
 * PlugFace :: Core
 * %%
 * Copyright (C) 2017 - 2018 PlugFace
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import org.junit.Before;
import org.junit.Test;
import org.plugface.core.internal.di.Node;
import org.plugface.core.plugins.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DependencyResolverTest {

//    private AnnotationProcessor mockProcessor = mock(AnnotationProcessor.class);
    private AnnotationProcessor mockProcessor = new AnnotationProcessor();
    private DependencyResolver sut;

    @Before
    public void setUp() throws Exception {
        sut = new DependencyResolver(mockProcessor);
    }

    @Test
    public void shouldResolveComplexDependencies() throws Exception {
        List<Class<?>> plugins = newArrayList(DeepDependencyPlugin.class, DependencyPlugin.class, TestPlugin.class, OptionalPlugin.class, SingleDependencyPlugin.class
        );
        Collection<Node<?>> nodes = sut.resolve(plugins);
        assertEquals(plugins.size(), nodes.size());
        for (Node node : nodes) {
            assertTrue(plugins.contains(node.getRefClass()));
        }
    }

    @Test
    public void shouldResolvePluginsWithoutDependencies() {
        List<Class<?>> plugins = newArrayList(TestPlugin.class, OptionalPlugin.class);
        final Collection<Node<?>> nodes = sut.resolve(plugins);
        assertEquals(plugins.size(), nodes.size());
        for (Node<?> node : nodes) {
            assertTrue(plugins.contains(node.getRefClass()));
        }
    }


    private <T> List<T> newArrayList(T... elements) {
        ArrayList<T> list = new ArrayList<>(elements.length);
        list.addAll(Arrays.asList(elements));
        return list;
    }



}
