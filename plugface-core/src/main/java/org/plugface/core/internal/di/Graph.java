package org.plugface.core.internal.di;

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

import java.util.*;

public class Graph {

    private Set<Node> all = new HashSet<>();
    private Map<Node, List<Node>> adj = new HashMap<>();

    public Graph addLeaf(Node<?> node) {
        addEdge(node, null);
        return this;
    }

    public Graph addEdges(Node<?> node, Collection<Node<?>> adjs) {
        for (Node<?> adj : adjs) {
            addEdge(node, adj);
        }
        return this;
    }
    public Graph addEdge(Node<?> node, Node<?> adj) {
        Objects.requireNonNull(node, "Cannot attach an edge to a null node");
        if (Objects.equals(node, adj)) {
            throw new IllegalArgumentException("Cannot connect a node to itself");
        }
        register(node);
        register(adj);
        checkCircularDependency(node, adj);
        this.adj.get(node).add(adj);
        return this;
    }

    private void register(Node<?> node) {
        if (node != null) {
            all.add(node);
            if (!this.adj.containsKey(node)) {
                this.adj.put(node, new ArrayList<Node>(0));
            }
        }
    }

    private void checkCircularDependency(Node<?> node, Node<?> adj) {
        final List<Node> twoDep = this.adj.get(adj);

        if (twoDep == null) return;

        if (twoDep.contains(node)) {

            throw new CircularDependencyException("Circular Dependency detected: %s <----> %s", node, adj);
        }


    }

    public Collection<Node<?>> resolve() {
        return topologicalSort();
    }

    private Collection<Node<?>> topologicalSort() {
        final Stack<Node> stack = new Stack<>();
        final Map<Node, Boolean> visited = new HashMap<>();
        for (Node node : all) {
            visited.put(node, false);
        }

        for (Node node : all) {
            if (!visited.get(node)) {
                doTopologicalSort(node, visited, stack);
            }
        }
        final List<Node<?>> classes = new ArrayList<>();
        for (Node node : stack) {
            classes.add(node);
        }
        return classes;
    }

    private void doTopologicalSort(Node<?> node, Map<Node, Boolean> visited, Stack<Node> stack) {
        visited.put(node, true);

        final List<Node> deps = adj.get(node);
        for (Node dep : deps) {
            final Boolean vis = visited.get(dep);
            if (vis != null && !vis) {
                doTopologicalSort(dep, visited, stack);
            }
        }
        stack.push(node);
    }
}
