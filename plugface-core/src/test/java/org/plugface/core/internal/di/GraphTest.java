package org.plugface.core.internal.di;

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

import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class GraphTest {


    @Test
    public void shouldNotAllowToAddSelfAsDependency() {
        final Graph graph = new Graph();
        final Node<String> node = new Node<>(String.class);
        try {
            graph.addEdge(node, node);
            fail("It should have thrown an IllegalArgumentException");
        } catch (Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
    }

    @Test
    public void shouldDetectCircularDependencies() {
        final Graph graph = new Graph();
        final Node<Integer> intNode = new Node<>(Integer.class);
        final Node<Float> floatNode = new Node<>(Float.class);

        try {
            graph.addEdge(intNode, floatNode).addEdge(floatNode, intNode);
            fail("It should have thrown a CircularDependencyException");
        } catch (Exception e) {
            assertThat(e, instanceOf(CircularDependencyException.class));
        }
    }

    @Test
    public void shouldNotDuplicateANode() {
        final Collection<Node<?>> one = new Graph()
                .addLeaf(new Node<>(String.class))
                .addLeaf(new Node<>(String.class))
                .resolve();

        assertContainsExactlyOne(one, new Node<>(String.class));

        final Collection<Node<?>> two = new Graph()
                .addEdge(new Node<>(String.class), new Node<>(Integer.class))
                .addLeaf(new Node<>(String.class))
                .resolve();

        assertContainsExactlyOne(two, new Node<>(String.class));

        final Collection<Node<?>> three = new Graph()
                .addEdge(new Node<>(String.class), new Node<>(Integer.class))
                .addEdge(new Node<>(String.class), new Node<>(Float.class))
                .resolve();

        assertContainsExactlyOne(three, new Node<>(String.class));
    }

    @Test
    public void testGraphResolution() {
        final Collection<Node<?>> resolved = seed().resolve();
        final ArrayList<Class<?>> classes = new ArrayList<>(resolved.size());
        for (Node<?> node : resolved) {
            classes.add(node.getRefClass());
        }
        assertArrayEquals(new Class<?>[]{BigDecimal.class, Float.class, Long.class, Integer.class}, classes.toArray());
    }

    private Graph seed() {
        final Node<Integer> integerNode = new Node<>(Integer.class);
        final Node<Float> floatNode = new Node<>(Float.class);
        final Node<BigDecimal> bigDecimalNode = new Node<>(BigDecimal.class);
        final Node<Long> longNode = new Node<>(Long.class);

        return new Graph()
                .addEdge(integerNode, floatNode)
                .addEdge(integerNode, longNode)
                .addEdge(floatNode, bigDecimalNode)
                .addEdge(longNode, floatNode)
                .addLeaf(bigDecimalNode)
                ;
    }

    private static <T> void assertContainsExactlyOne(Collection<T> list, T item) {
        if (!list.contains(item)) throw new AssertionError("List does not contain " + item.toString());
        int cont = 0;
        for (T i : list) {
            if (Objects.equals(i, item)) {
                cont++;
            }
        }
        assertThat(cont, is(1));
    }

}
